package com.iorix2k2.infopedia.user.runner;

import java.util.Arrays;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorix2k2.infopedia.user.model.User;
import com.iorix2k2.infopedia.user.service.UserService;


@Component
public class UserStartupRunner implements CommandLineRunner
{
	@Override
	public void run(String...args) throws Exception
	{
		persistStartingData();
	}

	public void persistStartingData()
	{
		try
		{
			var is = new ClassPathResource("data/data.txt").getInputStream();
			var s = new Scanner(is).useDelimiter("\\A");
			var filesString = s.hasNext() ? s.next() : "";
			s.close();
			var files = filesString.split("\n");
			Arrays.sort(files);

			for(var file : files)
			{
				var cpr = new ClassPathResource("data/" + file);
				var user = new ObjectMapper().readValue(cpr.getInputStream(), User.class);
				userService.add(user);
			}
		}
		catch(Exception e)
		{
			System.out.println("Problems inserting the initial data!");
			System.out.println(e.getMessage());
		}
	}
	
	@Autowired
	private UserService userService;
}
