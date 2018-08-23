package com.example.demo;

//import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

public interface CalendarDAO {
	
	public void setDataSource(DataSource ds);
	
	public List<Schedule> getSchedule(LocalDate date);
	
	public List<Schedule> getSchedule(Coach coach);
	
	public List<Schedule> getSchedule(Schedule schedule);
	
	public String updateSchedule(Schedule schedule);
	
	public String addSchedule(Schedule schedule);

}
