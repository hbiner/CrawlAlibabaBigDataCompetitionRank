package com.alibaba.tianchigame.model;

public class Neighbor {
	private String neig_ID;
	private double semblance;
	private double power;
	private int hit;
	private int user_count;
	public String getNeig_ID() {
		return neig_ID;
	}
	public void setNeig_ID(String neig_ID) {
		this.neig_ID = neig_ID;
	}
	public double getSemblance() {
		return semblance;
	}
	public void setSemblance(double semblance) {
		this.semblance = semblance;
		double n=semblance*10;
		power=Math.pow(2, n);
		power=((int)(power*100))/1000.0;
		if(user_count/hit<2&&power<0.9){
			power=0.9;
		}
		if(power>1){
			power=0.9;
		}
	}
	public void setPower(double power) {
		this.power = power;
	}
	public double getPower() {
		return power;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public int getUser_count() {
		return user_count;
	}
	public void setUser_count(int user_count) {
		this.user_count = user_count;
	} 
	
	
}
