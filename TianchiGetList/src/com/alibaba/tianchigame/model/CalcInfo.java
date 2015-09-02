package com.alibaba.tianchigame.model;

public class CalcInfo {
	private int upsum;
	private int lowsum;
	private int upnum;
	private int lownum;
	private int newin;
	public int getUpnum() {
		return upnum;
	}
	public void setUpnum(int upnum) {
		this.upnum = upnum;
	}
	public int getLownum() {
		return lownum;
	}
	public void setLownum(int lownum) {
		this.lownum = lownum;
	}
	public double getAvgup() {
		return (double)upsum/(double)upnum;
	}
	public double getAvglow() {
		return (double)lowsum/(double)lownum;
	}
	public int getNewin() {
		return newin;
	}
	public void setNewin(int newin) {
		this.newin = newin;
	}
	public int getUpsum() {
		return upsum;
	}
	public void setUpsum(int upsum) {
		this.upsum = upsum;
	}
	public int getLowsum() {
		return lowsum;
	}
	public void setLowsum(int lowsum) {
		this.lowsum = lowsum;
	}
	
}
