package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

public interface ParentDAO {
	
	public void setDataSource(DataSource ds);
	
	public List<Parent> getParentID(Parent data);
}
