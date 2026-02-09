package com.epam.dimazak.appliances.controller.admin;

import com.epam.dimazak.appliances.facade.admin.AdminOrderFacade;
import com.epam.dimazak.appliances.model.OrderStatus;
import com.epam.dimazak.appliances.model.dto.order.OrderFilterDto;
import com.epam.dimazak.appliances.model.dto.order.OrderHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
public class AdminOrderController {

    private final AdminOrderFacade adminOrderFacade;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Page<OrderHistoryDto>> getAllOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String deliveryType,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        OrderFilterDto filter = new OrderFilterDto();
        filter.setSearch(search);
        filter.setStatus(status);
        filter.setDeliveryType(deliveryType);

        return ResponseEntity.ok(adminOrderFacade.getAllOrders(filter, pageable));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus newStatus
    ) {
        adminOrderFacade.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok().build();
    }
}