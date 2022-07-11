package com.iorix2k2.infopedia.subcontent.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iorix2k2.infopedia.subcontent.error.InvalidDataException;
import com.iorix2k2.infopedia.subcontent.error.InvalidDataExceptionType;
import com.iorix2k2.infopedia.subcontent.model.SubContent;
import com.iorix2k2.infopedia.subcontent.model.SubContentType;
import com.iorix2k2.infopedia.subcontent.repository.SubContentRepository;


@Service
public class SubContentService
{
	public List<SubContent> getAll()
	{
		return subContentRepository.findAll();
	}
	
	public List<SubContent> getByArticleId(Long articleId)
	{
		return subContentRepository.findByArticleIdOrderByPositionAscIdAsc(articleId);
	}
	
	public Optional<SubContent> getById(Long id)
	{
		return subContentRepository.findById(id);
	}

	public List<SubContent> getByContentWith(String content)
	{
		return subContentRepository.findByContentContainingIgnoreCase(content);
	}

	public SubContent add(SubContent subContent)
	{
		subContent.setId(null);
		validateFields(subContent, false);
		fixContentOfTypeImageLinkOrLink(subContent);
		return subContentRepository.save(subContent);
	}

	public Optional<SubContent> update(SubContent subContent)
	{
		validateFields(subContent, true);
		var optional = subContentRepository.findById(subContent.getId());
		
		if(!optional.isEmpty())
		{
			subContent.setId(null);
			var updateSubContent = optional.get();
			setNonNull(subContent, updateSubContent);
			fixContentOfTypeImageLinkOrLink(updateSubContent);
			updateSubContent = subContentRepository.save(updateSubContent);
			return Optional.of(updateSubContent);
		}

		return Optional.empty();
	}
	
	public Optional<SubContent> remove(Long id)
	{
		var optional = subContentRepository.findById(id);
		
		if(!optional.isEmpty())
			subContentRepository.deleteById(id);
			
		return optional;
	}

	public Optional<SubContent> remove(SubContent subContent)
	{
		return remove(subContent.getId());
	}
	
	public List<SubContent> removeByArticleId(Long articleId)
	{
		var subContentList = subContentRepository.
				findByArticleIdOrderByPositionAscIdAsc(articleId);
		
		if(subContentList.size() > 0)
			subContentRepository.deleteByArticleId(articleId);
		
		return subContentList;
	}
	
	public List<SubContent> removeByUserId(Long userId)
	{
		var subContentList = subContentRepository.findByUserId(userId);
		
		if(subContentList.size() > 0)
			subContentRepository.deleteByUserId(userId);
		
		return subContentList;
	}
	
	private void validateFields(SubContent subContent, boolean ignoreNull)
	{
		var fields = new String[]{"userId", "articleId",
				"position", "type", "title", "content"};
		var nullFields = new boolean[] {subContent.getUserId() == null,
				subContent.getArticleId() == null, subContent.getPosition() == null,
				subContent.getType() == null, subContent.getTitle() == null,
				subContent.getContent() == null};
		
		for(int i = 0; i < fields.length; i++)
		{
			if(ignoreNull && nullFields[i])
				continue;
			
			validator.validateProperty(subContent, fields[i]).forEach((violation) ->
			{
				throw new InvalidDataException(InvalidDataExceptionType.CONSTRAINT_NOT_SATISFIED,
						violation.getMessage());
			});
		}
	}

	private void setNonNull(SubContent from, SubContent to)
	{
		if(from != null)
		{
			if(from.getId() != null)
				to.setId(from.getId());
			
			if(from.getArticleId() != null)
				to.setArticleId(from.getArticleId());
			
			if(from.getPosition() != null)
				to.setPosition(from.getPosition());
			
			if(from.getType() != null)
				to.setType(from.getType());
			
			if(from.getTitle() != null)
				to.setTitle(from.getTitle());
			
			if(from.getContent() != null)
				to.setContent(from.getContent());
		}
	}
	
	private void fixContentOfTypeImageLinkOrLink(SubContent subContent)
	{				
		if(subContent.getContent() != null &&
				subContent.getType() == SubContentType.IMAGE_LINK ||
				subContent.getType() == SubContentType.LINK)
		{
			var p = "(\\\\{1,2}[trsn])+|([\\u0020\\u0085\\u2028\\u2029\\u0009])+";
			subContent.setContent(subContent.getContent().replaceAll(p, "\n"));
		}
	}
	
	
	@Autowired
	private SubContentRepository subContentRepository;
	
	@Autowired
	private Validator validator;
}
