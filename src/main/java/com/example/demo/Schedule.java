package com.example.demo;

import java.sql.Date;

public class Schedule {

	private String time;
	private Date date;
	private String groupName;
	private String groupID;
	private String calendarID;
	private String kidID;
	private String coachID;
	
	public String getCoachID() {
		return coachID;
	}
	public void setCoachID(String coachID) {
		this.coachID = coachID;
	}
	public String getCalendarID() {
		return calendarID;
	}
	public void setCalendarID(String calendarID) {
		this.calendarID = calendarID;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String string) {
		this.groupID = string;
	}
	public String getKidID() {
		return kidID;
	}
	public void setKidID(String kidID) {
		this.kidID = kidID;
	}
	
}
