package com.rakesh.sms.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

public class SessionFactoryList {

	
	private static List<SessionFactory> sessionFactoryList;
	
	
	public List<SessionFactory> getSessionFactoryList() 
	{
		return sessionFactoryList;
	}
	public void setSessionFactoryList(List<SessionFactory> sessionFactoryList) 
	{
		this.sessionFactoryList = sessionFactoryList;
	}
	
	
	public SessionFactoryList() {
		
		init();
	}
	
	public void init() {
		
		this.sessionFactoryList = new ArrayList<SessionFactory>();
		SessionFactory factory0 = DBConnection.getSessionFactory("3");
		SessionFactory factory1 = DBConnection.getSessionFactory("4");
		SessionFactory factory2 = DBConnection.getSessionFactory("5");
		SessionFactory factory3 = DBConnection.getSessionFactory("6");
		SessionFactory factory4 = DBConnection.getSessionFactory("7");
		SessionFactory factory5 = DBConnection.getSessionFactory("8");
		SessionFactory factory6 = DBConnection.getSessionFactory("9");
		SessionFactory factory7 = DBConnection.getSessionFactory("10");
		SessionFactory factory8 = DBConnection.getSessionFactory("11");
		SessionFactory factory9 = DBConnection.getSessionFactory("12");
		
		this.sessionFactoryList.add(factory0);
		this.sessionFactoryList.add(factory1);
		this.sessionFactoryList.add(factory2);
		this.sessionFactoryList.add(factory3);
		this.sessionFactoryList.add(factory4);
		this.sessionFactoryList.add(factory5);
		this.sessionFactoryList.add(factory6);
		this.sessionFactoryList.add(factory7);
		this.sessionFactoryList.add(factory8);
		this.sessionFactoryList.add(factory9);
	}
	
	
}
