package com.alibaba.tianchigame.model;

public class School implements Comparable<School>{
	
	private String schoolname;
	private int teamsum;
	public String getSchoolname() {
		return schoolname;
	}
	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}
	public int getTeamsum() {
		return teamsum;
	}
	public void setTeamsum(int teamsum) {
		this.teamsum = teamsum;
	}
	@Override
	public int compareTo(School o) {
		if (this.getTeamsum()>o.getTeamsum()) {
			return -1;
		}else if (this.getTeamsum()<o.getTeamsum()) {
			return 1;
		}
		return 0;
	}
}
