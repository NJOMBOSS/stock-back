package com.stockback.service.impl;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.FournisseurDto;
import com.stockback.dto.MvtStockDto;
import com.stockback.entity.Fournisseur;
import com.stockback.entity.MvtStock;
import com.stockback.exception.EntityNotFoundException;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.MvtStockRepository;
import com.stockback.response.RestResponse;
import com.stockback.service.MvtStockService;
import com.stockback.validator.FournisseurValidator;
import com.stockback.validator.MvtStockValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MvtStockServiceImpl implements MvtStockService {

    public static final String ID_NOT_NULL_MESSAGE = "Mouvement de stock ID est null";
    public static final String NOT_FOUND = "Aucun mouvement de stock trouvé avec cet id ";
    private final MvtStockRepository mvtStockRepository;

    @Autowired
    public MvtStockServiceImpl(MvtStockRepository mvtStockRepository) {
        this.mvtStockRepository = mvtStockRepository;
    }

    @Override
    public RestResponse getMvtStocks(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<MvtStock> mvtStocks = mvtStockRepository.findAll(pageable);
        List<MvtStock> listOfMvtStocks = mvtStocks.getContent();
        long deleted = mvtStockRepository.numberDeleteMvtStock();

        List<MvtStockDto> datas = listOfMvtStocks.stream().map(MvtStockDto::fromEntity).collect(Collectors.toList());
        RestResponse restResponse = new RestResponse();
        restResponse.setDatas(datas);
        restResponse.setPageNumber(mvtStocks.getNumber());
        restResponse.setPageSize(mvtStocks.getSize());
        restResponse.setTotalElements(mvtStocks.getTotalElements() - deleted);
        restResponse.setTotalPages(mvtStocks.getTotalPages());
        restResponse.setFirst(mvtStocks.isFirst());
        restResponse.setLast(mvtStocks.isLast());
        return restResponse;
    }

    @Override
    public MvtStockDto addMvtStock(MvtStockDto mvtStockDto) {
        try {
            List<String> errors = MvtStockValidator.validate(mvtStockDto);
            if (!errors.isEmpty()) {
                log.error("Le mouvement en stock n'est pas valide {}", mvtStockDto);
                throw new InvalidEntityException("Le mouvement en stock n'est pas valide", ErrorCodes.MOUVEMENT_EN_STOCK_NOT_VALID, errors);
            }
            return MvtStockDto.fromEntity(mvtStockRepository.save(MvtStockDto.toEntity(mvtStockDto)));
        } catch (DataIntegrityViolationException e1) {
            throw new InvalidEntityException("Ce mouvement en stock existe déjà dans la base de données", ErrorCodes.MOUVEMENT_EN_STOCK_NAME_IS_EXIST);
        }
    }

    @Override
    public MvtStockDto updateMvtStock(MvtStockDto mvtStockDto, Integer id) {
        Optional<MvtStock> mvtStock = mvtStockRepository.findById(id);
        if (mvtStock.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.MOUVEMENT_EN_STOCK_NOT_FOUND
            );
        }
        List<String> errors = MvtStockValidator.validate(mvtStockDto);
        if (!errors.isEmpty()) {
            log.error("Le mouvement en stock n'est pas valide {}", mvtStockDto);
            throw new InvalidEntityException("Le mouvement en stock n'est pas valide", ErrorCodes.MOUVEMENT_EN_STOCK_NOT_VALID, errors);
        }
        try {
            mvtStock.get().setLibelle(mvtStockDto.getLibelle());
            return mvtStockDto.fromEntity(mvtStockRepository.save(mvtStock.get()));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidEntityException("Ce mouvement en stock existe déjà dans la base de données", ErrorCodes.MOUVEMENT_EN_STOCK_NAME_IS_EXIST);
        }
    }

    @Override
    public Optional<MvtStockDto> findById(Integer id) {
        Optional<MvtStock> mvtStock = mvtStockRepository.findById(id);
        if (mvtStock.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.MOUVEMENT_EN_STOCK_NOT_FOUND
            );
        }
        return Optional.ofNullable(MvtStockDto.fromEntity(mvtStock.get()));
    }

    @Override
    public MvtStockDto deleteMvtStock(Integer id) {
        if (id == null) {
            log.error(ID_NOT_NULL_MESSAGE);
            throw new EntityNotFoundException(
                    AppsConstant.ID_NOT_NULL_MESSAGE,
                    ErrorCodes.MOUVEMENT_EN_STOCK_NOT_FOUND
            );
        }
        Optional<MvtStock> mvtStock = mvtStockRepository.findById(id);
        if (mvtStock.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.MOUVEMENT_EN_STOCK_NOT_FOUND
            );
        }
        mvtStock.get().setDeleted(true);

        mvtStock.get().setLibelle(mvtStock.get().getLibelle() + "-del-" + mvtStock.get().getId());
        return MvtStockDto.fromEntity(mvtStockRepository.save(mvtStock.get()));
    }

}
