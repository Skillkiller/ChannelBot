package de.skillkiller.channelbot.commands;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;


/**
 * Created by Sebastian on 07.05.2017.
 */
public class CommandHandler {

    public static final CommandParser parse = new CommandParser();
    public static HashMap<String,Command>commands = new HashMap<>();

    public static void handleCommand(CommandParser.CommandContainer cmd) throws ParseException {
        if(commands.containsKey(cmd.invoke)){
            boolean safe = commands.get(cmd.invoke).called(cmd.args,cmd.event);
            if(!safe){
                try {
                    commands.get(cmd.invoke).action(cmd.args,cmd.event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                commands.get(cmd.invoke).executed(safe,cmd.event);
            }else{
                commands.get(cmd.invoke).executed(safe,cmd.event);
            }
        }
    }

}
