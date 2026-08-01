package com.pms.calprop.repositories;

import com.pms.calprop.entities.Bill;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, UUID> {

    List<Bill> findByClientId(String clientId);

    Optional<Bill> findByIdAndClientId(UUID id, String clientId);
}
