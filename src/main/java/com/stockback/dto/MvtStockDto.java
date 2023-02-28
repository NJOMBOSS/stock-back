package com.stockback.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stockback.entity.Fournisseur;
import com.stockback.entity.MvtStock;
import com.stockback.entity.TypeMvtStk;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Builder
public class MvtStockDto {

    private Integer id;

    private String libelle;

    @JsonIgnore
    private List<TypeMvtStk> typeMvtStks;

    public static MvtStockDto fromEntity(MvtStock mvtStock) {
        if (mvtStock == null) {
            return null;
        }
        return MvtStockDto.builder()
                .id(mvtStock.getId())
                .libelle(mvtStock.getLibelle())

                .build();
    }

    public static MvtStock toEntity(MvtStockDto dto) {

        if (dto == null) {
            return null;
        }
        MvtStock mvtStock = new MvtStock();
        mvtStock.setId(dto.getId());
        mvtStock.setLibelle(dto.getLibelle());

        return mvtStock;
    }
}
