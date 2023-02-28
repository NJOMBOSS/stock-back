package com.stockback.validator;


import com.stockback.constant.AppsConstant;
import com.stockback.dto.EntrepriseDto;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EntrepriseValidator {

    public static List<String> validate(EntrepriseDto dto) {
        List<String> errors = new ArrayList<>();
        if (dto == null) {
            errors.add("Le nom est obligatoire");
            return errors;
        }

        if (!StringUtils.hasLength(dto.getNom())) {
            errors.add("Le nom est obligatoire");
        }

        if (StringUtils.hasLength(dto.getNom()) && (dto.getNom().length() > AppsConstant.NUMBER_CHARACTER_NAME)) {
            errors.add("Le nom est  très long !!!");
        }


        return errors;
    }

}
