package de.skillkiller.channelbot.core;

import de.skillkiller.channelbot.commands.AutoChannel;
import de.skillkiller.channelbot.commands.CommandHandler;
import de.skillkiller.channelbot.commands.TempChannel;
import de.skillkiller.channelbot.listeners.LSTCommand;
import de.skillkiller.channelbot.listeners.LSTReady;
import de.skillkiller.channelbot.listeners.LSTVoice;
import de.skillkiller.channelbot.util.FileManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class Main {

    static JDA bot;

    public static String commandPrefix;

    public static void main(String[] args) {
        FileManager config = new FileManager("", "config.txt");
        try {
            config.loadFile();
            commandPrefix = config.get("commandPrefix");
            if (commandPrefix == null) {
                commandPrefix = ".";
                config.set("commandPrefix", commandPrefix);
            }



            if (config.get("token") == null || config.get("token").equals("Token angeben")) {
                config.set("token", "Token angeben");
                config.saveFile();
            }

            JDABuilder botBuilder = new JDABuilder(AccountType.BOT)
                .setToken(config.get("token"))
                .setAutoReconnect(true)
                .setStatus(OnlineStatus.ONLINE)
                .setToken(config.get("token"))

                .addEventListener(new LSTCommand())
                .addEventListener(new LSTVoice())
                .addEventListener(new LSTReady());

            bot = botBuilder.buildBlocking();
        } catch (IOException | LoginException | RateLimitedException | InterruptedException e) {
            e.printStackTrace();
        }

        CommandHandler.commands.put("autochannel", new AutoChannel());
        CommandHandler.commands.put("tempchannel", new TempChannel());
    }

}
