package com.jk.jasper_bot;

import java.time.DayOfWeek;

public class FixedEvent {

	int hour;
	int min;
	DayOfWeek dayOfWeek;
	
	/**
	 * @param min
	 * @param hour
	 * @param dayOfWeek
	 */
	public FixedEvent(int min, int hour, DayOfWeek dayOfWeek) {
		this.hour = hour;
		this.min = min;
		this.dayOfWeek = dayOfWeek;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public String toString() {
		return "FixedEvent [hour=" + hour + ", min=" + min + ", dayOfWeek=" + dayOfWeek + "]";
	}
	
	
}
