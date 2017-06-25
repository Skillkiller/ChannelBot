package de.skillkiller.channelbot.listeners;

import de.skillkiller.channelbot.commands.CommandHandler;
import de.skillkiller.channelbot.core.Main;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.text.ParseException;

/**
 * Created by Sebastian on 04.05.2017.
 */
public class LSTCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event.getMessage().getContent().startsWith(Main.commandPrefix) && event.getAuthor().getId() != event.getJDA().getSelfUser().getId()){
            try {
                CommandHandler.handleCommand(CommandHandler.parse.parse(event.getMessage().getContent(), event));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }
}
