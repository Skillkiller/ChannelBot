package de.skillkiller.channelbot.listeners;

import de.skillkiller.channelbot.util.ServerConfig;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;

/**
 * Created by Skillkiller on 26.06.2017.
 */
public class LSTReady extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        for(Guild guild: event.getJDA().getGuilds()) {
            ServerConfig guildConfig = new ServerConfig(guild.getId());
            for (String s : guildConfig.getTempChannel()) {
                Channel channel = guild.getVoiceChannelById(s);

                if (channel != null && channel.getMembers().isEmpty()) {
                    channel.delete().queue();
                    try {
                        guildConfig.removeTempChannel(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
