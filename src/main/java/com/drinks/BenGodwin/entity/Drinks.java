package com.drinks.BenGodwin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE drinks SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "drinks")
public class Drinks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull(message = "Missing required field Brand")
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull(message = "Missing required field Total Purchase Price")
    @Column(name = "total_purchase_price", nullable = false)
    private BigDecimal totalPurchasePrice;

    @NotNull(message = "Missing required field Selling Price")
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;

    @Column(name = "quantity_in_stock", nullable = false)
    private int quantityInStock;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Setter(AccessLevel.NONE)
    private Date updatedAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "deleted_at")
    private Date deletedAt;
}
