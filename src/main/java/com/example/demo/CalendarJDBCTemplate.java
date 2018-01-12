package com.example.demo;

import java.sql.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;




public class CalendarJDBCTemplate implements CalendarDAO{
	
private static Logger logger = LogManager.getLogger(CalendarJDBCTemplate.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;

//Instead of dataSource I had written ds.. and got exceptions
	public void setDataSource(DataSource dataSource) {
		//this.dataSource = dataSource;
	    this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	   
	}

@Override
public List<Schedule> getSchedule(Date date) {
	// TODO Auto-generated method stub - query for finding the schedule based on date
	logger.info("Calling getSchedule()  ");
	
	logger.info("date to be queried = " + date);

	String SQL = "select Calendar.Time AS TIME, Calendar.Date As Date, Calendar.CalendarID As CalendarID,  "
			+ "GroupOfKids.GroupName AS GroupName, GroupOfKids.GroupID AS GroupID "
			+ "from Calendar, GroupOfKids "
			+ "where Calendar.GroupOfKids_GroupID = GroupOfKids.GroupID "
			+ "AND Calendar.Date like ?";
	
    List <Schedule> schedule = jdbcTemplateObject.query(SQL, new Object[] {date},new CalendarMapper());
    
   
    return schedule;
	
}

@Override
public List<Schedule> getSchedule() {
	// TODO Auto-generated method stub - query for finding the schedule based on date
	logger.info("Calling getSchedule()  ");
	
	
	String SQL = "select C.CalendarID, C.Date, C.Time, " + 
			"		G.GroupID, G.GroupName	" + 
			"from Calendar C, GROUPOFKIDS G " + 
			"where C.GroupOFKIDS_GroupID = G.GroupID";
	
    List <Schedule> schedule = jdbcTemplateObject.query(SQL,new CalendarMapper());
    
   
    return schedule;
	
}

@Override
public String addSchedule(Schedule schedule) {
	String SQL = "INSERT INTO CALENDAR (GROUPOFKIDS_GROUPID, TIME, DATE) VALUES (?,?,?)";
	logger.info("inserting DATE as : " + schedule.getDate());
	
	int resultOfQuery = jdbcTemplateObject.update( SQL, schedule.getGroupID(), schedule.getTime(), schedule.getDate() );
	
	logger.info("result of query after insert into calendar = "+ resultOfQuery);
	
	if (resultOfQuery!=0) {
		
		String SQL2 = "INSERT into Attendance (DateOfAttendance, GROUPOFKIDS_GroupID, " + 
				" KID_KidID, PresentAbsent) SELECT  ?, ?, KIDID , 'A' " + 
				" FROM KID where KID.GROUPOFKIDS_GroupID = ? ";
		
		int resultOfQuery2 = jdbcTemplateObject.update(SQL2, schedule.getDate(), schedule.getGroupID(), schedule.getGroupID());
		
		logger.info("result of query after insert into attendance = "+ resultOfQuery2);
		
		if (resultOfQuery!=0)
		return "SUCCESS";
		else return "Problem in updating attendance";
		}
		else return "CANT UPDATE SCHEDULE";
}
	

public String updateSchedule(Schedule schedule) {
	String SQL = "UPDATE CALENDAR SET TIME=?, Date=?"
			+ " WHERE CalendarID=?";
	logger.info("inserting DATE as : " + schedule.getDate());
	
	int resultOfQuery = jdbcTemplateObject.update( SQL, schedule.getTime(), 
														schedule.getDate(),
														schedule.getCalendarID());
	
	logger.info("result of query after insert into calendar = "+ resultOfQuery);
	
	if (resultOfQuery!=0) {
		return "SUCCESS";
		}
		else return "CANT UPDATE SCHEDULE";
}


	
}
