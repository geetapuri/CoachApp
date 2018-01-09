package com.example.demo;

import java.sql.Date;
import java.util.List;

import javax.sql.DataSource;

public interface CalendarDAO {
	
	public void setDataSource(DataSource ds);
	
	public List<Schedule> getSchedule(Date date);
	
	public List<Schedule> getSchedule();
	
	public String updateSchedule(Schedule schedule);
	
	public String addSchedule(Schedule schedule);

}
