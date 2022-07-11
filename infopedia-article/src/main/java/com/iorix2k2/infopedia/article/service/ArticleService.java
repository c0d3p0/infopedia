package com.iorix2k2.infopedia.article.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iorix2k2.infopedia.article.error.InvalidDataException;
import com.iorix2k2.infopedia.article.error.InvalidDataExceptionType;
import com.iorix2k2.infopedia.article.model.Article;
import com.iorix2k2.infopedia.article.repository.ArticleRepository;


@Service
public class ArticleService
{
	public List<Article> getAll()
	{
		return articleRepository.findAll();
	}
	
	public List<Article> getRandom(Long amount)
	{
		return articleRepository.findTopAmountOrderByRandom(amount);
	}
	
	public List<Article> getByUserId(Long userId)
	{
		return articleRepository.findByUserId(userId);
	}
	
	public List<Article> getByTitleWith(String title)
	{
		return articleRepository.findByTitleContainingIgnoreCase(title);
	}
	
	public List<Article> getByContentWith(String content)
	{
		return articleRepository.findByContentContainingIgnoreCase(content);
	}

	public List<Article> getByIds(Long[] ids)
	{
		return articleRepository.findAllByIdIn(ids);
	}

	public Optional<Article> getById(Long id)
	{
		return articleRepository.findById(id); 
	}

	public Optional<Article> getByIdAndUserId(Long id, Long userId)
	{
		return articleRepository.findByIdAndUserId(id, userId); 
	}
	
	public Article add(Article article)
	{
		article.setId(null);
		validateFields(article, false);		
		return articleRepository.save(article);
	}

	public Optional<Article> update(Article article)
	{
		validateFields(article, true);
		var optional = articleRepository.findById(article.getId());
		
		if(!optional.isEmpty())
		{
			article.setId(null);
			var updatedArticle = optional.get();
			setNonNull(article, updatedArticle);
			updatedArticle = articleRepository.save(updatedArticle);
			return Optional.of(updatedArticle);
		}
		
		return Optional.empty();
	}
	
	public Optional<Article> remove(Long id)
	{
		var optional = getById(id);
		
		if(!optional.isEmpty())
			articleRepository.deleteById(id);
		
		return optional;
	}
	
	public Optional<Article> remove(Article article)
	{
		return remove(article.getId());
	}	
	
	public List<Article> removeByUserId(Long userId)
	{
		var articleList = getByUserId(userId);
		
		if(articleList.size() > 0)
			articleRepository.deleteByUserId(userId);
		
		return articleList;
	}
	
	private void validateFields(Article article, boolean ignoreNull)
	{
		var fields = new String[] {"userId", "title", "content"};
		var nullFields = new boolean[] {article.getUserId() == null,
				article.getTitle() == null, article.getContent() == null};
		
		for(int i = 0; i < fields.length; i++)
		{
			if(ignoreNull && nullFields[i])
				continue;
			
			validator.validateProperty(article, fields[i]).forEach((violation) ->
			{
				throw new InvalidDataException(
						InvalidDataExceptionType.CONSTRAINT_NOT_SATISFIED, violation.getMessage());
			});
		}
	}
	
	private void setNonNull(Article from, Article to)
	{
		if(from != null)
		{
			if(from.getId() != null)
				to.setId(from.getId());
			
			if(from.getUserId() != null)
				to.setUserId(from.getUserId());
			
			if(from.getContent() != null)
				to.setContent(from.getContent());
			
			if(from.getTitle() != null)
				to.setTitle(from.getTitle());
		}
	}
	
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private Validator validator;
}
