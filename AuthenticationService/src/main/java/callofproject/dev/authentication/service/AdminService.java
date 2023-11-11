package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.MessageResponseDTO;
import callofproject.dev.authentication.dto.MultipleMessageResponseDTO;
import callofproject.dev.authentication.dto.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.UsersShowingAdminDTO;
import callofproject.dev.authentication.mapper.IUserMapper;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

import static java.util.stream.StreamSupport.stream;
import static org.springframework.data.domain.Sort.by;

@Service
@Lazy
public class AdminService
{
    @Value("${spring.data.web.pageable.default-page-size}")
    private int m_defaultPageSize;
    private final UserManagementServiceHelper m_managementServiceHelper;
    private final IUserMapper m_userMapper;

    public AdminService(UserManagementServiceHelper managementServiceHelper, IUserMapper userMapper)
    {
        m_managementServiceHelper = managementServiceHelper;
        m_userMapper = userMapper;
    }

    public MultipleMessageResponseDTO<UsersShowingAdminDTO> findAllUsersPageable(int page)
    {
        try
        {
            var pageable = PageRequest.of(page - 1, m_defaultPageSize);

            var dtoList = m_userMapper
                    .toUsersShowingAdminDTO(stream(m_managementServiceHelper.getUserServiceHelper().findAllPageable(pageable).spliterator(), true)
                            .map(m_userMapper::toUserShowingAdminDTO).toList());

            var msg = String.format("%d user found!", dtoList.users().size());

            return new MultipleMessageResponseDTO<>(page, dtoList.users().size(), msg, HttpStatus.SC_OK, dtoList);

        } catch (DataServiceException ex)
        {
            throw new DataServiceException("Internal Server Error!");
        }
    }


    public MessageResponseDTO<Boolean> removeUser(String username)
    {
        try
        {
            var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(username);
            if (user.isEmpty())
                throw new DataServiceException("User Not Found!");

            if (user.get().isAdminOrRoot())
                throw new DataServiceException("You cannot remove this user!");

            m_managementServiceHelper.getUserServiceHelper().removeUser(user.get());

            return new MessageResponseDTO<>("User removed Successfully!", HttpStatus.SC_OK, true);
        } catch (DataServiceException exception)
        {
            return new MessageResponseDTO<>(exception.getMessage(), HttpStatus.SC_BAD_REQUEST, false);
        }
    }

    public MultipleMessageResponseDTO<UsersShowingAdminDTO> findUsersByUsernameContainsIgnoreCase(int page, String word)
    {
        try
        {
            var pageable = PageRequest.of(page - 1, m_defaultPageSize);

            var dtoList = m_userMapper
                    .toUsersShowingAdminDTO(stream(m_managementServiceHelper.getUserServiceHelper().findUsersByUsernameContainsIgnoreCase(word, pageable).spliterator(), true)
                            .map(m_userMapper::toUserShowingAdminDTO).toList());

            var msg = String.format("%d user found!", dtoList.users().size());

            return new MultipleMessageResponseDTO<>(page, dtoList.users().size(), msg, HttpStatus.SC_OK, dtoList);

        } catch (DataServiceException ex)
        {
            throw new DataServiceException("Internal Server Error!");
        }
    }


    public MultipleMessageResponseDTO<UsersShowingAdminDTO> findUsersByUsernameNotContainsIgnoreCase(int page, String word)
    {
        try
        {
            var pageable = PageRequest.of(page - 1, m_defaultPageSize);

            var dtoList = m_userMapper
                    .toUsersShowingAdminDTO(stream(m_managementServiceHelper.getUserServiceHelper()
                            .findUsersByUsernameNotContainsIgnoreCase(word, pageable)
                            .spliterator(), true)
                            .map(m_userMapper::toUserShowingAdminDTO).toList());

            var msg = String.format("%d user found!", dtoList.users().size());

            return new MultipleMessageResponseDTO<>(page, dtoList.users().size(), msg, HttpStatus.SC_OK, dtoList);

        } catch (DataServiceException ex)
        {
            throw new DataServiceException("Internal Server Error!");
        }
    }

}
