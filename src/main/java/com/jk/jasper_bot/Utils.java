package com.jk.jasper_bot;

import java.time.DayOfWeek;

public class Utils {

	public String getFirstWord(String text, char delim) {
		int index = text.indexOf(delim);
		if (index > -1) {
			return text.substring(0, index).trim();
		} else {
			return text;
		}
	}
	
	public boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

	public String isSupportedBossName(String bossName) {
		for (BossEnum boss: BossEnum.values()) {
			if (boss.name().equalsIgnoreCase(bossName)) {
				return boss.name();
			}
		}
		
		return null;
	}

	public static DayOfWeek getPreviousDay(DayOfWeek dayOfWeek) {
		DayOfWeek previousDay = null;
		switch (dayOfWeek) {
			case SUNDAY:
				previousDay = DayOfWeek.SATURDAY;
				break;
			case MONDAY:
				previousDay = DayOfWeek.SUNDAY;
				break;
			case TUESDAY:
				previousDay = DayOfWeek.MONDAY;
				break;
			case WEDNESDAY:
				previousDay = DayOfWeek.TUESDAY;
				break;
			case THURSDAY:
				previousDay = DayOfWeek.WEDNESDAY;
				break;
			case FRIDAY:
				previousDay = DayOfWeek.THURSDAY;
				break;
			case SATURDAY:
				previousDay = DayOfWeek.FRIDAY;
				break;
			default:
				break;
		}
		
		return previousDay;
	}
}
