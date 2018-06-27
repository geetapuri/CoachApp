package com.example.demo;

import javax.sql.DataSource;
import java.util.List;


public interface CoachDAO {

	public void setDataSource(DataSource ds);
	
	public List<Coach> getCoachID(Coach data);
}
