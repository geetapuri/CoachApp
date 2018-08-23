package com.example.demo;

import java.sql.Date;
//import java.time.Instant;
import java.time.LocalDate;
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
	public List<Schedule> getSchedule(LocalDate date) {
		// TODO Auto-generated method stub - query for finding the schedule based on date
		logger.info("Calling getSchedule()  ");
		
		logger.info("date to be queried = " + date);
	
		String SQL = "select CALENDAR.Time AS TIME, CALENDAR.Date As Date, CALENDAR.CalendarID As CalendarID,  "
				+ "GROUPOFKIDS.GroupName AS GroupName, GROUPOFKIDS.GroupID AS GroupID "
				+ "from CALENDAR, GROUPOFKIDS "
				+ "where CALENDAR.GroupOfKids_GroupID = GROUPOFKIDS.GroupID "
				+ "AND CALENDAR.Date like ?";
		
	    List <Schedule> schedule = jdbcTemplateObject.query(SQL, new Object[] {date},new CalendarMapper());
	    
	   
	    return schedule;
		
	}
	
	@Override
	public List<Schedule> getSchedule(Coach coach) {
		// TODO Auto-generated method stub - query for finding the schedule based on date
		logger.info("Calling getSchedule()  ");
		logger.info("calling schedule for coachID = " + coach.getCoachID());
		String coachID  = coach.getCoachID();
	
	
		
		String SQL = "select 'AllKids' as KidID, C.CalendarID, C.Date, C.Time, " + 
				"		G.GroupID, G.GroupName, G.CoachID " + 
				"from CALENDAR C, GROUPOFKIDS G " + 
				"where C.GroupOFKIDS_GroupID = G.GroupID " +
				" AND G.CoachID =?" +
				" ORDER BY G.GroupID ASC, C.Date DESC";
		
	    List <Schedule> schedule = jdbcTemplateObject.query(SQL,new Object[] {coachID},new CalendarMapper());
	    
	   
	    return schedule;
		
	}
	
	@Override
	public List<Schedule> getSchedule(Schedule schedule) {
		// TODO Auto-generated method stub - query for finding the schedule based on date
		logger.info("Calling getSchedule()  ");
		
		String kidID = schedule.getKidID();
		
		String SQL = "select C.CalendarID, C.Date, C.Time, " + 
				"		G.GroupID, G.GroupName, G.CoachID, K.KidID	" + 
				"from CALENDAR C, GROUPOFKIDS G , KID K " + 
				"where C.GroupOFKIDS_GroupID = G.GroupID " + 
				" And K.GroupOfKids_GroupID = G.GroupID " + 
				"	And K.KidID = ? " +
				" ORDER BY C.Date DESC";
		
	    List <Schedule> returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {kidID}, new CalendarMapper());
	    
	   
	    return returnSchedule;
		
	}
	
	@Override
	public String addSchedule(Schedule schedule) {
		String SQL = "INSERT INTO CALENDAR (GROUPOFKIDS_GROUPID, TIME, DATE) VALUES (?,?,?)";
		//logger.info("inserting DATE as : " + schedule.getDate());
		
		int resultOfQuery = jdbcTemplateObject.update( SQL, schedule.getGroupID(), schedule.getTime(), java.sql.Date.valueOf(schedule.getDate()) );
		LocalDate dateToInsert = schedule.getDate();
		
		//To Add entries for scheduled class for a year
		for (int i=0; i<52; i++) {
			
			dateToInsert = dateToInsert.plusDays(7);
			int result = jdbcTemplateObject.update(SQL, schedule.getGroupID(), schedule.getTime(), java.sql.Date.valueOf(dateToInsert));
		}
		
			
		//logger.info("result of query after insert into calendar = "+ resultOfQuery);
		
		if (resultOfQuery!=0) {
			
			//String SQL2 = "INSERT into ATTENDANCE (DateOfAttendance, GROUPOFKIDS_GroupID, " + 
			//		" KID_KidID, PresentAbsent) SELECT  ?, ?, KIDID , 'A' " + 
			//		" FROM KID_GROUP where KID_GROUP.GroupID = ? ";
			
			//int resultOfQuery2 = jdbcTemplateObject.update(SQL2, schedule.getDate(), schedule.getGroupID(), schedule.getGroupID());
			
			//logger.info("result of query after insert into attendance = "+ resultOfQuery2);
			
			//if (resultOfQuery2!=0)
			return "SUCCESS";
			//else return "Problem in updating attendance";
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
			//TODO: delete from the attendance table and insert new rows based on updated date
			
			String SQL2 = "DELETE FROM ATTENDANCE WHERE GROUPOFKIDS_GroupID IN"
					+ "  (SELECT groupofkids_groupID FROM CALENDAR WHERE CalendarID=?)  ";
			
			int resultOfQuery2 = jdbcTemplateObject.update(SQL2, schedule.getCalendarID());
			
			if (resultOfQuery2!=0) {
			
				
				String SQL3 = "INSERT INTO ATTENDANCE (DateOfAttendance, GROUPOFKIDS_GroupID, KID_KidID, PresentAbsent) "
						+ " SELECT ? , ? , KIDID , 'A' " 
						+ " FROM KID where KID.GROUPOFKIDS_GroupID = ? " ;
				
				int resultOfQuery3 = jdbcTemplateObject.update(SQL3, schedule.getDate(), schedule.getGroupID(), schedule.getGroupID() );
				
				if (resultOfQuery3!=0) {
					return "SUCCESS";
				}
				else return "CANT UPDATE ATTENDANCE according to the SCHEDULE";
			}
			else return "CANT UPDATE ATTENDANCE according to the SCHEDULE";
			}
			else return "CANT UPDATE SCHEDULE";
	}
	
	public List<Schedule> getScheduleKidDate(Schedule schedule) {
		
	logger.info("Calling getScheduleKidDate()  ");
		
		String kidID = schedule.getKidID();
		//Date date = schedule.getDate();
		
		String SQL = "select C.CalendarID, C.Date, C.Time, " + 
				" G.GroupName, G.CoachID, K.KidName, KG.KidID, KG.GroupID 	" + 
				" from CALENDAR C, GROUPOFKIDS G , KID K, KID_GROUP KG " + 
				" where C.GROUPOFKIDS_GroupID = G.GroupID " + 
				" And KG.GroupID = G.GroupID " +
				" And K.KidID = KG.KidID " + 
				" And K.KidID = ? And C.Date = ?" +
				" ORDER BY C.Date DESC";
		
	    List <Schedule> returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {kidID, schedule.getStrDate()}, new CalendarMapper());
	    
	   
	    return returnSchedule;
		
	}
	
	public List<Schedule> getScheduleCoachDate(Schedule schedule) {
	logger.info("Calling getScheduleKidDate()  ");
		//TODO if groupID has come, sql based on that, else this one
		String coachID = schedule.getCoachID();
		String SQL;
		List <Schedule> returnSchedule;
		//Date date = schedule.getDate();
		if (schedule.getGroupID()!= null ) { 
			 SQL = "select 'AllKids' as KidID, C.CalendarID, C.Date, C.Time, " + 
					"G.GroupID, G.GroupName, G.CoachID	" + 
					"from CALENDAR C, GROUPOFKIDS G  " + 
					"where C.GROUPOFKIDS_GroupID = G.GroupID " + 
					"And G.CoachID = ? And C.Date = ? " +
					" And G.GroupID = ? " +
					" ORDER BY C.Date DESC";
			  returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {coachID, schedule.getStrDate(), schedule.getGroupID()}, new CalendarMapper());
			    
			
		} else {
		
		 SQL = "select 'AllKids' as KidID, C.CalendarID, C.Date, C.Time, " + 
				"G.GroupID, G.GroupName, G.CoachID	" + 
				"from CALENDAR C, GROUPOFKIDS G  " + 
				"where C.GROUPOFKIDS_GroupID = G.GroupID " + 
				"And G.CoachID = ? And C.Date = ?" +
				" ORDER BY C.Date DESC";
		  returnSchedule = jdbcTemplateObject.query(SQL,new Object[] {coachID, schedule.getStrDate()}, new CalendarMapper());
		    
		}
	    
	   
	    return returnSchedule;
		
	}

	public List<Schedule> getScheduleCoachGroup(Schedule data) {
		logger.info("Calling getScheduleKidDate()  ");
		
		String groupID = data.getGroupID();
		//Date date = schedule.getDate();
		
		String sql = "select  'AllKids' as KidID, C.CalendarID, C.Date, C.Time, " + 
				"G.GroupID, G.GroupName, G.CoachID	" + 
				"from CALENDAR C, GROUPOFKIDS G  " + 
				"where C.GROUPOFKIDS_GroupID = G.GroupID " + 
				"And G.GroupID=? And G.CoachID=? " +
				" ORDER BY C.Date DESC";
		
	    List <Schedule> returnSchedule = jdbcTemplateObject.query(sql,new Object[] {groupID, data.getCoachID()}, new CalendarMapper());
	    
	   
	    return returnSchedule;
	}

	public List<Schedule> getScheduleParentDate(Schedule data) {
		
		String sql =  "SELECT P.ParentID, K.KidName, C.Date, C.Time, KG.GroupID, G.GroupName "
				+ "	FROM PARENT P, KID K, CALENDAR C, KID_GROUP KG, GROUPOFKIDS G "
				+ " WHERE P.ParentID = K.ParentID " 
				+ "	AND KG.GroupID = G.GroupID "
				+ " AND C.GROUPOFKIDS_GroupID = G.GroupID "
				+ " AND P.ParentID=? AND C.Date=?";
		
		List <Schedule> schedule = jdbcTemplateObject.query(sql, new Object[] {data.getParentID(), data.getDate()}, new CalendarMapperParent() );
		return schedule;
	}
}


	

