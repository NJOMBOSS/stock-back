package com.stockback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mvtstk")
public class MvtStock extends AbstractEntity{

    @Column(name = "libelle")
    private String liebelle;

    @OneToMany(mappedBy = "mvtStocks")
    private List<TypeMvtStk> typeMvtStks;
}
