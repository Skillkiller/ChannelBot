package de.skillkiller.channelbot.commands;

import de.skillkiller.channelbot.util.files.Config;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Skillkiller on 26.06.2017.
 */
public class TempChannel implements Command {
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws ParseException, IOException {
        Config guildConfig = new Config(event.getGuild().getId());
        if(event.getGuild().getOwner().getUser().getId().equals(event.getAuthor().getId())) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.green);
            StringBuilder builder = new StringBuilder();

            for(String s : guildConfig.getTempChannel()) {
                builder.append(event.getGuild().getVoiceChannelById(s).getName() + "(" + event.getGuild().getVoiceChannelById(s).getId() + ")\n");
            }

            if (builder.toString().length() == 0) {
                builder.append("Keinen Temporären Channel vorhanden");
            }

            embedBuilder.addField("Temporäre Channel", builder.toString().trim(), false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
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
        return "Gibt eine Liste aller derzeitigen Temporären Channel aus.";
    }

    @Override
    public String commandType() {
        return null;
    }
}
