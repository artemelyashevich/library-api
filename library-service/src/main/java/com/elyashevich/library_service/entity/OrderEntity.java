package com.elyashevich.library_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "orders")
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderEntity {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID bookId;

    @Column
    private LocalDateTime expireIn;

    @Column
    private LocalDateTime orderIn;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
