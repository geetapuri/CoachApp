package com.example.demo;

import java.sql.Date;
import java.util.List;

public class MarkAttendance {
	
	private List<Kid> kidsList;
	private Date date;
	private String groupID;
	
	public List<Kid> getKidsList() {
		return kidsList;
	}
	public void setKidID(List<Kid> kidsList) {
		this.kidsList = kidsList;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	

}
