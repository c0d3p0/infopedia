package com.iorix2k2.infopedia.integration.model;

import java.util.List;


public class Catalogue<T>
{
	public Catalogue() {}
	
	public Catalogue(List<T> list)
	{
		setList(list);
	}
	
	public List<T> getList()
	{
		return list;
	}

	public void setList(List<T> list)
	{
		this.list = list;
	}


	private List<T> list;
}
