package de.skillkiller.channelbot.listeners;

import de.skillkiller.channelbot.util.files.Config;
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
        StringBuilder builder = new StringBuilder();
        int deleted = 0;
        System.out.println("Channelbot läuft auf:");
        for(Guild guild: event.getJDA().getGuilds()) {
            builder.append(guild.getName() + "(O: " + guild.getOwner().getUser().getName() + ")\n");
            Config guildConfig = new Config(guild.getId());
            for (String s : guildConfig.getTempChannel()) {
                Channel channel = guild.getVoiceChannelById(s);

                if (channel != null && channel.getMembers().isEmpty()) {
                    channel.delete().queue();
                    deleted++;
                    try {
                        guildConfig.removeTempChannel(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        System.out.println(builder.toString().trim());
        System.out.println("Beim Start wurden " + deleted + " leere Temp Channel gelöscht!");
    }
}
