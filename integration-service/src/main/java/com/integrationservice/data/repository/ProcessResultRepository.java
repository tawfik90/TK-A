package com.integrationservice.data.repository;

import com.integrationservice.data.entity.ProcessResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ProcessResultRepository to carry the CRUD operations for table process_result
 */
@Repository
public interface ProcessResultRepository extends JpaRepository<ProcessResult, Long> {
}
