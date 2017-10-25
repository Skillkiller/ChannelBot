package de.skillkiller.channelbot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Skillkiller on 25.06.2017.
 */
public class FileManager {


    private String fileName;
    private Properties p;
    private File f;

    public FileManager(String dir, String fileName) {
        //Erstelle Verzeichnisse
        File userDir = null;
        if (!dir.startsWith(System.getProperty("user.dir"))) {
            userDir = new File(System.getProperty("user.dir") + "/ChannelBot/" + dir);
        } else {
            userDir = new File(dir);
        }

        userDir.mkdirs();

        this.f = new File(userDir.getAbsolutePath() + "/" + fileName);
        this.p = new Properties();
    }

    public void loadFile() throws IOException {

        if (!this.f.exists()) {
            this.f.createNewFile();
        }

        p.load(new FileInputStream(this.f));
    }

    public String get(String key) {
        return p.getProperty(key);
    }

    public String get(String key, String defaultvalue) {
        return p.getProperty(key, defaultvalue);
    }

    public void set(String key, String value) {
        p.setProperty(key, value);
    }

    public Properties getProperties() {
        return p;
    }

    public void saveFile() throws IOException {
        p.store(new FileOutputStream(f), f.getName());
    }

    public File getFile() {
        return f;
    }


}