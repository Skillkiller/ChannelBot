package de.skillkiller.channelbot.util.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Skillkiller on 28.06.2017.
 */
public class Config {

    public Config(String guildID) {
        configFile = new GuildFiles(guildID).getConfigFile();

        this.fmConfig = new FileManager(configFile);

        if(fmConfig.get("commandPrefix") == null) {
            fmConfig.set("commandPrefix", "/");
        }

        if(fmConfig.get("PastebinApiKey") == null) {
            fmConfig.set("PastebinApiKey", "Key angeben");
        }

        //Speichere Datei
        try {
            fmConfig.saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File configFile;
    private FileManager fmConfig;

    public List<String> getAutoChannel() {
        return getChannelList("AutoChannel");
    }

    public void addAutoChannel(String channelID) throws IOException {
        addChannelList("AutoChannel", channelID);
    }

    public String getCommandPrefix() {
        return fmConfig.get("commandPrefix");
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

    public String getPastebinDevKey() {
        String s = null;
        try {
            s = fmConfig.getSet("PastebinApiKey", "Key angeben");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (s != null && s.equalsIgnoreCase("Key angeben")) {
            return null;
        } else {
            return  s;
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
