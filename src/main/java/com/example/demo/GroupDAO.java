package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

public interface GroupDAO {
	
	public void setDataSource(DataSource ds);
	
	public List<GroupOfKids> addGroup(GroupOfKids data);
	
	public List<GroupOfKids> getGroups(GroupOfKids data);

}
