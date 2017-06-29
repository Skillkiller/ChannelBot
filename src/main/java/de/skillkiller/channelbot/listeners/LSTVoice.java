package de.skillkiller.channelbot.listeners;

import de.skillkiller.channelbot.util.files.Config;
import de.skillkiller.channelbot.util.files.Log;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.IOException;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class LSTVoice extends ListenerAdapter{

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        Config guildConfig = new Config(event.getGuild().getId());

        checkCreate(event.getMember(), event.getChannelJoined(), event.getGuild());

    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        Config guildConfig = new Config(event.getGuild().getId());
        checkDelete(event.getChannelLeft(), event.getGuild().getId());
        checkCreate(event.getMember(), event.getChannelJoined(), event.getGuild());
    }

    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        checkDelete(event.getChannelLeft(), event.getGuild().getId());
    }

    @Override
    public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
        Config guildConfig = new Config(event.getGuild().getId());
        if(guildConfig.getAutoChannel().contains(event.getChannel().getId())) {
            try {
                guildConfig.removeAutoChannel(event.getChannel().getId());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                event.getGuild().getTextChannels().get(0).sendMessage("AutoChannel ``" + event.getChannel().getName() + "`` wurde gerade gelöscht!").queue();
            }
        }
    }

    private void checkCreate(Member member, Channel channel, Guild guild) {
        Config guildConfig = new Config(guild.getId());
        Log log = new Log(guild.getId());
        if(guildConfig.getAutoChannel() != null && guildConfig.getAutoChannel().contains(channel.getId())) {
            GuildController controller = guild.getController();
            try {
                controller.setMute(member, true).queue();
            } catch (PermissionException ex) {

            }

            Channel channelNew = controller.createCopyOfChannel(channel).complete();
            channelNew.getManager().setName(getNewChannelName(guild, channel.getName())).queue();
            guild.getController().modifyVoiceChannelPositions().selectPosition(channelNew.getPosition()).moveTo(getNewPostion(channel, guild)).queue();
            controller.moveVoiceMember(member, guild.getVoiceChannelById(channelNew.getId())).queue();
            try {
                controller.setMute(member, false).queue();
            } catch (PermissionException ex) {

            }
            log.writeChannelLog("[CREATE] [" + channelNew.getId() + "] Grund: " + member.getUser().getName() + " benötigt TempChannel");
            try {
                guildConfig.addTempChannel(channelNew.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getNewPostion(Channel oldChannel, Guild guild) {
        Config guildConfig = new Config(guild.getId());
        GuildController guildController = guild.getController();

        int oldPosition = oldChannel.getPosition();

        int neuPosition = 0;

        for(Channel channel : guild.getVoiceChannels()) {
            if(channel.getPosition() > oldPosition) {
                if (!guildConfig.getTempChannel().contains(channel.getId())) {
                    neuPosition = channel.getPosition();
                    break;
                }
            }
        }
        return neuPosition;


    }

    private void checkDelete(Channel channel, String guildID) {
        Config guildConfig = new Config(guildID);
        Log log = new Log(guildID);
        if(guildConfig.getTempChannel() != null && guildConfig.getTempChannel().contains(channel.getId()) && channel.getMembers().isEmpty()) {
            try {
                guildConfig.removeTempChannel(channel.getId());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                channel.delete().queue();
                log.writeChannelLog("[DELETE] [" + channel.getId() + "] Grund: Leerer TempChannel ");
            }
        }
    }

    private String getNewChannelName(Guild guild, String prefix) {
        String s;

        if (prefix.contains("Eingang")) {
            s = prefix.replace("Eingang", RandomString(5));
        } else {
            s = prefix + "-" + RandomString(5);
        }

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
