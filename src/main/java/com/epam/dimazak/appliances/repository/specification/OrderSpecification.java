package com.epam.dimazak.appliances.repository.specification;

import com.epam.dimazak.appliances.model.DeliveryType;
import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.Orders;
import com.epam.dimazak.appliances.model.dto.order.OrderFilterDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<Orders> getSpecifications(OrderFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
                String searchPattern = "%" + filter.getSearch().toLowerCase() + "%";

                Predicate idLike = criteriaBuilder.like(root.get("id").as(String.class), searchPattern);

                Predicate phoneLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("contactPhone")), searchPattern);

                Predicate clientNameLike = criteriaBuilder.like(criteriaBuilder.lower(root.join("client").get("name")), searchPattern);

                predicates.add(criteriaBuilder.or(idLike, phoneLike, clientNameLike));
            }

            if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("status"), OrderStatus.valueOf(filter.getStatus())));
                } catch (IllegalArgumentException ignored) {}
            }

            if (filter.getDeliveryType() != null && !filter.getDeliveryType().isBlank()) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("deliveryType"), DeliveryType.valueOf(filter.getDeliveryType())));
                } catch (IllegalArgumentException ignored) {}
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}