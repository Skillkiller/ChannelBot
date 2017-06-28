package de.skillkiller.channelbot.commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;

/**
 * Created by Skillkiller on 26.06.2017.
 */
public class UploadLog implements Command {
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws ParseException, IOException {
        /*
        Config guildConfig = new Config(event.getGuild().getId());
        if(!event.getGuild().getOwner().getUser().getId().equals(event.getAuthor().getId())) {
            event.getChannel().sendMessage("Du bist nicht berechtigt!").queue();
            return;
        }
        if (guildConfig.getPastebinDevKey() == null) {
            event.getChannel().sendMessage("PastebinDev Key fehlt!").queue();
            return;
        }
        if (args.length == 0) {
            //TODO Sende Hilfe
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("channellog")) {
                if (guildConfig.getFileChannelLog().length() / (1024*1024) > 0.5) {
                    event.getChannel().sendMessage("Log zu groß für einen Upload!").queue();
                } else {
                    BufferedReader br = new BufferedReader(new FileReader(guildConfig.getFileChannelLog().getAbsolutePath()));
                    try {
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append(System.lineSeparator());
                            line = br.readLine();
                        }
                        String everything = sb.toString();
                        Message message = event.getChannel().sendMessage("Upload läuf...").complete();
                        message.editMessage(sendtoPastebin(guildConfig.getPastebinDevKey(), event.getGuild().getName() + " ChannelLog", everything)).queue();
                    } finally {
                        br.close();
                    }


                }

            } else if (args[0].equalsIgnoreCase("config")) {
                if (guildConfig.getFileConfig().length() / (1024*1024) > 0.5) {
                    event.getChannel().sendMessage("Log zu groß für einen Upload!").queue();
                } else {
                    BufferedReader br = new BufferedReader(new FileReader(guildConfig.getFileConfig().getAbsolutePath()));
                    try {
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append(System.lineSeparator());
                            line = br.readLine();
                        }
                        String everything = sb.toString();
                        Message message = event.getChannel().sendMessage("Upload läuf...").complete();
                        message.editMessage(sendtoPastebin(guildConfig.getPastebinDevKey(), event.getGuild().getName() + " Config", everything)).queue();
                    } finally {
                        br.close();
                    }


                }
            }
        }*/
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String commandType() {
        return null;
    }

    private String sendtoPastebin(String PastebinDevkey, String name, String code) throws IOException {
        String body = "api_dev_key=" + URLEncoder.encode( PastebinDevkey, "UTF-8" ) + "&" +
                "api_option=" + URLEncoder.encode( "paste", "UTF-8" ) + "&" +
                "api_paste_name=" + URLEncoder.encode( name, "UTF-8" ) + "&" +
                "api_paste_private=" + URLEncoder.encode( "1", "UTF-8" ) + "&" +
                "api_paste_expire_date=" + URLEncoder.encode("10M", "UTF-8") + "&" +
                "api_paste_code=" + URLEncoder.encode(code,"UTF-8");

        URL url = new URL( "https://pastebin.com/api/api_post.php" );
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod( "POST" );
        connection.setDoInput( true );
        connection.setDoOutput( true );
        connection.setUseCaches( false );
        connection.setRequestProperty( "Content-Length", String.valueOf(body.length()) );

        OutputStreamWriter writer = new OutputStreamWriter( connection.getOutputStream() );

        writer.write( body );
        writer.flush();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()) );

        StringBuilder builder = new StringBuilder();
        for ( String line; (line = reader.readLine()) != null; )
        {
            builder.append(line);
            //System.out.println(line);
        }

        writer.close();
        reader.close();
        return builder.toString();
    }
}
