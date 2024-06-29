package com.drinks.BenGodwin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE batch SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "batch")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @NotNull(message = "Missing required field Quantity")
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull(message = "Missing required field Remaining Quantity")
    @Column(name = "remaining_quantity", nullable = false)
    private int remainingQuantity;

    @NotNull(message = "Missing required field Batch Price")
    @Column(name = "batch_price", nullable = false)
    private BigDecimal batchPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean completed;

    @OneToMany(mappedBy = "batch")
    private List<Sale> sales;


    @Setter(AccessLevel.NONE)
    @Column(name = "deleted_at")
    private Date deletedAt;

}
