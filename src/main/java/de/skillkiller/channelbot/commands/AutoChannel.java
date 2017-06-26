package de.skillkiller.channelbot.commands;

import de.skillkiller.channelbot.core.Main;
import de.skillkiller.channelbot.util.ServerConfig;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Skillkiller on 26.06.2017.
 */
public class AutoChannel implements Command {
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws ParseException, IOException {
        Member member = event.getMember();
        ServerConfig guildConfig = new ServerConfig(event.getGuild().getId());
        if(args.length == 0) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.orange);
            embedBuilder.addField("Verwendung!", "``" + Main.commandPrefix + "autochannel [add|remove|list]", false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            if (args[0].equals("add")){
                if(event.getGuild().getOwner().getUser().getId().equals(member.getUser().getId())) {
                    if(member.getVoiceState().inVoiceChannel()) {
                        if(guildConfig.getAutoChannel() != null && guildConfig.getAutoChannel().contains(member.getVoiceState().getChannel().getId())) {
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
            } else if (args[0].equals("remove")) {
                if (args.length == 1) {
                    //TODO AutoChannel remove <ChannelID>

                } else if (args.length == 2){
                    if(event.getGuild().getOwner().getUser().getId().equals(member.getUser().getId())) {
                        if (guildConfig.getAutoChannel().contains(args[1])) {
                            guildConfig.removeAutoChannel(args[1]);
                            event.getChannel().sendMessage("Autochannel gelöscht!").queue();
                        } else {
                            event.getChannel().sendMessage("Dies ist kein AutoChannel").queue();
                        }


                    } else {
                        event.getChannel().sendMessage("Du bist dafür nicht berechtigt!").queue();
                    }
                }
            } else if (args[0].equals("list")) {
                if(event.getGuild().getOwner().getUser().getId().equals(member.getUser().getId())) {

                    StringBuilder builder = new StringBuilder();
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.orange);

                    for (String s : guildConfig.getAutoChannel()) {
                        builder.append(event.getGuild().getVoiceChannelById(s).getName() + "(" + event.getGuild().getVoiceChannelById(s).getId() + "), ");
                    }

                    String s = builder.toString();
                    if (s.length() > 1) {
                        s.substring(0, s.length() - 1);
                    }

                    embedBuilder.addField("AutoChannel", s, false);
                    event.getChannel().sendMessage(embedBuilder.build()).queue();

                } else {
                    event.getChannel().sendMessage("Du bist dafür nicht berechtigt!").queue();
                }
            }
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
