package com.stockback.service.impl;

import com.stockback.constant.AppsConstant;
import com.stockback.dto.ArticleDto;
import com.stockback.dto.CategorieDto;
import com.stockback.dto.EntrepriseDto;
import com.stockback.entity.Article;
import com.stockback.entity.Categorie;
import com.stockback.entity.Entreprise;
import com.stockback.exception.EntityNotFoundException;
import com.stockback.exception.ErrorCodes;
import com.stockback.exception.InvalidEntityException;
import com.stockback.repository.ArticleRepository;
import com.stockback.response.RestResponse;
import com.stockback.service.ArticleService;
import com.stockback.validator.ArticleValidator;
import com.stockback.validator.EntrepriseValidator;
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
public class ArticleServiceImpl implements ArticleService {

    public static final String ID_NOT_NULL_MESSAGE = "Categorie ID est null";

    public static final String NOT_FOUND = "Aucune categorie trouvée avec cet id ";

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public RestResponse getArticles(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Article> articles = articleRepository.findAll(pageable);
        List<Article> listOfArticles = articles.getContent();
        long deleted = articleRepository.numberDeleteArticle();

        List<ArticleDto> datas = listOfArticles.stream().map(ArticleDto::fromEntity).collect(Collectors.toList());
        RestResponse restResponse = new RestResponse();
        restResponse.setDatas(datas);
        restResponse.setPageNumber(articles.getNumber());
        restResponse.setPageSize(articles.getSize());
        restResponse.setTotalElements(articles.getTotalElements() - deleted);
        restResponse.setTotalPages(articles.getTotalPages());
        restResponse.setFirst(articles.isFirst());
        restResponse.setLast(articles.isLast());
        return restResponse;
    }

    @Override
    public ArticleDto addArticle(ArticleDto articleDto) {
        try {
            List<String> errors = ArticleValidator.validate(articleDto);
            if (!errors.isEmpty()) {
                log.error("L'article n'est pas valide {}", articleDto);
                throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
            }
            return ArticleDto.fromEntity(articleRepository.save(ArticleDto.toEntity(articleDto)));
        } catch (DataIntegrityViolationException e1) {
            throw new InvalidEntityException("Cet article existe déjà dans la base de données", ErrorCodes.ARTICLE_NAME_IS_EXIST);
        }
    }

    @Override
    public ArticleDto updateArticle(ArticleDto articleDto, Integer id) {
        Optional<Article> article =articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.CATEGORY_NOT_FOUND
            );
        }
        List<String> errors = ArticleValidator.validate(articleDto);
        if (!errors.isEmpty()) {
            log.error("L'article n'est pas valide {}", articleDto);
            throw new InvalidEntityException("L'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }
        try {
            article.get().setCodeArticle(articleDto.getCodeArticle());
            article.get().setDesignation(articleDto.getDesignation());
            article.get().setPrixUnitaireHt(articleDto.getPrixUnitaireHt());
            article.get().setTauxTva(articleDto.getTauxTva());
            article.get().setPrixUnitaireTtc(articleDto.getPrixUnitaireTtc());
            article.get().setPhoto(articleDto.getPhoto());
            article.get().setEntreprise(articleDto.getEntreprise());
            article.get().setCategorie(articleDto.getCategorie());
            return articleDto.fromEntity(articleRepository.save(article.get()));
        } catch (DataIntegrityViolationException e) {
                    throw new InvalidEntityException("Cet article existe déjà dans la base de données", ErrorCodes.ARTICLE_NAME_IS_EXIST);
        }
    }

    @Override
    public Optional<ArticleDto> findById(Integer id) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.ARTICLE_NOT_FOUND
            );
        }
        return Optional.ofNullable(ArticleDto.fromEntity(article.get()));
    }



    @Override
    public ArticleDto deleteArticle(Integer id) {
        if (id == null) {
            log.error(ID_NOT_NULL_MESSAGE);
            throw new EntityNotFoundException(
                    AppsConstant.ID_NOT_NULL_MESSAGE,
                    ErrorCodes.ARTICLE_NOT_FOUND
            );
        }
        Optional<Article> article = articleRepository.findById(id);
        if (article.isEmpty()) {
            throw new EntityNotFoundException(
                    NOT_FOUND + id,
                    ErrorCodes.ARTICLE_NOT_FOUND
            );
        }
        article.get().setDeleted(true);

       article.get().setDesignation(article.get().getDesignation() + "-del-" + article.get().getId());
        return ArticleDto.fromEntity(articleRepository.save(article.get()));
    }
}
