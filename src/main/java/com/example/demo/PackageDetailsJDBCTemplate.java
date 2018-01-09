package com.example.demo;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class PackageDetailsJDBCTemplate implements PackageDetailsDAO {
	
private static Logger logger = LogManager.getLogger(PackageDetailsJDBCTemplate.class);

	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
		
	}

	@Override
	public List<PackageDetails> getPackages(PackageDetails data) {
		// TODO Auto-generated method stub
		String SQL = "SELECT * FROM PACKAGE";
		
		List<PackageDetails> packageList = jdbcTemplateObject.query(SQL, new PackageMapper());
		
		return packageList;	}

}
