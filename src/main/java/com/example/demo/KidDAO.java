package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

public interface KidDAO {
	
	public void setDataSource(DataSource ds);
	
	public List<Kid> getKids(String groupID);
	
	public List<Kid> addKid(Kid kid);
	
	public List<Kid> getKids();
}
