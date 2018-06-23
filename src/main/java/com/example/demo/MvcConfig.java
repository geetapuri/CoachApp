package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
	}

	@Bean(name = "dataSourceLogin")
	 public DriverManagerDataSource datasource() {
	     DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	     driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
	     driverManagerDataSource.setUrl("jdbc:mysql://google/coachKidsDB?cloudSqlInstance=coachingapp-203705:asia-southeast1:coachkids&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=root&password=1234&useSSL=false");
	    /* driverManagerDataSource.setUsername("root");
	     driverManagerDataSource.setPassword("1234");*/
	     return driverManagerDataSource;
	 }

}
