package com.jk.jasper_bot;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixedEventTimer {
	
	Logger log = LoggerFactory.getLogger(FixedEventTimer.class);
	final Map<String, List<FixedEvent>> eventMap = new HashMap<String, List<FixedEvent>>();
	
	public FixedEventTimer() {
		initEventMap();
	}

	private void initEventMap() {
		
		//Used to adjust the BUFF timers based on the current local time zone (CST / CDT)
		//BUFF Timers are fixed to XDT so they will be adjusted 1 hour backwards when in XST
		//CDT = -05:00, CST = -06:00
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.systemDefault());
		String zoneOffset = zdt.getOffset().toString(); //"-05:00"
		int offset = 0; //distance from CDT, not distance from UTC
		if (zoneOffset.equals("-05:00")) { //CDT
			offset = 0;
		}
		else if (zoneOffset.equals("-06:00")) { //CST
			offset = 1;
		}
		else { //Unknown TZ (untested)
			int i = zoneOffset.indexOf(':');
			String offsetValueAsString = zoneOffset.substring(i-2, i);
			offset = 5 - Integer.parseInt(offsetValueAsString);
		}
		log.info("offset from CDT: " + offset);
		
		//Vell
		List<FixedEvent> vellTimes = new ArrayList<>();
		vellTimes.add(new FixedEvent(0, 19, DayOfWeek.WEDNESDAY));
		vellTimes.add(new FixedEvent(0, 16, DayOfWeek.SUNDAY));
		eventMap.put(BossEnum.VELL.name(), vellTimes);
		
		//Karanda
		List<FixedEvent> karandaTimes = new ArrayList<>();
		karandaTimes.add(new FixedEvent(15, 0, DayOfWeek.MONDAY));
		karandaTimes.add(new FixedEvent(0, 2, DayOfWeek.MONDAY));
		karandaTimes.add(new FixedEvent(15, 0, DayOfWeek.TUESDAY));
		karandaTimes.add(new FixedEvent(0, 2, DayOfWeek.WEDNESDAY));
		karandaTimes.add(new FixedEvent(0, 9, DayOfWeek.WEDNESDAY));
		karandaTimes.add(new FixedEvent(15, 22, DayOfWeek.WEDNESDAY));
		karandaTimes.add(new FixedEvent(15, 0, DayOfWeek.FRIDAY));
		karandaTimes.add(new FixedEvent(0, 5, DayOfWeek.FRIDAY));
		karandaTimes.add(new FixedEvent(0, 12, DayOfWeek.FRIDAY));
		karandaTimes.add(new FixedEvent(15, 0, DayOfWeek.SATURDAY));
		karandaTimes.add(new FixedEvent(0, 19, DayOfWeek.SATURDAY));
		eventMap.put(BossEnum.KARANDA.name(), karandaTimes);
		
		//Kzarka
		List<FixedEvent> kzarkaTimes = new ArrayList<>();
		kzarkaTimes.add(new FixedEvent(0, 5, DayOfWeek.MONDAY));
		kzarkaTimes.add(new FixedEvent(0, 9, DayOfWeek.MONDAY));
		kzarkaTimes.add(new FixedEvent(15, 22, DayOfWeek.MONDAY));
		kzarkaTimes.add(new FixedEvent(0, 5, DayOfWeek.TUESDAY));
		kzarkaTimes.add(new FixedEvent(15, 0, DayOfWeek.WEDNESDAY));
		kzarkaTimes.add(new FixedEvent(15, 22, DayOfWeek.WEDNESDAY));
		kzarkaTimes.add(new FixedEvent(0, 5, DayOfWeek.THURSDAY));
		kzarkaTimes.add(new FixedEvent(0, 16, DayOfWeek.THURSDAY));
		kzarkaTimes.add(new FixedEvent(15, 0, DayOfWeek.FRIDAY));
		kzarkaTimes.add(new FixedEvent(0, 19, DayOfWeek.FRIDAY));
		kzarkaTimes.add(new FixedEvent(15, 22, DayOfWeek.FRIDAY));
		kzarkaTimes.add(new FixedEvent(0, 19, DayOfWeek.SATURDAY));
		kzarkaTimes.add(new FixedEvent(0, 2, DayOfWeek.SUNDAY));
		kzarkaTimes.add(new FixedEvent(0, 12, DayOfWeek.SUNDAY));
		kzarkaTimes.add(new FixedEvent(15, 22, DayOfWeek.SUNDAY));
		eventMap.put(BossEnum.KZARKA.name(), kzarkaTimes);
		
		//Kutum
		List<FixedEvent> kutumTimes = new ArrayList<>();
		kutumTimes.add(new FixedEvent(15, 0, DayOfWeek.MONDAY));
		kutumTimes.add(new FixedEvent(0, 16, DayOfWeek.MONDAY));
		kutumTimes.add(new FixedEvent(0, 2, DayOfWeek.TUESDAY));
		kutumTimes.add(new FixedEvent(0, 12, DayOfWeek.TUESDAY));
		kutumTimes.add(new FixedEvent(15, 0, DayOfWeek.WEDNESDAY));
		kutumTimes.add(new FixedEvent(0, 16, DayOfWeek.WEDNESDAY));
		kutumTimes.add(new FixedEvent(0, 2, DayOfWeek.THURSDAY));
		kutumTimes.add(new FixedEvent(0, 9, DayOfWeek.THURSDAY));
		kutumTimes.add(new FixedEvent(0, 19, DayOfWeek.THURSDAY));
		kutumTimes.add(new FixedEvent(0, 9, DayOfWeek.FRIDAY));
		kutumTimes.add(new FixedEvent(15, 22, DayOfWeek.FRIDAY));
		kutumTimes.add(new FixedEvent(0, 9, DayOfWeek.SATURDAY));
		kutumTimes.add(new FixedEvent(0, 5, DayOfWeek.SUNDAY));
		eventMap.put(BossEnum.KUTUM.name(), kutumTimes);
		
		//Nouver
		List<FixedEvent> nouvTimes = new ArrayList<>();
		nouvTimes.add(new FixedEvent(0, 19, DayOfWeek.MONDAY));
		nouvTimes.add(new FixedEvent(0, 9, DayOfWeek.TUESDAY));
		nouvTimes.add(new FixedEvent(0, 16, DayOfWeek.TUESDAY));
		nouvTimes.add(new FixedEvent(0, 12, DayOfWeek.WEDNESDAY));
		nouvTimes.add(new FixedEvent(15, 0, DayOfWeek.THURSDAY));
		nouvTimes.add(new FixedEvent(0, 12, DayOfWeek.THURSDAY));
		nouvTimes.add(new FixedEvent(0, 2, DayOfWeek.FRIDAY));
		nouvTimes.add(new FixedEvent(0, 16, DayOfWeek.FRIDAY));
		nouvTimes.add(new FixedEvent(0, 5, DayOfWeek.SATURDAY));
		nouvTimes.add(new FixedEvent(0, 12, DayOfWeek.SATURDAY));
		nouvTimes.add(new FixedEvent(15, 0, DayOfWeek.SUNDAY));
		nouvTimes.add(new FixedEvent(0, 9, DayOfWeek.SUNDAY));
		nouvTimes.add(new FixedEvent(15, 22, DayOfWeek.SUNDAY));
		eventMap.put(BossEnum.NOUVER.name(), nouvTimes);
		
		//Garmoth
		List<FixedEvent> garTimes = new ArrayList<>();
		garTimes.add(new FixedEvent(15, 22, DayOfWeek.TUESDAY));
		garTimes.add(new FixedEvent(15, 22, DayOfWeek.THURSDAY));
		garTimes.add(new FixedEvent(0, 19, DayOfWeek.SUNDAY));
		eventMap.put(BossEnum.GARMOTH.name(), garTimes);
		
		//Offin
		List<FixedEvent> offinTimes = new ArrayList<>();
		offinTimes.add(new FixedEvent(0, 12, DayOfWeek.MONDAY));
		offinTimes.add(new FixedEvent(0, 16, DayOfWeek.WEDNESDAY));
		offinTimes.add(new FixedEvent(0, 2, DayOfWeek.SATURDAY));
		eventMap.put(BossEnum.OFFIN.name(), offinTimes);
		
		//Imperial Crafting Reset -- Alch/Cooking boxes
		List<FixedEvent> imperialCraftTimes = new ArrayList<>(); 
		imperialCraftTimes.add(new FixedEvent(0, 1, DayOfWeek.MONDAY));
		imperialCraftTimes.add(new FixedEvent(0, 4, DayOfWeek.MONDAY));
		imperialCraftTimes.add(new FixedEvent(0, 7, DayOfWeek.MONDAY));
		imperialCraftTimes.add(new FixedEvent(0, 10, DayOfWeek.MONDAY));
		imperialCraftTimes.add(new FixedEvent(0, 13, DayOfWeek.MONDAY));
		imperialCraftTimes.add(new FixedEvent(0, 16, DayOfWeek.MONDAY));
		imperialCraftTimes.add(new FixedEvent(0, 19, DayOfWeek.MONDAY));
		imperialCraftTimes.add(new FixedEvent(0, 22, DayOfWeek.MONDAY));
		
		imperialCraftTimes.add(new FixedEvent(0, 1, DayOfWeek.TUESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 4, DayOfWeek.TUESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 7, DayOfWeek.TUESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 10, DayOfWeek.TUESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 13, DayOfWeek.TUESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 16, DayOfWeek.TUESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 19, DayOfWeek.TUESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 22, DayOfWeek.TUESDAY));
		
		imperialCraftTimes.add(new FixedEvent(0, 1, DayOfWeek.WEDNESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 4, DayOfWeek.WEDNESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 7, DayOfWeek.WEDNESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 10, DayOfWeek.WEDNESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 13, DayOfWeek.WEDNESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 16, DayOfWeek.WEDNESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 19, DayOfWeek.WEDNESDAY));
		imperialCraftTimes.add(new FixedEvent(0, 22, DayOfWeek.WEDNESDAY));
		
		imperialCraftTimes.add(new FixedEvent(0, 1, DayOfWeek.THURSDAY));
		imperialCraftTimes.add(new FixedEvent(0, 4, DayOfWeek.THURSDAY));
		imperialCraftTimes.add(new FixedEvent(0, 7, DayOfWeek.THURSDAY));
		imperialCraftTimes.add(new FixedEvent(0, 10, DayOfWeek.THURSDAY));
		imperialCraftTimes.add(new FixedEvent(0, 13, DayOfWeek.THURSDAY));
		imperialCraftTimes.add(new FixedEvent(0, 16, DayOfWeek.THURSDAY));
		imperialCraftTimes.add(new FixedEvent(0, 19, DayOfWeek.THURSDAY));
		imperialCraftTimes.add(new FixedEvent(0, 22, DayOfWeek.THURSDAY));
		
		imperialCraftTimes.add(new FixedEvent(0, 1, DayOfWeek.FRIDAY));
		imperialCraftTimes.add(new FixedEvent(0, 4, DayOfWeek.FRIDAY));
		imperialCraftTimes.add(new FixedEvent(0, 7, DayOfWeek.FRIDAY));
		imperialCraftTimes.add(new FixedEvent(0, 10, DayOfWeek.FRIDAY));
		imperialCraftTimes.add(new FixedEvent(0, 13, DayOfWeek.FRIDAY));
		imperialCraftTimes.add(new FixedEvent(0, 16, DayOfWeek.FRIDAY));
		imperialCraftTimes.add(new FixedEvent(0, 19, DayOfWeek.FRIDAY));
		imperialCraftTimes.add(new FixedEvent(0, 22, DayOfWeek.FRIDAY));
		
		imperialCraftTimes.add(new FixedEvent(0, 1, DayOfWeek.SATURDAY));
		imperialCraftTimes.add(new FixedEvent(0, 4, DayOfWeek.SATURDAY));
		imperialCraftTimes.add(new FixedEvent(0, 7, DayOfWeek.SATURDAY));
		imperialCraftTimes.add(new FixedEvent(0, 10, DayOfWeek.SATURDAY));
		imperialCraftTimes.add(new FixedEvent(0, 13, DayOfWeek.SATURDAY));
		imperialCraftTimes.add(new FixedEvent(0, 16, DayOfWeek.SATURDAY));
		imperialCraftTimes.add(new FixedEvent(0, 19, DayOfWeek.SATURDAY));
		imperialCraftTimes.add(new FixedEvent(0, 22, DayOfWeek.SATURDAY));
		
		imperialCraftTimes.add(new FixedEvent(0, 1, DayOfWeek.SUNDAY));
		imperialCraftTimes.add(new FixedEvent(0, 4, DayOfWeek.SUNDAY));
		imperialCraftTimes.add(new FixedEvent(0, 7, DayOfWeek.SUNDAY));
		imperialCraftTimes.add(new FixedEvent(0, 10, DayOfWeek.SUNDAY));
		imperialCraftTimes.add(new FixedEvent(0, 13, DayOfWeek.SUNDAY));
		imperialCraftTimes.add(new FixedEvent(0, 16, DayOfWeek.SUNDAY));
		imperialCraftTimes.add(new FixedEvent(0, 19, DayOfWeek.SUNDAY));
		imperialCraftTimes.add(new FixedEvent(0, 22, DayOfWeek.SUNDAY));
		
		eventMap.put(BossEnum.IMPERIALCRAFT.name(), imperialCraftTimes);
		
		//Imperial Trading Reset
		List<FixedEvent> imperialResetTimes = new ArrayList<>();
		//imperialResetTimes.add(new FixedEvent(0, 3, DayOfWeek.MONDAY));
		//imperialResetTimes.add(new FixedEvent(0, 9, DayOfWeek.MONDAY));
		//imperialResetTimes.add(new FixedEvent(0, 15, DayOfWeek.MONDAY));
		//imperialResetTimes.add(new FixedEvent(0, 21, DayOfWeek.MONDAY));
		imperialResetTimes.add(new FixedEvent(0, 1, DayOfWeek.MONDAY));
		imperialResetTimes.add(new FixedEvent(0, 7, DayOfWeek.MONDAY));
		imperialResetTimes.add(new FixedEvent(0, 13, DayOfWeek.MONDAY));
		imperialResetTimes.add(new FixedEvent(0, 19, DayOfWeek.MONDAY));
		
		imperialResetTimes.add(new FixedEvent(0, 1, DayOfWeek.TUESDAY));
		imperialResetTimes.add(new FixedEvent(0, 7, DayOfWeek.TUESDAY));
		imperialResetTimes.add(new FixedEvent(0, 13, DayOfWeek.TUESDAY));
		imperialResetTimes.add(new FixedEvent(0, 19, DayOfWeek.TUESDAY));
		
		imperialResetTimes.add(new FixedEvent(0, 1, DayOfWeek.WEDNESDAY));
		imperialResetTimes.add(new FixedEvent(0, 7, DayOfWeek.WEDNESDAY));
		imperialResetTimes.add(new FixedEvent(0, 13, DayOfWeek.WEDNESDAY));
		imperialResetTimes.add(new FixedEvent(0, 19, DayOfWeek.WEDNESDAY));
		
		imperialResetTimes.add(new FixedEvent(0, 1, DayOfWeek.THURSDAY));
		imperialResetTimes.add(new FixedEvent(0, 7, DayOfWeek.THURSDAY));
		imperialResetTimes.add(new FixedEvent(0, 13, DayOfWeek.THURSDAY));
		imperialResetTimes.add(new FixedEvent(0, 19, DayOfWeek.THURSDAY));
		
		imperialResetTimes.add(new FixedEvent(0, 1, DayOfWeek.FRIDAY));
		imperialResetTimes.add(new FixedEvent(0, 7, DayOfWeek.FRIDAY));
		imperialResetTimes.add(new FixedEvent(0, 13, DayOfWeek.FRIDAY));
		imperialResetTimes.add(new FixedEvent(0, 19, DayOfWeek.FRIDAY));
		
		imperialResetTimes.add(new FixedEvent(0, 1, DayOfWeek.SATURDAY));
		imperialResetTimes.add(new FixedEvent(0, 7, DayOfWeek.SATURDAY));
		imperialResetTimes.add(new FixedEvent(0, 13, DayOfWeek.SATURDAY));
		imperialResetTimes.add(new FixedEvent(0, 19, DayOfWeek.SATURDAY));
		
		imperialResetTimes.add(new FixedEvent(0, 1, DayOfWeek.SUNDAY));
		imperialResetTimes.add(new FixedEvent(0, 7, DayOfWeek.SUNDAY));
		imperialResetTimes.add(new FixedEvent(0, 13, DayOfWeek.SUNDAY));
		imperialResetTimes.add(new FixedEvent(0, 19, DayOfWeek.SUNDAY));
		
		eventMap.put(BossEnum.IMPERIALTRADE.name(), imperialResetTimes);
		
		//BDONavy Guild Buffs
		List<FixedEvent> buffTimes = new ArrayList<>();
		buffTimes.add(new FixedEvent(0, 1 - offset, DayOfWeek.MONDAY));
		buffTimes.add(new FixedEvent(0, 7 - offset, DayOfWeek.MONDAY));
		buffTimes.add(new FixedEvent(0, 12 - offset, DayOfWeek.MONDAY));
		buffTimes.add(new FixedEvent(0, 18 - offset, DayOfWeek.MONDAY));
		
		buffTimes.add(new FixedEvent(0, 1 - offset, DayOfWeek.TUESDAY));
		buffTimes.add(new FixedEvent(0, 7 - offset, DayOfWeek.TUESDAY));
		buffTimes.add(new FixedEvent(0, 12 - offset, DayOfWeek.TUESDAY));
		buffTimes.add(new FixedEvent(0, 18 - offset, DayOfWeek.TUESDAY));
		
		buffTimes.add(new FixedEvent(0, 1 - offset, DayOfWeek.WEDNESDAY));
		buffTimes.add(new FixedEvent(0, 7 - offset, DayOfWeek.WEDNESDAY));
		buffTimes.add(new FixedEvent(0, 12 - offset, DayOfWeek.WEDNESDAY));
		buffTimes.add(new FixedEvent(0, 18 - offset, DayOfWeek.WEDNESDAY));

		buffTimes.add(new FixedEvent(0, 1 - offset, DayOfWeek.THURSDAY));
		buffTimes.add(new FixedEvent(0, 7 - offset, DayOfWeek.THURSDAY));
		buffTimes.add(new FixedEvent(0, 12 - offset, DayOfWeek.THURSDAY));
		buffTimes.add(new FixedEvent(0, 18 - offset, DayOfWeek.THURSDAY));
		
		buffTimes.add(new FixedEvent(0, 1 - offset, DayOfWeek.FRIDAY));
		buffTimes.add(new FixedEvent(0, 7 - offset, DayOfWeek.FRIDAY));
		buffTimes.add(new FixedEvent(0, 12 - offset, DayOfWeek.FRIDAY));
		buffTimes.add(new FixedEvent(0, 18 - offset, DayOfWeek.FRIDAY));
		
		buffTimes.add(new FixedEvent(0, 1 - offset, DayOfWeek.SATURDAY));
		buffTimes.add(new FixedEvent(0, 7 - offset, DayOfWeek.SATURDAY));
		buffTimes.add(new FixedEvent(0, 12 - offset, DayOfWeek.SATURDAY));
		buffTimes.add(new FixedEvent(0, 18 - offset, DayOfWeek.SATURDAY));
		
		buffTimes.add(new FixedEvent(0, 1 - offset, DayOfWeek.SUNDAY));
		buffTimes.add(new FixedEvent(0, 7 - offset, DayOfWeek.SUNDAY));
		buffTimes.add(new FixedEvent(0, 12 - offset, DayOfWeek.SUNDAY));
		buffTimes.add(new FixedEvent(0, 18 - offset, DayOfWeek.SUNDAY));
		
		eventMap.put(BossEnum.BUFF.name(), buffTimes);
	}

	public Map<String, List<FixedEvent>> getEventMap() {
		return eventMap;
	}
	

}
