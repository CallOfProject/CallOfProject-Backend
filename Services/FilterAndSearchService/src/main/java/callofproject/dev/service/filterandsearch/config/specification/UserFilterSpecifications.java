package callofproject.dev.service.filterandsearch.config.specification;

import callofproject.dev.repository.authentication.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserFilterSpecifications
{
    public static Specification<User> searchUsers(String keyword)
    {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank())
            {
                return criteriaBuilder.conjunction();
            } else
            {
                String pattern = "%" + keyword.toLowerCase() + "%";
                var usernamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), pattern);
                var firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern);
                var middleNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("middleName")), pattern);
                var lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern);

                return criteriaBuilder.or(usernamePredicate, firstNamePredicate, middleNamePredicate, lastNamePredicate);
            }
        };
    }
}
