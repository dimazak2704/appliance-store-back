package com.epam.dimazak.appliances.repository;

import com.epam.dimazak.appliances.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
}
