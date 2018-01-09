package com.example.demo;

import java.security.Principal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@SpringBootApplication
@RestController
@Controller
public class CoachAppApplication {
	private static final Logger logger = Logger.getLogger(CoachAppApplication.class);
	
	
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	  public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    logger.info("Inside configure http security");
	    	http
	      .csrf().csrfTokenRepository(csrfTokenRepository())
	      
	      .and()
	      .logout()
	      	.logoutSuccessUrl("/#/login")
	      	.invalidateHttpSession(true)
	      
	      .and()
	        .httpBasic()
	        
	      .and()
	      
	        .authorizeRequests()
	          .antMatchers( "/index.html", "/home.html", "/login.html", "/logout", "/")
	        		.permitAll()
	        		.antMatchers("/calendar.html").hasRole("USER")
	        		.antMatchers("/manageClasses.html").hasRole("ADMIN")
	          .anyRequest().authenticated().and()
	          
	          .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
	          
	          ;
	    }
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    	System.out.println("Inside configure- authentication manager builder");
	    		auth
	    			.inMemoryAuthentication()
	    				.withUser("user1").password("password").roles("USER").and()
	    				.withUser("admin").password("password").roles("USER", "ADMIN");
	    		
	    }
	  }
	
		private  CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
		}
		
		@RequestMapping("/resourceLogin")
		 
		 public Principal user(Principal user) {
			   logger.info("First use of logger! user = "+ user.getName());
    
			   return user;
		  }	
		
	
	
	@RequestMapping("/resource")
	public Map<String,Object> home() {
	    Map<String,Object> model = new HashMap<String,Object>();
	    model.put("id", UUID.randomUUID().toString());
	    model.put("content", "Hello World");
	    logger.debug("Got the request mapped!");
	    logger.info("Got the request mapped! INFO");
	    return model;
	  }
	
	@RequestMapping("/getCalendar")
	public @ResponseBody Map<String,Object> getCalendar(@RequestBody Schedule data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	    
	    
	    
	    Date date = data.getDate();
	   logger.info("First use of logger! date = "+ date);
		
		//System.out.println("Date is "+ date);
	    //ToDO 
	    // get the date and do a search in db based on date
	    FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    CalendarJDBCTemplate  calendarJDBCTemplate = 
		         context.getBean(CalendarJDBCTemplate.class);
		
	   List<Schedule> schedule = calendarJDBCTemplate.getSchedule(date);
	    
	    model.put("Schedule", schedule);
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/checkAttendance")
	public @ResponseBody Map<String,Object> checkAttendance(@RequestBody Attendance data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	    logger.info("check attendance landed");
		
	    Date date = data.getDateOfAttendance();
	   logger.info("First use of logger! date = "+ date);
		
	    //ToDO 
	    // get the date and do a search in db based on date
	    FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    AttendanceJDBCTemplate  attendanceJDBCTemplate = 
		         context.getBean(AttendanceJDBCTemplate.class);
		
	   List<Attendance> attendance = attendanceJDBCTemplate.checkAttendance(data);
	    
	    model.put("attendanceList", attendance);
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/getCalendarAll")
	public @ResponseBody Map<String,Object> getCalendarAll(@RequestBody Schedule data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	    
	    
	    
	    Date date = data.getDate();
	   logger.info("First use of logger! date = "+ date);
		
		//System.out.println("Date is "+ date);
	    //ToDO 
	    // get the date and do a search in db based on date
	    FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    CalendarJDBCTemplate  calendarJDBCTemplate = 
		         context.getBean(CalendarJDBCTemplate.class);
		
	   List<Schedule> schedule = calendarJDBCTemplate.getSchedule();
	    
	    model.put("Schedule", schedule);
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/getKidsInGroup")
	public @ResponseBody Map<String,Object> getKidsInGroup(@RequestBody GroupOfKids data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	    
	    //Date date = data.getDate();
	    String groupID = data.getGroupID();
	   logger.info("First use of logger! groupID = "+ groupID);
		
		
	    //ToDO 
	    // get the groupName from data and get Names of kids in that group 
	    FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    KidJDBCTemplate  kidJDBCTemplate = 
		         context.getBean(KidJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<Kid> kid = kidJDBCTemplate.getKids(groupID);
	    
	    model.put("kidsList", kid);
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/markAttendance")
	public @ResponseBody Map<String,Object> markAttendance(@RequestBody MarkAttendance data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("Landed mark attendance  ");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    AttendanceJDBCTemplate  attendanceJDBCTemplate = 
		         context.getBean(AttendanceJDBCTemplate.class);
	    
	    //TODO : check if attendance is already marked for the date. 
	    // If marked, show marked attendance else go to mark attendance
	    
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		String result = attendanceJDBCTemplate.markAttendance( data);
	    
	    model.put("result", result);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	
	@RequestMapping("/addGroup")
	public @ResponseBody Map<String,Object> addGroup(@RequestBody GroupOfKids data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("GroupName to be added =  " + data.getGroupName());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    GroupJDBCTemplate  groupJDBCTemplate = 
		         context.getBean(GroupJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<GroupOfKids> groups = groupJDBCTemplate.addGroup( data);
	    
	    model.put("groupList", groups);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/getGroups")
	public @ResponseBody Map<String,Object> getGroups(@RequestBody GroupOfKids data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("GroupNames to be obtained");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    GroupJDBCTemplate  groupJDBCTemplate = 
		         context.getBean(GroupJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<GroupOfKids> groups = groupJDBCTemplate.getGroups( data);
	    
	    model.put("groupList", groups);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/getPackages")
	public @ResponseBody Map<String,Object> getPackages(@RequestBody PackageDetails data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("Package List to be obtained");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    PackageDetailsJDBCTemplate  packageDetailsJDBCTemplate = 
		         context.getBean(PackageDetailsJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<PackageDetails> packages = packageDetailsJDBCTemplate.getPackages( data);
	    
	    model.put("packageList", packages);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/getKidInfo")
	public @ResponseBody Map<String,Object> getKidInfo(@RequestBody Kid data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("Kid List to be obtained");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    KidJDBCTemplate  kidJDBCTemplate = 
		         context.getBean(KidJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<Kid> kids = kidJDBCTemplate.getKids( );
	    
	    model.put("kidList", kids);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/addKid")
	public @ResponseBody Map<String,Object> addKid(@RequestBody Kid data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("KidName to be added =  " + data.getKidName());
	  logger.info("Group to be added to : " + data.getGroupID());
	  logger.info("Package chosen : " + data.getPackageID());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    KidJDBCTemplate  kidJDBCTemplate = 
		         context.getBean(KidJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<Kid> kids = kidJDBCTemplate.addKid( data);
	    
	    model.put("kidList", kids);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/addSchedule")
	public @ResponseBody Map<String,Object> addSchedule(@RequestBody Schedule data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("date to be added =  " + data.getDate());
	  logger.info("Group to be added  : " + data.getGroupID());
	  logger.info("Time to be entered : " + data.getTime());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    CalendarJDBCTemplate  calendarJDBCTemplate = 
		         context.getBean(CalendarJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		String result = calendarJDBCTemplate.addSchedule( data);
	    
	    model.put("result", result);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/updateKid")
	public @ResponseBody Map<String,Object> updateKid(@RequestBody Kid data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("kidname to be added =  " + data.getKidName());
	  logger.info("Group to be added  : " + data.getGroupID());
	  logger.info("kidID to be entered : " + data.getKidID());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    KidJDBCTemplate  kidJDBCTemplate = 
		         context.getBean(KidJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		String result = kidJDBCTemplate.updateKid(data);
		
		logger.info("returned result from update = " + result);
	    
	    model.put("result", result);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/deleteKid")
	public @ResponseBody Map<String,Object> deleteKid(@RequestBody Kid data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("kidname to be deleted =  " + data.getKidName());
	  logger.info("kidID to be deleted : " + data.getKidID());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    KidJDBCTemplate  kidJDBCTemplate = 
		         context.getBean(KidJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		String result = kidJDBCTemplate.deleteKid(data);
		
		logger.info("returned result from update = " + result);
	    
	    model.put("result", result);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/updateGroup")
	public @ResponseBody Map<String,Object> updateGroup(@RequestBody GroupOfKids data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("groupName to be updated =  " + data.getGroupName());
	  logger.info("GroupID to be added  : " + data.getGroupID());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    GroupJDBCTemplate  groupJDBCTemplate = 
		         context.getBean(GroupJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		String result = groupJDBCTemplate.updateGroup(data);
		
		logger.info("returned result from update = " + result);
	    
	    model.put("result", result);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/updateSchedule")
	public @ResponseBody Map<String,Object> updateSchedule(@RequestBody Schedule data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("groupName to be updated =  " + data.getGroupName());
	  logger.info("CalendarID to be updated  : " + data.getCalendarID());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    CalendarJDBCTemplate  calendarJDBCTemplate = 
		         context.getBean(CalendarJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		String result = calendarJDBCTemplate.updateSchedule(data);
		
		logger.info("returned result from update = " + result);
	    
	    model.put("result", result);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	public static void main(String[] args) {
		SpringApplication.run(CoachAppApplication.class, args);
	}
}
