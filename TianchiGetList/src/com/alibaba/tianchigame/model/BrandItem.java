package com.alibaba.tianchigame.model;

public class BrandItem implements Comparable<BrandItem>{
	private String brandID;
	private int rank;
	public String getBrandID() {
		return brandID;
	}
	public void setBrandID(String brandID) {
		this.brandID = brandID;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	@Override
	public int compareTo(BrandItem o) {
		if (o.getRank()>this.getRank()) {
			return 1;
		}
		if (o.getRank()<this.getRank()) {
			return -1;
		}
		return 0;
	}
}
