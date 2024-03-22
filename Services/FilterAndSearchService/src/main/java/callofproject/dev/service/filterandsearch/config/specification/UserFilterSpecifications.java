package callofproject.dev.service.filterandsearch.config.specification;

import callofproject.dev.repository.authentication.entity.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class is used to create specifications for filtering users.
 */
public final class UserFilterSpecifications
{
    /**
     * This constructor is used to prevent instantiation of this class.
     */
    private UserFilterSpecifications()
    {
    }

    /**
     * This method is used to create a specification for searching users.
     *
     * @param keyword The keyword to search for.
     * @return The specification for searching users.
     */
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
