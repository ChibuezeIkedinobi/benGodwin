package com.drinks.BenGodwin.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE transactions SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drink_id")
    private Drinks drinks;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customers customers;

    @Column(name = "quantity_sold")
    private int quantitySold;

    @NotNull(message = "Missing required field Gain")
    @Column(name = "gain", nullable = false)
    private BigDecimal gain;

    @NotNull(message = "Missing required field Purchase Price At Transaction")
    @Column(name = "purchase_price_at_transaction", nullable = false)
    private BigDecimal purchasePriceAtTransaction;

    @NotNull(message = "Missing required field date")
    @Column(name = "date", nullable = false)
    private LocalDate date;

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
