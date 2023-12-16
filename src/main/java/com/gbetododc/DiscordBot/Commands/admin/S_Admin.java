package com.gbetododc.DiscordBot.Commands.admin;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.relation.Role;
import javax.management.relation.RoleList;

import com.gbetododc.DiscordBot.Roles;
import com.gbetododc.System.Json;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

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
                    case "removeall":
                        if (event.getOption("confirme").getAsBoolean()) {
                            List<net.dv8tion.jda.api.entities.Role> allServerRoles = event.getJDA().getGuildById(GUILDID).getRoles();
                            for (net.dv8tion.jda.api.entities.Role role : allServerRoles) {
                                String rolename = role.getName();
                                if (role.getIdLong() != 1173317439761678339L) { // When it is not he Bot ID
                                    if (role.getIdLong() != 1173303775407116288L) { // When it is not the @everyone ID
                                        try {
                                            role.delete().queue(
                                                success -> {
                                                    Logger.log("S_Admin - roles removeall", "Successfully deleted Role: " + rolename, LogLvl.normale);

                                                    if (event.getJDA().getGuildById(GUILDID).getRoles().size() <= 2) {
                                                        Json.clearCourseList();
                                                        Logger.log("S_Admin - roles removeall", "Successfully removed ALL roles. Hope it wasn't a mistake.", LogLvl.critical);
                                                    }
                                                }
                                            );
                                        } catch (Error error) {
                                            Logger.log("S_Admin - roles removeall", error.toString(), LogLvl.moderate);
                                        }
                                    } else if (role.getIdLong() == 1173303775407116288L) {
                                        Logger.log("S_Admin - removeall", "Can't remove the role of @everybody", LogLvl.moderate);
                                    }
                                } else if (role.getIdLong() == 1173317439761678339L) {
                                    Logger.log("S_Admin - removeall", "Can't remove the role of the Bot", LogLvl.moderate);
                                }
                            }
                            Logger.log("S_Admin - removeall", "Queued all roles to be removed", LogLvl.normale);
                            event.reply("Queued all roles to be removed").queue();
                        } else if (!event.getOption("confirme").getAsBoolean()) {
                            Logger.log("S_Admin - roles removeall", event.getUser().getName() + " tried to remove all roles, but denied it.", LogLvl.normale);
                            event.reply("You did not agree to remove all roles. Try again and select 'YES' under confirm").queue();
                        }
                        break;
                    case "removeallcourses":
                        if (event.getOption("confirme").getAsBoolean()) {
                            Map<String, Map<String, Long>> coursemap = Json.getcoursemap();
                            for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
                                Map<String, Long> CourseIDMap = entry.getValue();
                                for (Map.Entry<String, Long> entry2 : CourseIDMap.entrySet()) {
                                    event.getJDA().getGuildById(GUILDID)
                                        .getRoleById(
                                            entry2.getValue()
                                        )
                                        .delete()
                                        .queue(
                                            success -> {
                                                Logger.log("S_Admin - roles removeallcourses", "Successfully deleted Role: " + entry2.getValue(), LogLvl.normale);
                                            }
                                        );
                                }
                            }
                            Json.clearCourseList();
                            Logger.log("S_Admin - roles removeallcourses", "Successfully removed all courseroles.", LogLvl.critical);
                            event.reply("Successfully removed all courseroles.").queue();
                        } else if (!event.getOption("confirme").getAsBoolean()) {
                            Logger.log("S_Admin - roles removeallcourses", event.getUser().getName() + " tried to remove all courseroles, but denied it.", LogLvl.normale);
                            event.reply("You did not agree to remove all courseroles. Try again and select 'YES' under confirm").queue();
                        }


                        break;
                    }

        
            case "bot":
                break;
        }


    }
}
