package com.financial.platform.repository;

import com.financial.platform.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data access for Customer entity.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
