package com.example.demo;



import java.util.List;

import javax.sql.DataSource;

public interface AttendanceDAO {
	
	public void setDataSource(DataSource ds);
	
	public String markAttendance(MarkAttendance data);
	
	public List<Attendance> checkAttendance(Attendance data);
}
