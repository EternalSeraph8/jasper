package com.jk.jasper_bot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Initialize {

	static BotRunner botRunner = new BotRunner();
	static Logger log = LoggerFactory.getLogger(Initialize.class);
    static FixedEventTimer fixedEventTimer;
    static boolean IS_DEBUG = false; 

	public static void main(String[] args) {
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		BossTimerService bts = new BossTimerService();
		
		String key = null;
		if (args.length == 0) {
			log.error("No key provided, please try again.");
			return;
		}
		else {
			key = args[0];
		}
				
		botRunner.initBot(key);
		
		Runnable timerTask = () -> {
			fixedEventTimer = new FixedEventTimer(); //Regenerate the Event Map
			List<String> genTimerQueue = bts.isItBossTime();
			if (genTimerQueue != null && genTimerQueue.size() >= 1) {
				for (String timerMsg : genTimerQueue) {
					if (IS_DEBUG) {
						botRunner.comment(timerMsg, ChannelListEnum.BOT_TESTING);
					}
					else {
						botRunner.comment(timerMsg, ChannelListEnum.GENERAL_CHAT);
					}
				}
			}
			
			List<String> sailiesTimerQueue = bts.isItSailiesTime();
			if (sailiesTimerQueue != null && sailiesTimerQueue.size() >= 1) {
				for (String timerMsg : sailiesTimerQueue) {
					if (IS_DEBUG) {
						botRunner.commentSailies(timerMsg, ChannelListEnum.BOT_TESTING);
					}
					else {
						botRunner.commentSailies(timerMsg, ChannelListEnum.GENERAL_CHAT);
					}
				}
			}
			
			LocalDateTime now = LocalDateTime.now();
			List<String> configuredTimerQueue = bts.isItConfiguredTime(now, BotRunner.getBossTimerMap(), fixedEventTimer.getEventMap());
			if (configuredTimerQueue != null && configuredTimerQueue.size() >= 1) {
				for (String timerMsg : configuredTimerQueue) {
					if (IS_DEBUG) {
						botRunner.comment(timerMsg, ChannelListEnum.BOT_TESTING);
					}
					else {
						botRunner.comment(timerMsg, ChannelListEnum.BOT_COMMANDS);
					}
				}
			}
		};
		service.scheduleAtFixedRate(timerTask, 3, 60, TimeUnit.SECONDS);
		
		/*
		//Backup timer data to disk daily
		Runnable dataCleanupTask = () -> {
			botRunner.cleanupData();
		};
		service.scheduleAtFixedRate(dataCleanupTask, 0, 1, TimeUnit.DAYS);
		*/
	}	
}
