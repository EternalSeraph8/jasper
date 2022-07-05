package com.jk.jasper_bot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.security.auth.login.LoginException;

//import org.apache.logging.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotRunner extends ListenerAdapter {

	// private Logger log = LogManager.getLogger(BotRunner.class);
	Logger log = LoggerFactory.getLogger(BotRunner.class);

	private static JDA jda = null;
	Utils utils = new Utils();
	static Map<String, BossTimer> bossTimerMap = new HashMap<String, BossTimer>();

	public int initBot(String key) {
		try {
			initData();

			jda = JDABuilder.createDefault(key)
					.addEventListeners(new BotRunner()).build();
			jda.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
			log.info("Bot started");
		} catch (LoginException e) {
			e.printStackTrace();
			return 0;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public void tearDownBot() {
		if (jda != null) {
			cleanupData();

			log.info("Bot stopped");
			jda.shutdown();
		}
	}

	private void initData() {

		// Load boss timer data from text file
		try {
			BufferedReader br = new BufferedReader(new FileReader("boss.txt"));

			String currLine = null;
			while ((currLine = br.readLine()) != null) {
				// parse current line
				int index = currLine.indexOf(':');
				String userName = currLine.substring(0, index).trim();
				String rest = currLine.substring(index+1);
				List<String> restList = Arrays.asList(rest.trim().split(","));
				BossTimer tempBT = new BossTimer();
				for (String bossTimer : restList) {
					List<String> btList = Arrays.asList(bossTimer.trim().split(" "));
					tempBT.getBossName().add(btList.get(0));
					tempBT.getTimerVal().add(Long.valueOf(btList.get(1)));
				}
				bossTimerMap.put(userName, tempBT);
			}
			br.close();
		} catch (FileNotFoundException e) {
			//This is fine, it's probably the first time run.  Just return.
			return;
		} catch (IOException e) {
			//This is fine, it's probably the first time run.  Just return.
			return;
		}

	}

	protected void cleanupData() {
		log.info("Running cleanup procedure");
		// Write Boss Timer data to disk
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("boss.txt"));
			for (String player : bossTimerMap.keySet()) {
				BossTimer bt = bossTimerMap.get(player);
				List<String> bossNames = bt.getBossName(); // Support multiple timers for each player
				List<Long> timerVals = bt.getTimerVal();
				bw.append(player + ":");
				for (int i = 0; i < bossNames.size(); i++) {
					String tempBossName = bossNames.get(i);
					Long tempTimerVal = timerVals.get(i);
					bw.append(tempBossName + " " + tempTimerVal);
					if (i < bossNames.size() - 1) {
						bw.append(",");
					}
				}
				bw.append("\n");
			}
			bw.close();
		} catch (IOException e) {
			log.error(e.toString());
		}
	}
	
	/*
	private void cleanupData(JDA jda) {
		log.info("Running cleanup procedure");
		// Write Boss Timer data to disk
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("boss.txt"));
			for (String player : bossTimerMap.keySet()) {
				BossTimer bt = bossTimerMap.get(player);
				
				//Detects players that leave the discord
				boolean isValidMember = false;
				List<Member> memberList = jda.getGuildById("531381047401906176L").getMembers();
				for (Member m : memberList) {
					if (player.equals(m.getAsMention())) {
						isValidMember = true;
					}
				}
				if (isValidMember) {
					List<String> bossNames = bt.getBossName(); // Support multiple timers for each player
					List<Long> timerVals = bt.getTimerVal();
					bw.append(player + ":");
					for (int i = 0; i < bossNames.size(); i++) {
						String tempBossName = bossNames.get(i);
						Long tempTimerVal = timerVals.get(i);
						bw.append(tempBossName + " " + tempTimerVal);
						if (i < bossNames.size() - 1) {
							bw.append(",");
						}
					}
					bw.append("\n");
				}
			}
			bw.close();
		} catch (IOException e) {
			log.error(e.toString());
		}
		
	}*/


	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}

		JDA jda = event.getJDA(); // JDA, the core of the api.
		long responseNumber = event.getResponseNumber();// The amount of discord events that JDA has received since the
														// last reconnect.
		
		// Event specific information
		User author = event.getAuthor(); // The user that sent the message
		//log.info(author.getAsMention());
		Message message = event.getMessage(); // The message that was received.
		MessageChannel channel = event.getChannel();

		String msg = message.getContentDisplay();

		if (event.isFromType(ChannelType.TEXT) && msg.startsWith("!")) { // Ignore anything not prefixed with !
			Guild guild = event.getGuild();
			TextChannel textChannel = event.getTextChannel();
			Member member = event.getMember();

			String name;
			String mentionableUser;
			if (message.isWebhookMessage()) {
				name = author.getName();
				mentionableUser = author.getAsMention();
			} else {
				name = member.getEffectiveName();
				mentionableUser = member.getAsMention();
			}

			log.info("(" + guild.getName() + ")[" + textChannel.getName() + "]<" + name + ">: " + msg);

			String firstToken = null;
			String rest = null;
			int index = msg.indexOf(' ');
			if (index > -1) {
				firstToken = msg.substring(0, index).trim();
				rest = msg.substring(index).trim();
			} else {
				firstToken = msg;
			}

			if (firstToken.equals("!map")) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("Sea Map", null);
				eb.setColor(Color.red);
				// eb.setDescription("Text");
				// eb.addField("Title of field", "test of field", false);
				// eb.addBlankField(false);
				// eb.setAuthor("name", null,
				// "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
				// eb.setFooter("Text",
				// "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
				// eb.setThumbnail("https://cdn.discordapp.com/attachments/661938033930797068/738538949995266098/The_Great_Sea_Map_V1.jpg");
				eb.setImage(
						"https://cdn.discordapp.com/attachments/661938033930797068/738538949995266098/The_Great_Sea_Map_V1.jpg");
				channel.sendMessage(eb.build()).queue();
			} else if (firstToken.equals("!currents") || firstToken.equals("!current")) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("Current Map", null);
				eb.setColor(Color.blue);
				eb.setImage(
						"https://cdn.discordapp.com/attachments/767656181812297738/767656275865108480/BlackDesertOnline_Ocean_Map_4350x4350_Full_Ceonsiune_v4.png");
				channel.sendMessage(eb.build()).queue();
			} else if (firstToken.equalsIgnoreCase("!timer") || firstToken.equalsIgnoreCase("!timers")) {
				// parse the rest of the string for a command
				// !timer <bossName> <warningTimerInMinutes>
				// EX !timer Vell 15
				if (rest == null || rest.isEmpty()) {
					channel.sendMessage("No params provided, see !jasperHelp for info.").queue();
				} else if (rest.trim() == "help"){
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("Valid Commands: ", null);
					String helpString = generateHelpString();
					eb.setDescription(helpString);
					eb.setColor(Color.green);
					channel.sendMessage(eb.build()).queue();
				} else {
					List<String> restList = Arrays.asList(rest.trim().split(" "));
					if (restList == null || restList.isEmpty() || restList.size() != 2) {
						channel.sendMessage("Please provide valid params (ex !timer vell 15)").queue();
					} else if (restList.size() == 2) {
						log.info("Timer requested for: " + restList.toString());
						String bossName = utils.isSupportedBossName(restList.get(0));
						String warningTimer = restList.get(1); // in mins

						if (utils.isNumeric(warningTimer) && bossName != null) {
							if (bossTimerMap.containsKey(mentionableUser)) {
								BossTimer bt = bossTimerMap.get(mentionableUser);
								bt.getBossName().add(bossName);
								bt.getTimerVal().add(Long.valueOf(warningTimer));
								bossTimerMap.put(mentionableUser, bt);
							} else {
								BossTimer bt = new BossTimer();
								bt.getBossName().add(bossName);
								bt.getTimerVal().add(Long.valueOf(warningTimer));
								bossTimerMap.put(mentionableUser, bt);
							}

							log.info("Timer created for: " + restList.toString());
							log.info(bossTimerMap.toString());
							channel.sendMessage("Alert created for user [" + name + "], boss [" + bossName.toLowerCase()
									+ "], timer [" + warningTimer + "]").queue();
						}
					}
				}
				cleanupData(); //Write timer data to disk in case it has changed
			}
			else if (firstToken.equalsIgnoreCase("!getTimer") || firstToken.equalsIgnoreCase("!getTimers")) {
				if (bossTimerMap.containsKey(mentionableUser)) {
					BossTimer bt = bossTimerMap.get(mentionableUser);
					EmbedBuilder eb = new EmbedBuilder();
					eb.setTitle("Timers for " + name, null);
					String btString = generateBtString(bt);
					eb.setDescription(btString);
					eb.setColor(Color.green);
					channel.sendMessage(eb.build()).queue();
					
				} else {
					channel.sendMessage("No timers found for user.  See !jasperHelp for info.").queue();
				}
			}
			else if (firstToken.equalsIgnoreCase("!removeTimer") || firstToken.equalsIgnoreCase("!removeTimers")) {
				if (rest == null || rest.isEmpty()) {
					channel.sendMessage("No params provided, unable to remove timer.  See !jasperHelp for info.").queue();
				}
				else if (!bossTimerMap.containsKey(mentionableUser)) {
					log.info(bossTimerMap.toString());
					channel.sendMessage("No timers found for user.  See !jasperHelp for info.").queue();					
				}
				else {
					BossTimer bt = bossTimerMap.get(mentionableUser);
					List<String> bossNames = bt.getBossName();
					List<Long> timers = bt.getTimerVal();
					boolean wasAlertRemoved = false;
					String removedBossName = "";
					Long removedAlertDuration = null;
					for (int i = 0; i < bossNames.size(); i++) {
						//remove by name
						if (rest.trim().equalsIgnoreCase(bossNames.get(i))) {
							removedBossName = bossNames.remove(i);
							removedAlertDuration = timers.remove(i);
							wasAlertRemoved = true;
						}
						//remove by ID
						else if (rest.trim().equals(i + "")) {
							removedBossName = bossNames.remove(i);
							removedAlertDuration = timers.remove(i);
							wasAlertRemoved = true;
						}
					}
					if (wasAlertRemoved) {
						if (bossNames.size() == 0) {
							bossTimerMap.remove(mentionableUser);
						}
						channel.sendMessage("Removed Timer for boss [" + removedBossName + "] and duration [" +
								removedAlertDuration + "] for user [" + name + "]").queue();
					}
					else {
						channel.sendMessage("No timers found for user.  See !jasperHelp for info.").queue();	
					}
				}
				cleanupData(); //Write timer data to disk in case it has changed
			} else if (firstToken.equalsIgnoreCase("!jasperHelp")) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle("Valid Commands: ", null);
				String helpString = generateHelpString();
				eb.setDescription(helpString);
				eb.setColor(Color.green);
				channel.sendMessage(eb.build()).queue();
			}
			
			
			// Testing / silly utility commands
			if (firstToken.equals("!roll")) {
				// In this case, we have an example showing how to use the flatMap operator for
				// a RestAction. The operator
				// will provide you with the object that results after you execute your
				// RestAction. As a note, not all RestActions
				// have object returns and will instead have Void returns. You can still use the
				// flatMap operator to run chain another RestAction!

				Random rand = ThreadLocalRandom.current();
				int roll = rand.nextInt(6) + 1; // This results in 1 - 6 (instead of 0 - 5)
				channel.sendMessage("Your roll: " + roll)
						.flatMap((v) -> roll < 3, sentMessage -> channel
								.sendMessage("The roll wasn't very good... Must be bad luck!\n"))
						.queue();
			}
		} else {
			return;
		}

	}

	public void comment(String message, ChannelListEnum channelEnum) {
		if (jda != null) {
			//If you need to find the channel ID, use this
			//List<TextChannel> channels = jda.getTextChannels();
			//log.info(channels.toString());
			
			//Send message to desired channel
			log.info(message);
			TextChannel channel = jda.getTextChannelById(channelEnum.getChannelId());
			channel.sendMessage(message).queue();
		}
	}
	
	public void commentSailies(String message, ChannelListEnum channelEnum) {
		if (jda != null) {
			//If you need to find the channel ID, use this
			//List<TextChannel> channels = jda.getTextChannels();
			//log.info(channels.toString());
			Role sailiesRole = jda.getRoleById(710995940387061780L);
			
			String messageWithMention = sailiesRole.getAsMention() + " " + message;
			
			//Send message to desired channel
			log.info(messageWithMention);
			TextChannel channel = jda.getTextChannelById(channelEnum.getChannelId());
			channel.sendMessage(messageWithMention).queue();
		}
	}
	

	private String generateHelpString() {
		StringBuilder sb = new StringBuilder();
		sb.append("!timer -- create a new timer alerts (ex !timer vell 15)\n");
		sb.append("!getTimers -- view a list of your timer alerts\n");
		sb.append("!map -- view a map of margoria creature locations\n");
		sb.append("!current -- view a map of margoria currents and barter locations\n");
		sb.append("!removeTimer -- Remove timer by ID (see !getTimers for ID) or by name ex (!removeTimer Vell) or (!removeTimer 0)\n");
		sb.append("If you have multiple timers for a boss, please use the ID method.\n");
		return sb.toString();
	}

	private String generateBtString(BossTimer bt) {
		StringBuilder sb = new StringBuilder();
		List<String> bossNames = bt.getBossName();
		List<Long> timers = bt.getTimerVal();
		for (int i = 0; i < bossNames.size(); i++) {
			sb.append(i + ": " + bossNames.get(i) + " - " + timers.get(i) + "mins.\n");
		}
		
		return sb.toString();
	}

	public static Map<String, BossTimer> getBossTimerMap() {
		return bossTimerMap;
	}

	public static void setBossTimerMap(Map<String, BossTimer> bossTimerMap) {
		BotRunner.bossTimerMap = bossTimerMap;
	}
}
