package de.skillkiller.channelbot.util.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Skillkiller on 29.06.2017.
 */
public class Log {

    public Log(String guildID) {
        String workingDir = new GuildFiles(guildID).getWorkingDir();

        channelLog = new File(workingDir + "/ChannelLog.txt");

        if (!channelLog.exists()) {
            try {
                new File(channelLog.getAbsolutePath().replace(channelLog.getName(), "")).mkdirs();
                channelLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private File channelLog;

    public void writeChannelLog(String message) {
        BufferedWriter buff = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY.MM.dd - HH:mm:ss");
        try {
            buff = new BufferedWriter(new FileWriter( channelLog.getAbsolutePath(), channelLog.exists()));
            buff.write("[" + simpleDateFormat.format(new Date()) + "] " + message);
            buff.newLine();
            buff.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
