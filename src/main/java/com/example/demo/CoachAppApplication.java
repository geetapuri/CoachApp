package com.example.demo;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;


@SpringBootApplication()
@RestController
@Controller
public class CoachAppApplication {
	private static final Logger logger = Logger.getLogger(CoachAppApplication.class);
	
	
	@Configuration
	
	
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	  public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**");
	    }
	    
	    
		
		@Override
	    protected void configure(HttpSecurity http) throws Exception {
	    logger.info("Inside configure http security");
	    	http
	    	
	    		.cors()
	    		.and()
		          
		          .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
	    		
	      .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	      .and()
	      .csrf().disable()
	      
	      .logout()
	      	.logoutSuccessUrl("/#/login")
	      	.invalidateHttpSession(true)
	      
	      .and()
	        .httpBasic()
	        
	      .and()
	      
	        .authorizeRequests()
	        
	          .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	        		.antMatchers("/calendar.html").hasRole("USER")
	        		.antMatchers("/manageClasses.html").hasRole("ADMIN")
	          .anyRequest().permitAll().and()
	          .formLogin().loginPage("/login")
	          .usernameParameter("username").passwordParameter("password")
	        .and()
	          	
	          .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
	          
	          
	          
	          ;
	    }
		
		
		
		
		
		@Component
		public class CorsFilter extends OncePerRequestFilter {
			
		    @Override
		    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		    	logger.info("Cors Filter Once Per Request");   
		    	response.setHeader("Access-Control-Allow-Origin", "*");
		        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		        response.setHeader("Access-Control-Max-Age", "3600");
		        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
		        response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
		        if ("OPTIONS".equals(request.getMethod())) {
		            response.setStatus(HttpServletResponse.SC_OK);
		            logger.info("OPTIONS ");
		        } else { 
		            filterChain.doFilter(request, response);
		            logger.info("Else");
		        }
		    }
		}
	
		
		@Autowired
		DataSource dataSource;
		
	    @Autowired
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    	System.out.println("Inside configure- authentication manager  builder");
	    	
	      		
		//instead of in memory Auth, using jdbc Auth now
	    		logger.info("inside configure");
	    		auth  
	    		/*.inMemoryAuthentication()
				.withUser("user1").password("password").roles("USER").and()
				.withUser("geeta").password("password").roles("ADMIN").and()
				.withUser("admin").password("password").roles("USER", "ADMIN");*/
	    			.jdbcAuthentication().dataSource( dataSource)
	    				.usersByUsernameQuery(
	    						"select username, password, enabled from users where username=?"
	    						)
	    				.authoritiesByUsernameQuery(
	    						"select username, role from user_roles where username=?"
	    						);
	    				//.passwordEncoder(new BCryptPasswordEncoder());
	    				
	    	
	    		
	    }
	  }
	
		private  CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
		}
		
		
		
		@CrossOrigin(origins= "*")
		@RequestMapping("/resourceLogin")
		public @ResponseBody Map<String,Object> user( Principal user) {
			Map<String,Object> model = new HashMap<String,Object>();
			   logger.info("First use of logger! user = "+ user.getName());
			   model.put("user", user);
    
			   return model;
		  }	
		
	
	@CrossOrigin(origins="*")
	@RequestMapping("/resource")
	
	public Map<String,Object> home() {
	    Map<String,Object> model = new HashMap<String,Object>();
	    model.put("id", UUID.randomUUID().toString());
	    model.put("content", "Hello World");
	    logger.debug("Got the request mapped!");
	    logger.info("Got the request mapped! INFO");
	    return model;
	  }
	
	@GetMapping("/")
	  public String hello() {
	    return "hello world!";
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
		
	    Date date = data.getDate();
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
	
	@RequestMapping("/getCalendarAllKid")
	public @ResponseBody Map<String,Object> getCalendarAllParent(@RequestBody Schedule data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	    
	    
	    
	    //Date date = data.getDate();
	   //logger.info("First use of logger! date = "+ date);
		
		//System.out.println("Date is "+ date);
	    //ToDO 
	    // get the date and do a search in db based on date
	    FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    CalendarJDBCTemplate  calendarJDBCTemplate = 
		         context.getBean(CalendarJDBCTemplate.class);
		
	   List<Schedule> schedule = calendarJDBCTemplate.getSchedule(data);
	    
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
	
	@CrossOrigin(origins="*")
	@RequestMapping("/getKids")
	public @ResponseBody Map<String,Object> getKids() {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	    
	    
	   logger.info("First use of logger! in getKids");
		
		
	    //ToDO 
	    // get the groupName from data and get Names of kids in that group 
	    FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    KidJDBCTemplate  kidJDBCTemplate = 
		         context.getBean(KidJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<Kid> kid = kidJDBCTemplate.getKidsList();
	    
	    model.put("kidList", kid);
	    
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
	
	@RequestMapping("/viewAttendanceKid")
	public @ResponseBody Map<String,Object> viewAttendance(@RequestBody Attendance data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("Landed view attendance  ");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    AttendanceJDBCTemplate  attendanceJDBCTemplate = 
		         context.getBean(AttendanceJDBCTemplate.class);
	    
	    //TODO : check if attendance is already marked for the date. 
	    // If marked, show marked attendance else go to mark attendance
	    
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
	    List<Attendance> attendance = attendanceJDBCTemplate.viewAttendanceKid( data);
	    
	    model.put("attendance", attendance);
	    //model.put("content", "Hello World");
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/viewFees")
	public @ResponseBody Map<String,Object> viewFees(@RequestBody FeeMgmt data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("Landed view fees  ");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    FeeMgmtJDBCTemplate  feeMgmtJDBCTemplate = 
		         context.getBean(FeeMgmtJDBCTemplate.class);
	    
	    //TODO : check if attendance is already marked for the date. 
	    // If marked, show marked attendance else go to mark attendance
	    
	    logger.info("querying for kid id = " + data.getKidID());
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
	    List<FeeMgmt> feeList = feeMgmtJDBCTemplate.viewFees( data);
	    
	    model.put("feeList", feeList);
	    
	    context.close();
	    return model;
	    
	  }
	
	@RequestMapping("/payFees")
	public @ResponseBody Map<String,Object> payFees(@RequestBody FeeMgmt data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("Landed pay fees  ");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    FeeMgmtJDBCTemplate  feeMgmtJDBCTemplate = 
		         context.getBean(FeeMgmtJDBCTemplate.class);
	    
	   
	    logger.info("updating for kid id = " + data.getKidID());
	    logger.info("updating for date = " + data.getDateOfAttendance());
		
	    
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
	    String result = feeMgmtJDBCTemplate.addFees( data);
	    
	    model.put("result", result);
	    
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
	
	@RequestMapping("/getParentID")
	public @ResponseBody Map<String,Object> getParentID(@RequestBody Parent data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	  
	  logger.info("ParentID  to be searched for parent =  " + data.getParentName());
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    ParentJDBCTemplate  parentJDBCTemplate = 
		         context.getBean(ParentJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<Parent> parent = parentJDBCTemplate.getParentID( data);
	    
	    model.put("parent", parent);
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

	@RequestMapping("/getKidInfoParent")
	public @ResponseBody Map<String,Object> getKidInfoParent(@RequestBody Kid data) {
		//String name;
	    Map<String,Object> model = new HashMap<String,Object>();
	    String parentID = data.getParentID();
	  
	  logger.info("Kid List to be obtained");
	  
	  FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
	
	    KidJDBCTemplate  kidJDBCTemplate = 
		         context.getBean(KidJDBCTemplate.class);
		
	    //List<Kids> kids = kidsJDBCTemplate.listAllKids();
		List<Kid> kids = kidJDBCTemplate.getKidsParent(parentID );
	    
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
