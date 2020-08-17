package com.integrationservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Process Result Entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "process_result")
public class ProcessResult {

    @Id
    @Column(name = "process_id")
    private Long processId;

    @Column(columnDefinition = "LONGTEXT")
    private String result;


}
