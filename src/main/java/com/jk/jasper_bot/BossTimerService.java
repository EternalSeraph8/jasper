package com.jk.jasper_bot;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BossTimerService {
	
	
	Logger log = LoggerFactory.getLogger(BossTimerService.class);
	
    String bossName = "";


	public String getBossName() {
		return bossName;
	}

	public void setBossName(String bossName) {
		this.bossName = bossName;
	}

	/**
	 * Returns a boss name if it is currently time for that boss.  null otherwise.
	 * @param map 
	 * @return
	 */
	public List<String> isItBossTime() {
		List<String> timerQueueList = new ArrayList<String>();
		LocalDateTime now = LocalDateTime.now();
		
		if (now.getDayOfWeek() == DayOfWeek.WEDNESDAY) {
			if (now.getHour() == 18 && now.getMinute() == 0) {
				timerQueueList.add("@everyone Vell in 1 hour!");
			}
		}
		else if (now.getDayOfWeek() == DayOfWeek.SUNDAY) {
			if (now.getHour() == 15 && now.getMinute() == 0) {
				timerQueueList.add("@everyone Vell in 1 hour!");
			}
		}
		
		return timerQueueList;
	}
	
	public List<String> isItSailiesTime() {
		List<String> timerQueueList = new ArrayList<String>();
		LocalDateTime now = LocalDateTime.now();
		
		if (now.getHour() == 18 && now.getMinute() == 0) {
			timerQueueList.add("It's reset, time for Sailies!  Please react if you want to run them!");
		}
		
		return timerQueueList;
	}
	
	public List<String> isItConfiguredTime(LocalDateTime now, Map<String, BossTimer> bossTimerMap, Map<String, List<FixedEvent>> fixedEventMap) {
		//Handle individual boss timers
		long start = System.currentTimeMillis();
		List<String> timerQueueList = new ArrayList<String>();
		
		for (String player : bossTimerMap.keySet()) {
			BossTimer bt = bossTimerMap.get(player);
			List<String> bossNameList = bt.getBossName();
			List<Long> timerList = bt.getTimerVal();
			for (int i = 0; i < bossNameList.size(); i++) {
				String boss = bossNameList.get(i);
				Long alertDelay = timerList.get(i);
				if (fixedEventMap.containsKey(boss)) {
					List<FixedEvent> fixedEventList = fixedEventMap.get(boss);
					for (FixedEvent fe : fixedEventList) {
						//Next time be smart and use java.time
						if (fe.getDayOfWeek() == now.getDayOfWeek()) {
							if (alertDelay <= fe.getMin()) {
								if (fe.getHour() == now.getHour() && (fe.getMin() - alertDelay) == now.getMinute()) {
									timerQueueList.add(player + " " + boss + " in " + alertDelay + " mins.");
									log.info("1 " + fe.toString());
								}
							}
							else {
								Long minutesLeft = alertDelay % 60;
								Double hours = Math.floor(alertDelay / 60);
								Double adjustedHours = (fe.getHour() - hours);
								if (adjustedHours > 0 || (adjustedHours == 0 && minutesLeft == 0)) {
									if (minutesLeft <= fe.getMin()) {
										if (adjustedHours == now.getHour() && (fe.getMin() - minutesLeft) == now.getMinute()) {
											timerQueueList.add(player + " " + boss + " in " + alertDelay + " mins.");
											log.info("2 " + fe.toString());
										}
									}
									else {
										minutesLeft = minutesLeft - fe.getMin();
										if ((adjustedHours - 1) == now.getHour() && (60 - minutesLeft) == now.getMinute()) {
											timerQueueList.add(player + " " + boss + " in " + alertDelay + " mins.");
											log.info("3 " + fe.toString());
										}
									}
								}
							}
						}
						else {
							Long minutesLeft = alertDelay % 60;
							Double hours = Math.floor(alertDelay / 60);
							Double adjustedHours = (fe.getHour() - hours);
							//Check yesterday
							if (Utils.getPreviousDay(fe.getDayOfWeek()) == now.getDayOfWeek()) {
								if ((adjustedHours == 0 && minutesLeft > 0)) {
									adjustedHours = 24 - adjustedHours;
									if (minutesLeft <= fe.getMin()) {
										if (adjustedHours == now.getHour() && (fe.getMin() - minutesLeft) == now.getMinute()) {
											timerQueueList.add(player + " " + boss + " in " + alertDelay + " mins.");
											log.info("4 " + fe.toString());
										}
									}
									else {
										minutesLeft = minutesLeft - fe.getMin();
										if ((adjustedHours - 1) == now.getHour() && (60 - minutesLeft) == now.getMinute()) {
											timerQueueList.add(player + " " + boss + " in " + alertDelay + " mins.");
											log.info("5 " + fe.toString());
										}
									}
								}
								else if (adjustedHours < 0) {
									adjustedHours = 24 - Math.abs(adjustedHours);
									if (minutesLeft <= fe.getMin()) {
										if (adjustedHours == now.getHour() && (fe.getMin() - minutesLeft) == now.getMinute()) {
											timerQueueList.add(player + " " + boss + " in " + alertDelay + " mins.");
											log.info("6 " + fe.toString());
										}
									}
									else {
										minutesLeft = minutesLeft - fe.getMin();
										if ((adjustedHours - 1) == now.getHour() && (60 - minutesLeft) == now.getMinute()) {
											timerQueueList.add(player + " " + boss + " in " + alertDelay + " mins.");
											log.info("7 " + fe.toString());
										}
									}
								}
							}			
						}
					}
				}
			}
		}
		long end = System.currentTimeMillis();
		long duration = (end - start);
		if (duration > 3) 
			log.info("Loop timing: " + duration + "ms.");
		return timerQueueList;
	}
}
