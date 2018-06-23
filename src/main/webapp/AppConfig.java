package com.example.demo;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {
	
	public @interface java {

	}

	@Bean(name = "dataSourceLogin")
	 public DriverManagerDataSource dataSource() {
	     DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	     driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
	     driverManagerDataSource.setUrl("jdbc:mysql://google/coachKidsDB?cloudSqlInstance=coachingapp-203705:asia-southeast1:coachkids&amp;socketFactory=com.google.cloud.sql.mysql.SocketFactory&amp;user=root&amp;password=1234&amp;useSSL=false");
	     driverManagerDataSource.setUsername("root");
	     driverManagerDataSource.setPassword("1234");
	     return driverManagerDataSource;
	 }

}
