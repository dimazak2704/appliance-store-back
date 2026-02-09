package com.epam.dimazak.appliances.repository;

import com.epam.dimazak.appliances.model.Client;
import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    List<Orders> findAllByClientOrderByCreatedAtDesc(Client client);
    List<Orders> findAllByClientAndStatusOrderByCreatedAtDesc(Client client, OrderStatus status);
    Page<Orders> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Orders> findAllByStatusOrderByCreatedAtDesc(OrderStatus status, Pageable pageable);
}