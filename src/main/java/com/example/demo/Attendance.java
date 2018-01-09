package com.example.demo;

import java.sql.Date;

public class Attendance {
	
	private Date dateOfAttendance;
	private String groupOfKids_GroupID;
	private String kid_KidID;
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
	public Date getDateOfAttendance() {
		return dateOfAttendance;
	}
	public void setDateOfAttendance(Date dateOfAttendance) {
		this.dateOfAttendance = dateOfAttendance;
	}
	public String getGroupOfKids_GroupID() {
		return groupOfKids_GroupID;
	}
	public void setGroupOfKids_GroupID(String groupOfKids_GroupID) {
		this.groupOfKids_GroupID = groupOfKids_GroupID;
	}
	public String getKid_KidID() {
		return kid_KidID;
	}
	public void setKid_KidID(String kid_KidID) {
		this.kid_KidID = kid_KidID;
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
