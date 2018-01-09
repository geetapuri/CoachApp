package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

public interface PackageDetailsDAO {
	
	public void setDataSource(DataSource ds);
	
	public List<PackageDetails> getPackages(PackageDetails data);

}
