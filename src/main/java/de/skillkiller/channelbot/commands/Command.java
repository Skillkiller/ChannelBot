package de.skillkiller.channelbot.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Sebastian on 07.05.2017.
 */
public interface Command {

    boolean called(String[] args, GuildMessageReceivedEvent event);
    void action(String[] args, GuildMessageReceivedEvent event) throws ParseException, IOException;
    void executed(boolean success, GuildMessageReceivedEvent event);
    String help();
    String description();
    String commandType();
}

