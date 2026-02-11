package com.epam.dimazak.appliances.repository.specification;

import com.epam.dimazak.appliances.model.Role;
import com.epam.dimazak.appliances.model.User;
import com.epam.dimazak.appliances.model.dto.admin.UserFilterDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> getSpecifications(UserFilterDto filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
                String pattern = "%" + filter.getSearch().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern)
                ));
            }

            if (filter.getRole() != null && !filter.getRole().isBlank() && !filter.getRole().equals("ALL")) {
                try {
                    predicates.add(cb.equal(root.get("role"), Role.valueOf(filter.getRole())));
                } catch (IllegalArgumentException ignored) {}
            }

            if (filter.getIsEnabled() != null) {
                predicates.add(cb.equal(root.get("isEnabled"), filter.getIsEnabled()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}