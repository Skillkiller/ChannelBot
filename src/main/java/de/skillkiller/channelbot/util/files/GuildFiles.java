package de.skillkiller.channelbot.util.files;

import java.io.File;

/**
 * Created by Skillkiller on 28.06.2017.
 */
public class GuildFiles {

    public GuildFiles(String guildID) {
        workingDir = System.getProperty("user.dir") + "/ChannelBot/" + "/guilds/" + guildID;
        messagesFile = new File(workingDir + "/messages.txt");
        configFile = new File(workingDir + "/config.txt");
    }

    private String workingDir;
    private File messagesFile;
    private File configFile;

    public String getWorkingDir() {
        return workingDir;
    }

    public File getMessagesFile() {
        return messagesFile;
    }

    public File getConfigFile() {
        return configFile;
    }
}
