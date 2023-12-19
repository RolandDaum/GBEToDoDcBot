package com.gbetododc.DiscordBot.Commands.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.System.Json;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class S_Admin {
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
        User eventUser = event.getUser();
        String eventOption = event.getOption("roletypes").getAsString();
        
        Logger.log("S_Admin - roles list", eventUser.getAsMention() + " executed /admin roles list roletypes:" + eventOption, LogLvl.Title);

        switch (eventOption) {
            case "all":
                role_list_all(event);
                break;
            case "courses":
                // role_list_courses(event);
                break;
        }
    }
    private static void role_list_all(SlashCommandInteractionEvent event) {
        List<Role> roles = event.getGuild().getRoles();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        // embedBuilder.setDescription("This is a test desc for the embedded reply");
        embedBuilder.setTitle("All roles");
        embedBuilder.setColor(25500);
        int roleCount = roles.size();
        int halfRoleCount = (roleCount / 2)+1;
        List<Role> firstHalfRoles = roles.subList(0, halfRoleCount);
        List<Role> secondHalfRoles = roles.subList(halfRoleCount, roleCount);
        StringBuilder firstHalfRoleNames = new StringBuilder();
        for (Role role : firstHalfRoles) {
            firstHalfRoleNames.append(role.getAsMention()).append("\n");
        }
        embedBuilder.addField("Roles", firstHalfRoleNames.toString(), true);
        StringBuilder secondHalfRoleNames = new StringBuilder();
        for (Role role : secondHalfRoles) {
            secondHalfRoleNames.append(role.getAsMention()).append("\n");
        }
        embedBuilder.addField("", secondHalfRoleNames.toString(), true);
        event.replyEmbeds(embedBuilder.build()).queue();
    }
    private static void role_list_courses(SlashCommandInteractionEvent event) {
        // TODO: Not done jet, just giving out all the courseroletypes
        Map<String, Map<String, Long>> coursemap = Json.getcoursemap();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("All roles");
        embedBuilder.setColor(25500);

        // Du kannst die Werte aus der Map in zwei Teillisten aufteilen
        List<String> roleNames = new ArrayList<>(coursemap.keySet());
        int roleCount = roleNames.size();
        int halfRoleCount = (roleCount / 2) + 1;

        List<String> firstHalfRoleNames = roleNames.subList(0, halfRoleCount);
        List<String> secondHalfRoleNames = roleNames.subList(halfRoleCount, roleCount);

        // Füge die Rollennamen der ersten Hälfte zum ersten Feld hinzu
        StringBuilder firstHalfRolesBuilder = new StringBuilder();
        for (String roleName : firstHalfRoleNames) {
            firstHalfRolesBuilder.append(roleName).append("\n");
        }
        embedBuilder.addField("Roles", firstHalfRolesBuilder.toString(), true);

        // Füge die Rollennamen der zweiten Hälfte zum zweiten Feld hinzu
        StringBuilder secondHalfRolesBuilder = new StringBuilder();
        for (String roleName : secondHalfRoleNames) {
            secondHalfRolesBuilder.append(roleName).append("\n");
        }
        embedBuilder.addField("", secondHalfRolesBuilder.toString(), true);

        event.replyEmbeds(embedBuilder.build()).queue();
    }

    private static void roles_add(SlashCommandInteractionEvent event) {
        String rolename = event.getOption("rolename", null, OptionMapping::getAsString);
        String courseroletype = event.getOption("coursetype", null, OptionMapping::getAsString);
        Integer rolecolor = event.getOption("rolecolor", null, OptionMapping::getAsInt);
        User eventUser = event.getUser();
        MessageChannelUnion eventChannel = event.getChannel();

        Logger.log("S_Admin - roles add", event.getUser().getName() + " executed /admin roles add rolename:" + rolename + " coursetype:" + courseroletype, LogLvl.Title);

        Map<String, Map<String, Long>> coursemap = Json.getcoursemap();
        Role roleOnserverifExisting = event.getGuild().getRolesByName(rolename, true).stream().findFirst().orElse(null);
        Boolean roleisInCourseMap = false;
        for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
            Map<String, Long> coursesMap = entry.getValue();
            if (roleisInCourseMap) {break;}
            for (Map.Entry<String, Long> entry2 : coursesMap.entrySet()) {
                if (entry2.getKey().equals(rolename)) {
                    roleisInCourseMap = true;
                    break;
                }
            }
        }
        
        if (!roleisInCourseMap && roleOnserverifExisting == null) {
            if (courseroletype != null) {
                event.getGuild().createRole()
                    .setName(rolename)
                    .setColor(rolecolor)
                    .queue(
                        success -> {
                            Logger.log("S_Admin - roles add", "created role: " + success.getName() + " on server", LogLvl.normale);
                            Boolean addedCoursetoJson = Json.addCourse(coursemap, courseroletype, success.getName(), success.getIdLong());
                            if (addedCoursetoJson) {
                                Logger.log("S_Admin - roles add", "added courserole '" + success.getName() + "' as coursetype '" + courseroletype + "' with the color '" + rolecolor + "'", LogLvl.normale);
                                eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " created " + success.getAsMention()).queue();
                            } else {
                                Logger.log("S_Admin - roles add", "failed to save rolecourse '" + success.getName() + "' in courses.json under '" + courseroletype + "'; Deleting role in Discord again.", LogLvl.moderate);
                                eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed to save rolecourse; Deleting role in Discord again").queue();
                                success.delete().queue();
                            }
                        },
                        failure -> {
                            Logger.log("S_Admin - roles add", failure.getMessage(), LogLvl.moderate);
                            eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed to create courserole in Discord").queue();
                        }
                    );
                event.reply("Creating role '" + rolename + "' under the category '" + courseroletype + "' with the color '" + rolecolor + "'").queue();
            } else if (courseroletype == null) {
                if (roleOnserverifExisting == null) {
                    event.getGuild().createRole()
                        .setName(rolename)
                        .setColor(rolecolor)
                        .queue(
                            success -> {
                                Logger.log("S_Admin - roles add", "created role: " + success.getName() + " on server", LogLvl.normale);
                                eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " created " + success.getAsMention()).queue();
                            },
                            failure -> {
                                Logger.log("S_Admin - roles add", failure.getMessage(), LogLvl.moderate);
                                eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed to create role in Discord").queue();
                            }
                        ); 
                    event.reply("Creating role '" + rolename + "' with the color '" + rolecolor + "'").queue();
                } else if (roleOnserverifExisting != null) {
                    Logger.log("S_Admin - roles add", "role '" + rolename + "' already exitst on server", LogLvl.moderate);
                    event.reply(":x:   " + roleOnserverifExisting.getAsMention() + " already exist on server").queue();
                }
            }
        } else if (roleisInCourseMap || roleOnserverifExisting != null) {
            try {
                Logger.log("S_Admin - roles add", rolename + " is already exising in courses.json and/or on the server", LogLvl.moderate);
                event.reply(":x:   " + " courserole '" + rolename + "' is already existing under in courses.json and/or on the server").queue();
            } catch (Throwable error) {
                Logger.log("S_Admin - roles add", error.toString(), LogLvl.moderate);
            }
        }
    }
    private static void roles_remove(SlashCommandInteractionEvent event) {
        User eventUser = event.getUser();
        MessageChannelUnion eventChannel = event.getChannel();
        Role roleObj = event.getOption("deleterole").getAsRole();
        String rolename = roleObj.getName();
        Long roleIDtodelete = roleObj.getIdLong();
        String coursetype = event.getOption("coursetype", null, OptionMapping::getAsString);
        Map<String, Map<String, Long>> coursemap = Json.getcoursemap();

        Logger.log("S_Admin - roles remove", event.getUser().getName() + " executed /admin roles remove rolename:" + rolename + " coursetype:" + coursetype, LogLvl.Title);

        if (roleIDtodelete == 1173317439761678339L || roleIDtodelete == 1173303775407116288L) {
            event.reply(":x:   you can't delete the role of the Bot or [@]everyone").queue();
            Logger.log("S_Admin - roles remove", "can't delete the role of the Bot or [@]everyone", LogLvl.moderate);
        } else {
            Boolean RoleisInCourseMap = false;
            for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
                Map<String, Long> coursesMap = entry.getValue();
                if (RoleisInCourseMap) {break;}
                for (Map.Entry<String, Long> entry2 : coursesMap.entrySet()) {
                    if (entry2.getKey().equals(rolename) && entry2.getValue().longValue() == roleIDtodelete) {
                        RoleisInCourseMap = true;
                        coursetype = entry.getKey().toString();
                        break;
                    }
                }
            }
            
            if (!RoleisInCourseMap) {
                roleObj.delete().queue(
                    success -> {
                        Logger.log("S_Admin - roles remove", eventUser.getAsMention() + " removed role: " + rolename, LogLvl.normale);
                        eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " removed role from Discord").queue();
                    },
                    failure -> {
                        Logger.log("S_Admin - roles remove", failure.getMessage(), LogLvl.moderate);
                        eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed to delete role from Discord").queue();
                    }
                );
                event.reply("Queued the removal of '" + rolename + "'").queue();
            } else if (RoleisInCourseMap) {
                Boolean removedCoursefromJson = Json.removeCourse(coursemap, rolename, roleIDtodelete, coursetype);
                if (removedCoursefromJson) {
                    roleObj.delete().queue(
                        success -> {
                            Logger.log("S_Admin - roles remove", "@" + eventUser + " removed role: " + rolename + " from the server and courses.json file", LogLvl.normale);
                            eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " removed role from Discord and courses.json").queue();
                        },
                        failure -> {
                            Logger.log("S_Admin - roles remove", failure.getMessage(), LogLvl.moderate);
                            eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed to delete role from Discord").queue();
                        }
                    );
                    event.reply("Queued the removal of '" + rolename + "'").queue();
                } else {
                    event.reply(":x:   something went wrong :(; Role probably cannot be found under the coursetype '" + coursetype + "'").queue();
                }
            }
        }
    }
    private static void roles_removeall(SlashCommandInteractionEvent event) {
        MessageChannelUnion eventChannel = event.getChannel();
        User eventUser = event.getUser();
        Boolean eventOption = event.getOption("confirme").getAsBoolean();

        Logger.log("S_Admin - roles removeall", eventUser.getAsMention() + " executed /admin roles removall confirme:" + eventOption, LogLvl.Title);
        
        if (eventOption) {
            List<net.dv8tion.jda.api.entities.Role> allServerRoles = event.getGuild().getRoles();
            for (net.dv8tion.jda.api.entities.Role role : allServerRoles) {
                String rolename = role.getName();
                if (role.getIdLong() != 1173317439761678339L) { // When it is not he Bot ID
                    if (role.getIdLong() != 1173303775407116288L) { // When it is not the @everyone ID
                        role.delete().queue(
                            success -> {
                                Logger.log("S_Admin - roles removeall", "Successfully deleted Role: " + rolename, LogLvl.normale);
                                if (event.getGuild().getRoles().size() <= 2) {
                                    Json.clearCourseList();
                                    Logger.log("S_Admin - roles removeall", "Successfully removed ALL roles. Hope it wasn't a mistake.", LogLvl.critical);
                                    eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " removed all roles, hope it wasn't a mistake").queue();
                                } else {
                                    eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " there are still at least more than two undeleteable roles or roles that haven't been removed on the server").queue();
                                }
                            },
                            failure -> {
                                Logger.log("S_Admin - roles removeall", failure.getMessage(), LogLvl.moderate);
                            }
                        );
                    }
                }
            }
            Logger.log("S_Admin - removeall", "Queued all roles to be removed", LogLvl.normale);
            event.reply("Queued all roles to be removed").queue();
        } else if (!eventOption) {
            Logger.log("S_Admin - roles removeall", event.getUser().getName() + " tried to remove all roles, but denied it.", LogLvl.normale);
            event.reply(":x:   You did not agree to remove all roles. Try again and select 'YES' under confirm").queue();
        }
    }
    private static void roles_removeallcourses(SlashCommandInteractionEvent event) {
        User eventUser = event.getUser();
        Boolean eventOption = event.getOption("confirme").getAsBoolean();
        MessageChannelUnion eventChannel = event.getChannel();
        Map<String, Map<String, Long>> coursemap = Json.getcoursemap();

        Logger.log("S_Admin - roles removeallcourses", eventUser.getAsMention() + " executed /admin roles removeallcourses confirme:" + event.getOption("confirme").getAsBoolean(), LogLvl.Title);


        if (eventOption) {
            AtomicInteger allCourseRoles = new AtomicInteger(0);
            for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
                allCourseRoles.addAndGet(entry.getValue().size());
            }

            System.out.println(allCourseRoles);
            for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
                Map<String, Long> CoursesMap = entry.getValue();
                for (Map.Entry<String, Long> entry2 : CoursesMap.entrySet()) {
                    event.getGuild()
                        .getRoleById(
                            entry2.getValue()
                        )
                        .delete()
                        .queue(
                            success -> {
                                allCourseRoles.decrementAndGet();
                                Logger.log("S_Admin - roles removeallcourses", "Successfully deleted Role: " + entry2.getValue(), LogLvl.normale);
                                if (allCourseRoles.get() == 0) {
                                    Logger.log("S_Admin - roles removeall", "all courseroles have been removed, hope it wasn't a mistake", LogLvl.critical);
                                    eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " removed all courseroles, hope it wasn't a mistake").queue();
                                }
                            }, 
                            failure -> {
                                Logger.log("S_Admin - roles removeall", failure.getMessage(), LogLvl.moderate);
                            }
                        );
                }
            }
            Json.clearCourseList();
            Logger.log("S_Admin - roles removeallcourses", "queued all courseroles to be removed", LogLvl.moderate);
            event.reply("Queued all courseroles to be removed").queue();
        } else if (!eventOption) {
            Logger.log("S_Admin - roles removeallcourses", event.getUser().getName() + " tried to queue remove all courseroles, but denied it", LogLvl.critical);
            event.reply("You did not agree to remove all courseroles. Try again and select 'True' under confirm").queue();
        }

    }

    private static void bot_shutdown(SlashCommandInteractionEvent event) {
        User eventUser = event.getUser();
        Boolean eventOption = event.getOption("confirme").getAsBoolean();

        Logger.log("S_Admin - bot", eventUser.getAsMention() + " executed /admin bot shutdown confirme:" + eventOption, LogLvl.Title);

        if (eventOption) {
            Logger.log("Shutting Bot down ...", eventUser.getAsMention() + " is shutting down the bot", LogLvl.critical); 
            DiscordBot.JDA.shutdown();
        } else  if (!eventOption) {
            Logger.log("S_Admin - bot shutdown", eventUser.getAsMention() + "tried to shut down the bot", LogLvl.moderate);
        }
    }
}