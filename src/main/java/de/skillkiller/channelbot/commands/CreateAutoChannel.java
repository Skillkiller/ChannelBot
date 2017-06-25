package de.skillkiller.channelbot.commands;

import de.skillkiller.channelbot.util.ServerConfig;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class CreateAutoChannel implements Command {
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws ParseException, IOException {
        Member member = event.getMember();
        ServerConfig guildConfig = new ServerConfig(event.getGuild().getId());
        if(event.getGuild().getOwner().getUser().getId().equals(member.getUser().getId())) {
            if(member.getVoiceState().inVoiceChannel()) {
                if(guildConfig.getAutoChannel().contains(member.getVoiceState().getChannel().getId())) {
                    event.getChannel().sendMessage("Dies ist bereits ein AutoChannel").queue();
                } else {
                    guildConfig.addAutoChannel(member.getVoiceState().getChannel().getId());
                    event.getChannel().sendMessage("Hinzugefügt!").queue();
                }
            }else {
                event.getChannel().sendMessage("Du befindest dich in keinem Voice Channel").queue();
            }
        } else {
            event.getChannel().sendMessage("Du bist dafür nicht berechtigt!").queue();
        }
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String commandType() {
        return null;
    }
}
