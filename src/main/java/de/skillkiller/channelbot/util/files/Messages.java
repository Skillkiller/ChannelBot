package de.skillkiller.channelbot.util.files;

import java.io.File;

/**
 * Created by Skillkiller on 28.06.2017.
 */
public class Messages {

    public Messages(String guildID) {
        messageFile = new GuildFiles(guildID).getConfigFile();


    }

    private File messageFile;
    private String keineRechte,autochannelBereits,autochannelKein,autochannelHinzugefügt,autochannelGelöscht,autochannelNichtImVoice;

    public String getKeineRechte() {
        return keineRechte;
    }

    public String getAutochannelBereits() {
        return autochannelBereits;
    }

    public String getAutochannelKein() {
        return autochannelKein;
    }

    public String getAutochannelHinzugefügt() {
        return autochannelHinzugefügt;
    }

    public String getAutochannelGelöscht() {
        return autochannelGelöscht;
    }

    public String getAutochannelNichtImVoice() {
        return autochannelNichtImVoice;
    }

}
