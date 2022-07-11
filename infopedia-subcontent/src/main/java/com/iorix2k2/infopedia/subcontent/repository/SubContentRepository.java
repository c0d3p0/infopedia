package com.iorix2k2.infopedia.subcontent.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.iorix2k2.infopedia.subcontent.model.SubContent;


public interface SubContentRepository extends JpaRepository<SubContent, Long>
{
	List<SubContent> findByArticleIdOrderByPositionAscIdAsc(Long articleId);
	List<SubContent> findByContentContainingIgnoreCase(String content);
	List<SubContent> findByUserId(Long userId);
	
	@Transactional
	@Modifying
	int deleteByArticleId(Long articleId);
	
	@Transactional
	@Modifying
	int deleteByUserId(Long userId); 
}
