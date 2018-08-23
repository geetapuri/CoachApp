package com.example.demo;

import java.sql.Date;
import java.time.LocalDate;

public class Attendance {
	
	private LocalDate date;
	private String groupID;
	private String kidID;
	private String presentAbsent;
	private String attendanceID;
	private String kidName;
	private String groupName;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getKidName() {
		return kidName;
	}
	public void setKidName(String kidName) {
		this.kidName = kidName;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getKidID() {
		return kidID;
	}
	public void setKidID(String kidID) {
		this.kidID = kidID;
	}
	public String getPresentAbsent() {
		return presentAbsent;
	}
	public void setPresentAbsent(String presentAbsent) {
		this.presentAbsent = presentAbsent;
	}
	public String getAttendanceID() {
		return attendanceID;
	}
	public void setAttendanceID(String attendanceID) {
		this.attendanceID = attendanceID;
	}
	
	

}
