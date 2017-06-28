package de.skillkiller.channelbot.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class ServerConfig {

    public ServerConfig(String guildID) {
        fmConfig = new FileManager("/guilds/" + guildID + "/", "config.txt");

        try {
            fmConfig.loadFile();
            fChannelLog = new File(new FileManager("/guilds/" + guildID + "/", "ChannelLog.txt").getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.guildID = guildID;
    }

    private String guildID;
    private FileManager fmConfig;
    private File fChannelLog;

    public FileManager getServerConfig() {
        return fmConfig;
    }

    public List<String> getAutoChannel() {
        return getChannelList("AutoChannel");
    }

    public void addAutoChannel(String channelID) throws IOException {
        addChannelList("AutoChannel", channelID);
    }

    public void removeAutoChannel(String channelID) throws IOException {
        removeChannelList("AutoChannel", channelID);
    }

    public List<String> getTempChannel() {
        return getChannelList("TempChannel");
    }

    public void addTempChannel(String channelID) throws IOException {
        addChannelList("TempChannel", channelID);
    }

    public void removeTempChannel(String channelID) throws IOException {
        removeChannelList("TempChannel", channelID);
    }

    public void writeChannelLog(String message) {
        BufferedWriter buff = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY.MM.dd - HH:mm:ss");
        try {
            buff = new BufferedWriter(new FileWriter( fChannelLog.getAbsolutePath(), fChannelLog.exists()));
            buff.write("[" + simpleDateFormat.format(new Date()) + "] " + message);
            buff.newLine();
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<String> getChannelList(String typ) {
        String channelString = fmConfig.get(typ);
        List<String> channelList = new ArrayList<>();
        if (channelString != null && channelString.contains(",")) {
            String[] teil = channelString.split(",");
            for (String s: teil) {
                channelList.add(s);
            }
            return channelList;
        } else if (channelString != null && channelString.length() > 1) {
            channelList.add(channelString);
            return channelList;
        } else {
            return new ArrayList<>();
        }
    }

    private void addChannelList(String typ, String channelID) throws IOException {
        List<String> channelList = getChannelList(typ);

        channelList.add(channelID);

        fmConfig.set(typ, listToString(channelList));
        fmConfig.saveFile();
    }

    private void removeChannelList(String typ, String channelID) throws IOException {
        List<String> channelList = getChannelList(typ);

        channelList.remove(channelID);

        fmConfig.set(typ, listToString(channelList));
        fmConfig.saveFile();
    }

    private String listToString(List<String> channelList) {
        StringBuilder builder = new StringBuilder();
        for(String s: channelList) {
            builder.append(s + ",");
        }
        String s = builder.toString();
        if (s.length() > 1) {
            s.substring(0, s.length() - 1);
        }

        return s;
    }

}
