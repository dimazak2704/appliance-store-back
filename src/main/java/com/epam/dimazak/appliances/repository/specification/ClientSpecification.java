package com.epam.dimazak.appliances.repository.specification;

import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.Role;
import com.epam.dimazak.appliances.model.dto.admin.UserFilterDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification {

    public static Specification<Client> getSpecifications(UserFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
                String searchPattern = "%" + filter.getSearch().toLowerCase() + "%";
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate emailLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern);
                predicates.add(criteriaBuilder.or(nameLike, emailLike));
            }

            if (filter.getRole() != null && !filter.getRole().isBlank()) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("role"), Role.valueOf(filter.getRole())));
                } catch (IllegalArgumentException ignored) {
                }
            }

            if (filter.getIsEnabled() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isEnabled"), filter.getIsEnabled()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}