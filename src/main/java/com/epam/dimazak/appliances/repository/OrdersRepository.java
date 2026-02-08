package com.epam.dimazak.appliances.repository;

import com.epam.dimazak.appliances.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
