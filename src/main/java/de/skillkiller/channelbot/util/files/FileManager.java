package de.skillkiller.channelbot.util.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Skillkiller on 28.06.2017.
 */
public class FileManager {

    private String fileName;
    private Properties p;
    private File f;

    public FileManager(File file) {

        if (!file.exists()) {
            try {
                new File(file.getAbsolutePath().replace(file.getName(), "")).mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.fileName = file.getName();
        this.f = file;
        this.p = new Properties();

        try {
            p.load(new FileInputStream(this.f));
        } catch (IOException e) {
            e.printStackTrace();
        }

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


}
