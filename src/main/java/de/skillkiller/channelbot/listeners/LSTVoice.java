package de.skillkiller.channelbot.listeners;

import de.skillkiller.channelbot.util.ServerConfig;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.IOException;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class LSTVoice extends ListenerAdapter{

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        ServerConfig guildConfig = new ServerConfig(event.getGuild().getId());

        checkCreate(event.getMember(), event.getChannelJoined(), guildConfig, event.getGuild());

    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        ServerConfig guildConfig = new ServerConfig(event.getGuild().getId());
        checkDelete(event.getChannelLeft(), guildConfig);
        checkCreate(event.getMember(), event.getChannelJoined(), guildConfig, event.getGuild());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        ServerConfig guildConfig = new ServerConfig(event.getGuild().getId());
        checkDelete(event.getChannelLeft(), guildConfig);
    }

    private void checkCreate(Member member, Channel channel, ServerConfig guildConfig, Guild guild) {
        if(guildConfig.getAutoChannel() != null && guildConfig.getAutoChannel().contains(channel.getId())) {
            GuildController controller = guild.getController();
            Channel channelNew = controller.createCopyOfChannel(channel).complete();
            channelNew.getManager().setName(getNewChannelName(guild, channel.getName())).queue();
            controller.moveVoiceMember(member, guild.getVoiceChannelById(channelNew.getId())).queue();

            try {
                guildConfig.addTempChannel(channelNew.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkDelete(Channel channel, ServerConfig guildConfig) {
        if(guildConfig.getTempChannel() != null && guildConfig.getTempChannel().contains(channel.getId()) && channel.getMembers().isEmpty()) {
            try {
                guildConfig.removeTempChannel(channel.getId());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                channel.delete().queue();
            }
        }
    }

    private String getNewChannelName(Guild guild, String prefix) {
        String s = prefix + "-" + RandomString(5);
        if (guild.getVoiceChannelsByName(s, true).isEmpty()) {
            return s;
        } else {
            return getNewChannelName(guild, prefix);
        }
    }

    private String RandomString(int len) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++)
        {
            sb.append((char)((int)(Math.random()*26)+97));
        }
        return sb.toString();
    }
}
