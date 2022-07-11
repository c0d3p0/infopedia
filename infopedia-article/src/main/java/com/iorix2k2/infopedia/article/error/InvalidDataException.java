package com.iorix2k2.infopedia.article.error;


public class InvalidDataException extends RuntimeException
{
	public InvalidDataException(
			InvalidDataExceptionType invalidDataExceptionType, String message)
	{
		super(message);
		this.invalidDataExceptionType = invalidDataExceptionType; 
	}

	public InvalidDataExceptionType getInvalidDataExceptionType()
	{
		return invalidDataExceptionType;
	}
	

	private InvalidDataExceptionType invalidDataExceptionType;
	
	private static final long serialVersionUID = 1L;
}
