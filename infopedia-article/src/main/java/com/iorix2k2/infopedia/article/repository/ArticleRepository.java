package com.iorix2k2.infopedia.article.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.iorix2k2.infopedia.article.model.Article;


public interface ArticleRepository extends JpaRepository<Article, Long>
{
	List<Article> findByUserId(Long userId);
	Optional<Article> findByIdAndUserId(Long id, Long userId);
	List<Article> findByTitleContainingIgnoreCase(String title);
	
	@Query(value="SELECT * FROM article ORDER BY RANDOM() FETCH FIRST :amount ROWS ONLY", nativeQuery=true)
	List<Article> findTopAmountOrderByRandom(Long amount);
	
	@Transactional
	@Modifying
	int deleteByUserId(Long userId); 
}
