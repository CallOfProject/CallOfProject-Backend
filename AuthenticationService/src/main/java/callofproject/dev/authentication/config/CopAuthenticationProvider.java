package callofproject.dev.authentication.config;

import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class CopAuthenticationProvider implements AuthenticationProvider
{

    @Autowired
    private UserManagementServiceHelper m_userManagementServiceHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Optional<User> user = m_userManagementServiceHelper.getUserServiceHelper().findByUsername(username);
        if (user.isPresent())
        {
            if (passwordEncoder.matches(pwd, user.get().getPassword()))
                return new UsernamePasswordAuthenticationToken(username, pwd,
                        getGrantedAuthorities(user.get().getRoles()));
            else
                throw new BadCredentialsException("Invalid password!");
        }
        else throw new BadCredentialsException("No user registered with this details!");
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<Role> authorities)
    {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (var authority : authorities)
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));

        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}