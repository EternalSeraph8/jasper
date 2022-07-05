package com.jk.jasper_bot;

import java.util.ArrayList;
import java.util.List;

public class BossTimer {
	
	public BossTimer() {	}
	
	List<Long> timerVal = new ArrayList<Long>();
	List<String> bossName = new ArrayList<String>();
	
	public List<Long> getTimerVal() {
		return timerVal;
	}

	public void setTimerVal(List<Long> timerVal) {
		this.timerVal = timerVal;
	}

	public List<String> getBossName() {
		return bossName;
	}

	public void setBossName(List<String> bossName) {
		this.bossName = bossName;
	}

	@Override
	public String toString() {
		return "BossTimer [timerVal=" + timerVal + ", bossName=" + bossName + "]";
	}

	
}
