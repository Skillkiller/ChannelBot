package de.skillkiller.channelbot.commands;

import de.skillkiller.channelbot.core.Main;
import de.skillkiller.channelbot.util.ServerConfig;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color; // Changed from java.awt.* -> java.awt.Color to implement java.utils.List
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Skillkiller on 26.06.2017.
 */
public class AutoChannel implements Command {

    private void sendHelp(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.orange);
        embedBuilder.addField("Verwendung!", "``" + Main.commandPrefix + "autochannel [add|remove|list]``", false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws ParseException, IOException {

        Member member = event.getMember();
        Guild guild = event.getGuild();
        TextChannel chan = event.getChannel();
        ServerConfig guildConfig = new ServerConfig(event.getGuild().getId());

        if(args.length == 0) {
            sendHelp(event);
            return;
        }


        switch (args[0].toLowerCase()) {

            case "add":

                if(guild.getOwner().getUser().getId().equals(member.getUser().getId())) {
                    if(member.getVoiceState().inVoiceChannel()) {
                        if(guildConfig.getAutoChannel() != null && guildConfig.getAutoChannel().contains(member.getVoiceState().getChannel().getId())) {
                            chan.sendMessage("Dies ist bereits ein AutoChannel").queue();
                        } else {
                            guildConfig.addAutoChannel(member.getVoiceState().getChannel().getId());
                            chan.sendMessage("Hinzugefügt!").queue();
                        }
                    }else {
                        chan.sendMessage("Du befindest dich in keinem Voice Channel").queue();
                    }
                } else {
                    chan.sendMessage("Du bist dafür nicht berechtigt!").queue();
                }
                break;


            case "remove":

                if (args.length == 1) {
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.orange);
                    embedBuilder.addField("Verwendung!", "``" + Main.commandPrefix + "autochannel remove [ChannelID]`` oder\n" +
                                    "``" + Main.commandPrefix + "autochannel remove [ChannelName]``", false);
                    chan.sendMessage(embedBuilder.build()).queue();
                } else if (args.length == 2){
                    if(guild.getOwner().getUser().getId().equals(member.getUser().getId())) {


                        /*
                            Sucht nach dem ersten voice channel, der mit dem angegebenen namen anfängt und nimmt
                            von diesem channel die ID. Wenn das auf keinen channel zutrifft wird die eingabe als
                            ID interpretiert.
                            Bsp:
                            Autochannel Name:   "General"
                            Command:            ".autochannel remove general"
                            oder:               ".autochannel remove gen"
                         */
                        String argsString = Arrays.stream(args).skip(1).collect(Collectors.joining(" ")).toLowerCase();
                        List<VoiceChannel> chans = new ArrayList<>();
                        event.getGuild().getVoiceChannels().stream()
                                .filter(c -> c.getName().toLowerCase().startsWith(argsString))
                                .forEach(chans::add);

                        String chanID = chans.size() > 0 ? chans.get(0).getId() : args[1];

                        if (guildConfig.getAutoChannel().contains(chanID)) {
                            guildConfig.removeAutoChannel(chanID);
                            chan.sendMessage("Autochannel gelöscht!").queue();
                        } else {
                            chan.sendMessage("Dies ist kein AutoChannel").queue();
                        }


                    } else {
                        chan.sendMessage("Du bist dafür nicht berechtigt!").queue();
                    }
                }
                break;


            case "list":

                if(event.getGuild().getOwner().getUser().getId().equals(member.getUser().getId())) {

                    StringBuilder builder = new StringBuilder();
                    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.orange);

                    for (String s : guildConfig.getAutoChannel()) {
                        builder.append(event.getGuild().getVoiceChannelById(s).getName() + "(" + event.getGuild().getVoiceChannelById(s).getId() + "), ");
                    }

                    String s = builder.toString();

                    if (s.length() > 1)
                        s.substring(0, s.length() - 1);

                    if(s.length() == 0)
                        s = "Keine AutoChannel vorhanden";


                    embedBuilder.addField("AutoChannel", s, false);
                    chan.sendMessage(embedBuilder.build()).queue();

                } else {
                    chan.sendMessage("Du bist dafür nicht berechtigt!").queue();
                }

                break;


            default:
                sendHelp(event);


        }

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
}
