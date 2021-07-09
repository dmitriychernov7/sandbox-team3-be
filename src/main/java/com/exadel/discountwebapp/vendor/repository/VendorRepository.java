package com.exadel.discountwebapp.vendor.repository;

import com.exadel.discountwebapp.vendor.entity.Vendor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorRepository
        extends CrudRepository<Vendor, Long>, JpaSpecificationExecutor<Vendor> {
    Optional<Vendor> findByTitle(String title);

    Optional<Vendor> findByEmail(String email);

    boolean existsByEmail(String email);
}
