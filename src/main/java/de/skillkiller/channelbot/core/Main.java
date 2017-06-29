package de.skillkiller.channelbot.core;

import de.skillkiller.channelbot.commands.AutoChannel;
import de.skillkiller.channelbot.commands.CommandHandler;
import de.skillkiller.channelbot.commands.TempChannel;
import de.skillkiller.channelbot.commands.UploadLog;
import de.skillkiller.channelbot.listeners.LSTCommand;
import de.skillkiller.channelbot.listeners.LSTReady;
import de.skillkiller.channelbot.listeners.LSTVoice;
import de.skillkiller.channelbot.util.files.FileManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class Main {

    static JDA bot;


    public static void main(String[] args) {
        FileManager config = new FileManager(new File((System.getProperty("user.dir") + "/ChannelBot/" + "config.txt")));
        try {
            if (config.get("token") == null || config.get("token").equals("Token angeben")) {
                config.set("token", "Token angeben");
                config.saveFile();
            }

            JDABuilder botBuilder = new JDABuilder(AccountType.BOT);
            botBuilder.setToken(config.get("token"));
            botBuilder.setAutoReconnect(true);
            botBuilder.setStatus(OnlineStatus.ONLINE);
            botBuilder.setGame(new Game() {
                @Override
                public String getName() {
                    return "Manage Channel";
                }

                @Override
                public String getUrl() {
                    return "Skillkiller.de";
                }

                @Override
                public GameType getType() {
                    return GameType.DEFAULT;
                }
            });
            botBuilder.setToken(config.get("token"));

            botBuilder.addEventListener(new LSTCommand());
            botBuilder.addEventListener(new LSTVoice());
            botBuilder.addEventListener(new LSTReady());

            bot = botBuilder.buildBlocking();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        CommandHandler.commands.put("autochannel", new AutoChannel());
        CommandHandler.commands.put("tempchannel", new TempChannel());
        CommandHandler.commands.put("upload", new UploadLog());
    }

}
