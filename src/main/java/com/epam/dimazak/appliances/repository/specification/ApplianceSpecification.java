package com.epam.dimazak.appliances.repository.specification;

import com.epam.dimazak.appliances.model.Appliance;
import com.epam.dimazak.appliances.model.dto.appliance.ApplianceFilterDto;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ApplianceSpecification {

    public static Specification<Appliance> getSpecifications(ApplianceFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isTrue(root.get("active")));

            if (filter.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), filter.getCategoryId()));
            }

            if (filter.getManufacturerId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("manufacturer").get("id"), filter.getManufacturerId()));
            }

            if (filter.getName() != null && !filter.getName().isEmpty()) {
                String searchPattern = "%" + filter.getName().toLowerCase() + "%";
                Predicate nameEnMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("nameEn")), searchPattern);
                Predicate nameUaMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("nameUa")), searchPattern);
                predicates.add(criteriaBuilder.or(nameEnMatch, nameUaMatch));
            }

            if (filter.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }

            if (filter.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}