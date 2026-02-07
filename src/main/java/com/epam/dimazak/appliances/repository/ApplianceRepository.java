package com.epam.dimazak.appliances.repository;

import com.epam.dimazak.appliances.model.Appliance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long>, JpaSpecificationExecutor<Appliance> {

    Page<Appliance> findAllByActiveTrue(Pageable pageable);

    Page<Appliance> findAllByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);

    Page<Appliance> findByNameEnContainingIgnoreCaseOrNameUaContainingIgnoreCase(String nameEn, String nameUa, Pageable pageable);
}
