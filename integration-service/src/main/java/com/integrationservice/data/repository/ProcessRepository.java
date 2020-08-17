package com.integrationservice.data.repository;

import com.integrationservice.data.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ProcessRepository to carry the CRUD operations for table process
 */
@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
}
