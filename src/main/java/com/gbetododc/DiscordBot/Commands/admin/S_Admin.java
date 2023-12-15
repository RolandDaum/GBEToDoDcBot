package com.gbetododc.DiscordBot.Commands.admin;

import com.gbetododc.DiscordBot.Roles;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class S_Admin {
    
    static Dotenv dotenv = Dotenv.configure().load();
    static String GUILDID = dotenv.get("GUILDID");

    public static void main(SlashCommandInteractionEvent event) {

        switch (event.getSubcommandGroup()) {

            case "roles":

                switch (event.getSubcommandName()) {

                    case "add":
                        Logger.log("S_Admin - roles add", "Started rollcreation process", LogLvl.normale);
                        Roles.createRole(
                            event.getJDA().getGuildById(GUILDID), 
                            event.getOption("rolename", null, OptionMapping::getAsString), 
                            event.getOption("coursetype", null, OptionMapping::getAsString), 
                            event.getOption("rolecolor", null, OptionMapping::getAsInt)         // Was machen, wenn null aka nicht eingegeben wurde?
                        );
                        event.reply("Finished rolecreation process successfull or not idk. I'm just a bot").queue();
                        break;

                    case "remove":
                        Logger.log("S_Admin - roles remove", "Started rollcreation process", LogLvl.normale);
                        Roles.deleteRole(
                            event.getOption("delete")
                                .getAsRole(), 
                            event.getOption("coursetype", null, OptionMapping::getAsString)
                        );
                        event.reply("Finished roleremoval process successfully or not idk. I'm just a bot.").queue();
                        break;
                }

        
            case "bot":
                break;
        }


    }
}
