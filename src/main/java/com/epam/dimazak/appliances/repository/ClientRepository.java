package com.epam.dimazak.appliances.repository;

import com.epam.dimazak.appliances.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    Optional<Client> findByEmail(String email);
    Boolean existsByEmail(String email);
}
