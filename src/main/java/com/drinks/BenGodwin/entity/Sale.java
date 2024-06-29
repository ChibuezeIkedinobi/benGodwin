package com.drinks.BenGodwin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE sale SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @NotNull(message = "Missing required field Quantity Sold")
    @Column(name = "quantity_sold")
    private int quantitySold;

    @NotNull(message = "Missing required field Selling Price")
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;

    @NotNull(message = "Missing required field date")
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Setter(AccessLevel.NONE)
    @Column(name = "deleted_at")
    private Date deletedAt;


}
