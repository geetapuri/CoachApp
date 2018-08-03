package com.example.demo;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.logging.Level;
//import org.apache.log4j.Logger;
import java.util.logging.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Component


public class ScheduledTasks {

	private static final Logger logger = Logger.getLogger(CoachAppApplication.class.getName());
	
    
    //private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    //private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron="0 * * * * * ")
    public void updateInvoiceDue () {
    	
    		//logger.info("doing something");
    		FileSystemXmlApplicationContext context = 
				new FileSystemXmlApplicationContext("BeanForCoach.xml");
    		
    		FeeMgmtJDBCTemplate  feeMgmtJDBCTemplate = 
   		         context.getBean(FeeMgmtJDBCTemplate.class);
    		
    		
    		feeMgmtJDBCTemplate.updateInvoice();
    		context.close();
    	
       // log.info("The time is now {}", dateFormat.format(new Date()));
    }
}