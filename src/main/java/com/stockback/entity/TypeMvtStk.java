package com.stockback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "typemvtstk")
public class TypeMvtStk extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "idmvtstk")
    private MvtStock mvtStock;

    @Column(name = "datemvt")
    private Instant dateMvt;

    @Column(name = "quantite")
    private BigDecimal quantite;

    @Column(name = "sourcemvt")
    @Enumerated(EnumType.STRING)
    private SourceMvtStk sourceMvt;
    @ManyToOne(optional = false)
    private MvtStock mvtStocks;

    public MvtStock getMvtStocks() {
        return mvtStocks;
    }

    public void setMvtStocks(MvtStock mvtStocks) {
        this.mvtStocks = mvtStocks;
    }
}
