package com.jk.jasper_bot;

public enum ChannelListEnum {
	
	GENERAL_CHAT(531416835812491264L),
	BOT_COMMANDS(531411774281613312L),
	OFFICER_CHAT(722445465345458196L),
	NA_BOSS(767469751064199168L),
	BOT_TESTING(771183681925677078L),
	WELCOME(531408559171174401L),
	PURGATORY(773391783593246782L);
	
	public Long channelId;

	ChannelListEnum(Long channelId) {
		this.channelId = channelId;
	}
	
	public String getChannelName(ChannelListEnum channel) {
		return channel.name();
	}
	public Long getChannelIdByName(String channelName) {
		for(ChannelListEnum channel: ChannelListEnum.values()) {
			if (channel.name().equalsIgnoreCase(channelName)) {
				return channel.getChannelId();
			}
	    }
		return BOT_TESTING.getChannelId();
	}
	
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}
