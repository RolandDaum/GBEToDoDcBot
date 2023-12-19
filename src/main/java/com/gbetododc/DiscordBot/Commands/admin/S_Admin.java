package com.gbetododc.DiscordBot.Commands.admin;

import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;
import javax.swing.text.html.Option;

import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.DiscordBot.Roles;
import com.gbetododc.System.Json;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class S_Admin {
    
    static Dotenv dotenv = Dotenv.configure().load();
    static String GUILDID = dotenv.get("GUILDID");

    public static void main(SlashCommandInteractionEvent event) {
        String subCommandGroupName = event.getSubcommandGroup();
        String subCommandName = event.getSubcommandName();
        switch (subCommandGroupName) {
            case "roles":
                switch (subCommandName) {
                    case "list":
                        roles_list(event);
                        break;
                    case "add":
                        roles_add(event);
                        break;
                    case "remove":
                        roles_remove(event);
                        break;                
                    case "removeall":
                        roles_removeall(event);
                        break;
                    case "removeallcourses":
                        roles_removeallcourses(event);
                        break;
                    }        
                    break;
            case "bot":
                switch (event.getSubcommandName()) {
                    case "token":
                        break;
                    case "changeseverid":
                        break;
                    case "shutdown":
                        bot_shutdown(event);
                        break;
                }
                break;
            case "msapi":
                switch (event.getSubcommandName()) {
                    case "token":
                        break;
                    case "refreshtoken":
                        break;
                    case "clientsecret":
                        break;
                }
                break;
        }
    }

    private static void roles_list(SlashCommandInteractionEvent event) {
        switch (event.getOption("roletypes").getAsString()) {
            case "all":
                Logger.log("S_Admin - roles list", event.getUser().getName() + " executed /admin roles list roletypes:all", LogLvl.Title);
                event.reply("listing all roles").queue();
                break;
            case "courses":
                Logger.log("S_Admin - roles list", event.getUser().getName() + " executed /admin roles list roletypes:allcourseroles", LogLvl.Title);
                event.reply("listing all courseroles").queue();
                break;
        }
    }
    private static void roles_add(SlashCommandInteractionEvent event) {

        String rolename = event.getOption("rolename", null, OptionMapping::getAsString);
        String courseroletype = event.getOption("coursetype", null, OptionMapping::getAsString);
        Integer rolecolor = event.getOption("rolecolor", null, OptionMapping::getAsInt);

        if (courseroletype != null) {
            Map<String, Map<String, Long>> coursemap = Json.getcoursemap();
            System.out.println(coursemap);
            Boolean roleIsinCourseMap = coursemap.get(courseroletype).containsKey(rolename);
            System.out.println(roleIsinCourseMap);
            if (!roleIsinCourseMap) {
                try {
                    event.getJDA().getGuildById(DiscordBot.GUILDID).createRole()
                        .setName(rolename)
                        .setColor(rolecolor)
                        .queue(
                            success -> {
                                Logger.log("S_Admin - roles add", "created role: " + success.getName() + " on server", LogLvl.normale);

                                Boolean addedCoursetoJson = Json.addCourse(coursemap, courseroletype, success.getName(), success.getIdLong());
                                if (addedCoursetoJson) {
                                    Logger.log("S_Admin - roles add", "added role: " + success.getName() + " as coursetype: " + courseroletype, LogLvl.normale);
                                }
                            }
                        );
                } catch (Throwable error) {
                    Logger.log("S_Admin - roles add", error.toString(), LogLvl.moderate);
                }

            }

        } else if (courseroletype == null) {
            try {
                event.getJDA().getGuildById(DiscordBot.GUILDID).createRole()
                    .setName(rolename)
                    .setColor(rolecolor)
                    .queue(
                        success -> {
                            Logger.log("S_Admin - roles add", "created role: " + success.getName() + " on server", LogLvl.normale);
                        }
                    );                
            } catch (Throwable error) {
                Logger.log("S_Admin - roles add", error.toString(), LogLvl.moderate);
            }
        }

        // Logger.log("S_Admin - roles add", event.getUser().getName() + " executed /admin roles add rolename:" + event.getOption("rolename", null, OptionMapping::getAsString) + " coursetype:" + event.getOption("coursetype", null, OptionMapping::getAsString) + " rolecolor:" + event.getOption("rolecolor", null, OptionMapping::getAsInt), LogLvl.Title);
        // Roles.createRole(
        //     event.getJDA().getGuildById(GUILDID), 
        //     event.getOption("rolename", null, OptionMapping::getAsString), 
        //     event.getOption("coursetype", null, OptionMapping::getAsString), 
        //     event.getOption("rolecolor", null, OptionMapping::getAsInt)         // Was machen, wenn null aka nicht eingegeben wurde?
        // );
        // event.reply("Finished rolecreation process successfull or not idk. I'm just a bot").queue();
    }
    private static void roles_remove(SlashCommandInteractionEvent event) {
        String eventTrigger = event.getUser().getName();
        Role roleObj = event.getOption("deleterole").getAsRole();
        String rolename = roleObj.getName();
        Long roleIDtodelete = roleObj.getIdLong();
        String coursetype = event.getOption("coursetype", null, OptionMapping::getAsString);

        Logger.log("S_Admin - roles remove", event.getUser().getName() + " executed /admin roles remove rolename:" + rolename + " coursetype:" + coursetype, LogLvl.Title);

        if (roleIDtodelete == 1173317439761678339L || roleIDtodelete == 1173303775407116288L) {
            event.reply("You Can't delete the role of the Bot or [@]everyone").queue();
            Logger.log("S_Admin - roles remove", "Can't delete the role of the Bot or [@]everyone", LogLvl.moderate);
        } else {
            if (coursetype == null) {
                try {
                    roleObj.delete().queue(
                        success -> {
                            Logger.log("S_Admin - roles remove", "@" + eventTrigger + " removed role: " + rolename, LogLvl.normale);
                        }
                    );
                    event.reply("Queued the removal of " + rolename).queue();
                } catch (Throwable error) {
                    Logger.log("S_Admin - roles remove", error.toString(), LogLvl.moderate);
                }
            } else if (coursetype != null) {
                Map<String, Map<String, Long>> coursemap = Json.getcoursemap();
                if (Json.removeCourse(coursemap, rolename, roleIDtodelete, coursetype)) {
                    try {
                        roleObj.delete().queue(
                            success -> {
                                Logger.log("S_Admin - roles remove", "@" + eventTrigger + " removed role: " + rolename + " from the server and courses.json file", LogLvl.normale);
                            }
                        );
                    } catch (Throwable error) {
                        Logger.log("S_Admin - roles remove", error.toString(), LogLvl.moderate);
                    }
                    event.reply("Queued the removal of " + rolename).queue();
                } else {
                    event.reply("Something went wrong :(; Role probably cannot be found under the course type: " + coursetype).queue();
                }
            }
        }

        // Logger.log("S_Admin - roles remove", "Started rollcreation process", LogLvl.normale);
        // Roles.deleteRole(
        //     event.getOption("delete")
        //         .getAsRole(), 
        //     event.getOption("coursetype", null, OptionMapping::getAsString)
        // );
        // event.reply("Finished roleremoval process successfully or not idk. I'm just a bot.").queue();
    }
    private static void roles_removeall(SlashCommandInteractionEvent event) {
        Logger.log("S_Admin - roles removeall", event.getUser().getName() + " executed /admin roles removell confirme:" + event.getOption("confirme").getAsBoolean(), LogLvl.Title);
        
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
    }
    private static void roles_removeallcourses(SlashCommandInteractionEvent event) {
        Logger.log("S_Admin - roles removeallcourses", event.getUser().getName() + " executed /admin roles removeallcourses confirme:" + event.getOption("confirme").getAsBoolean(), LogLvl.Title);

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
            Logger.log("S_Admin - roles removeallcourses", "Successfully queued to remove all courseroles", LogLvl.critical);
            event.reply("Successfully queued to remove all courseroles").queue();
        } else if (!event.getOption("confirme").getAsBoolean()) {
            Logger.log("S_Admin - roles removeallcourses", event.getUser().getName() + " tried to remove all courseroles, but denied it", LogLvl.critical);
            event.reply("You did not agree to remove all courseroles. Try again and select 'YES' under confirm").queue();
        }

    }

    private static void bot_shutdown(SlashCommandInteractionEvent event) {
        Logger.log("S_Admin - bot", event.getUser().getName() + " executed /admin bot shutdown confirme:" + event.getOption("confirme").getAsBoolean(), LogLvl.Title);

        if (event.getOption("confirme").getAsBoolean()) {
            Logger.log("Shutting Bot down ...", event.getUser().getName() + " is shutting down the bot", LogLvl.Title); 
            DiscordBot.JDA.shutdown();
        } else {
            Logger.log("S_Admin - bot shutdown", event.getUser().getAsMention() + "tried to shut down the bot", LogLvl.moderate);
        }
    }
}