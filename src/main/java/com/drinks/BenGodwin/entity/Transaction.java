package com.drinks.BenGodwin.entity;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE transaction SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    private Users cashier;

    @NotNull(message = "Missing required field Total Amount")
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @NotNull(message = "Missing required field Amount Paid")
    @Column(name = "amount_paid", nullable = false)
    private BigDecimal amountPaid;

    @NotNull(message = "Missing required field Balance")
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @NotNull(message = "Missing required field Discount")
    @Column(name = "discount", nullable = false)
    private BigDecimal discount;

    @NotNull(message = "Missing required field createdAt")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionItem> items;

    @NotNull(message = "Missing required field Paid")
    @Column(name = "paid", nullable = false)
    private boolean paid;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Setter(AccessLevel.NONE)
    private Date updatedAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @PrePersist
    public void prePersist() {
        calculateBalanceAndPaidStatus();
    }

    @PreUpdate
    public void preUpdate() {
        calculateBalanceAndPaidStatus();
    }

    private void calculateBalanceAndPaidStatus() {
        this.balance = this.totalAmount.subtract(this.amountPaid);
        this.paid = this.balance.compareTo(BigDecimal.ZERO) <= 0;
    }
}