package com.pms.calprop.repositories;

import com.pms.calprop.entities.Bill;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByClientId(String clientId);

    Optional<Bill> findByIdAndClientId(Long id, String clientId);
}
