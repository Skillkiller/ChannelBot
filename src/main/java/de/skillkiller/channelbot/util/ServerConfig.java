package de.skillkiller.channelbot.util;

import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class ServerConfig {

    public ServerConfig(String guildID) {
        fm = new FileManager("/guilds/" + guildID + "/", "config.txt");
        try {
            fm.loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.guildID = guildID;
    }

    private String guildID;
    private FileManager fm;

    public FileManager getServerConfig() {
        return fm;
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

    private List<String> getChannelList(String typ) {
        String channelString = fm.get(typ);
        List<String> channelList = new ArrayList<>();
        if (channelString != null && channelString.contains(",")) {
            String[] teil = channelString.split(",");
            for (String s: teil) {
                channelList.add(s);
            }
            return channelList;
        } else {
            return null;
        }
    }

    private void addChannelList(String typ, String channelID) throws IOException {
        List<String> channelList = getChannelList(typ);

        channelList.add(channelID);

        fm.set(typ, listToString(channelList));
        fm.saveFile();
    }

    private void removeChannelList(String typ, String channelID) throws IOException {
        List<String> channelList = getChannelList(typ);

        channelList.remove(channelID);

        fm.set(typ, listToString(channelList));
        fm.saveFile();
    }

    private String listToString(List<String> channelList) {
        StringBuilder builder = new StringBuilder();
        for(String s: channelList) {
            builder.append(s + ",");
        }

        return builder.toString().substring(0, builder.toString().length() - 1);
    }

}
