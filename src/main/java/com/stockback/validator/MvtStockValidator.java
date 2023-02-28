package com.stockback.validator;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.MvtStockDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MvtStockValidator {

    public static List<String> validate(MvtStockDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Le libelle est obligatoire");
            return errors;
        }

        if (!StringUtils.hasLength(dto.getLibelle())) {
            errors.add("Le libelle est obligatoire");
        }

        if (StringUtils.hasLength(dto.getLibelle()) && (dto.getLibelle().length() > AppsConstant.NUMBER_CHARACTER_NAME)) {
            errors.add("Le libelle est  très long !!!");
        }


        return errors;
    }
}
