package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

public interface FeeMgmtDAO {
	
public void setDataSource(DataSource ds);
	
	public String addFees(FeeMgmt data);
	
	public List<FeeMgmt> viewFees(FeeMgmt data);

}
