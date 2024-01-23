package com.gbetododc.DiscordBot.Commands.admin;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.DiscordBot.Commands.register.S_Register_Setup;
import com.gbetododc.DiscordBot.Notification.HomeworkNotification;
import com.gbetododc.MSAuthGraph.MsAuth;
import com.gbetododc.MSAuthGraph.MsGraph;
import com.gbetododc.System.CJson;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

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
                    case "updatecommands":
                        bot_updatecommands(event);
                        break;
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
                    case "refreshtoken":
                        msapi_refreshtoken(event);
                        break;
                    case "reauthorize":
                        msapi_reauthorize(event);
                        break;
                    case "refreshtodo":
                        msapi_refreshtodo(event);
                        break;
                    case "ctimehwnot":
                        msapi_ctimehwnot(event);
                        break;
                }
                break;
        }
    }

    private static void roles_list(SlashCommandInteractionEvent event) {
        User eventUser = event.getUser();
        String eventOption = event.getOption("roletypes").getAsString();
        
        Logger.log("S_Admin - roles list", eventUser.getName().toString() + " executed /admin roles list roletypes:" + eventOption, LogLvl.command);

        switch (eventOption) {
            case "all":
                roles_list_all(event);
                break;
            case "courses":
                roles_list_courses(event);     
                break;
        }
    }
    private static void roles_list_all(SlashCommandInteractionEvent event) {
        List<Role> roles = event.getGuild().getRoles();
        String roleNames = "";
        for (Role role : roles) {roleNames += "\n" + role.getAsMention();}
        event.replyEmbeds(
            new EmbedBuilder()
                .setTitle("All roles")
                .setColor(25500)
                .addField("---", roleNames, true)
                .build()
        ).queue();
    }
    private static void roles_list_courses(SlashCommandInteractionEvent event) {
        Map<String, Map<String, Long>> coursemap = CJson.getcoursemap();
        String roleNames = "";
        for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
            Map<String, Long> CoursesMap = entry.getValue();
            for (Map.Entry<String, Long> entry2 : CoursesMap.entrySet()) {
                roleNames += "\n<@&" + entry2.getValue().toString() + ">";
            }
        }
        event.replyEmbeds(
            new EmbedBuilder()
                .setTitle("All courseroles")
                .setColor(25500)
                .addField("---", roleNames, true)
                .build()
        ).queue();
    }
    private static void roles_add(SlashCommandInteractionEvent event) {
        String rolename = event.getOption("rolename", null, OptionMapping::getAsString);
        String courseroletype = event.getOption("coursetype", null, OptionMapping::getAsString);
        Integer rolecolor = event.getOption("rolecolor", null, OptionMapping::getAsInt);
        User eventUser = event.getUser();
        MessageChannelUnion eventChannel = event.getChannel();

        Logger.log("S_Admin - roles add", event.getUser().getName() + " executed /admin roles add rolename:" + rolename + " coursetype:" + courseroletype, LogLvl.command);

        Map<String, Map<String, Long>> coursemap = CJson.getcoursemap();
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
                            Boolean addedCoursetoJson = CJson.addCourse(coursemap, courseroletype, success.getName(), success.getIdLong());
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
        Map<String, Map<String, Long>> coursemap = CJson.getcoursemap();

        Logger.log("S_Admin - roles remove", event.getUser().getName() + " executed /admin roles remove rolename:" + rolename + " coursetype:" + coursetype, LogLvl.command);

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
                        Logger.log("S_Admin - roles remove", eventUser.getName() + " removed role: " + rolename, LogLvl.normale);
                        eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " removed role from Discord").queue();
                    },
                    failure -> {
                        Logger.log("S_Admin - roles remove", failure.getMessage(), LogLvl.moderate);
                        eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed to delete role from Discord").queue();
                    }
                );
                event.reply("Queued the removal of '" + rolename + "'").queue();
            } else if (RoleisInCourseMap) {
                Boolean removedCoursefromJson = CJson.removeCourse(coursemap, rolename, roleIDtodelete, coursetype);
                if (removedCoursefromJson) {
                    roleObj.delete().queue(
                        success -> {
                            Logger.log("S_Admin - roles remove", "@" + eventUser.getName() + " removed role: " + rolename + " from the server and courses.json file", LogLvl.normale);
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

        Logger.log("S_Admin - roles removeall", eventUser.getName() + " executed /admin roles removall confirme:" + eventOption, LogLvl.command);
        
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
                                    CJson.clearCourseList();
                                    Logger.log("S_Admin - roles removeall", "Successfully removed ALL roles. Hope it wasn't a mistake.", LogLvl.critical);
                                    eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " removed all roles, hope it wasn't a mistake").queue();
                                } else {
                                    // eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " there are still at least more than two undeleteable roles or roles that haven't been removed on the server").queue();
                                }
                            },
                            failure -> {
                                Logger.log("S_Admin - roles removeall", failure.getMessage(), LogLvl.moderate);
                            }
                        );
                    }
                }
            }
            Logger.log("S_Admin - removeall", "Queued all roles to be removed", LogLvl.moderate);
            event.reply("Queued all roles to be removed").queue();
        } else if (!eventOption) {
            Logger.log("S_Admin - roles removeall", event.getUser().getName() + " tried to remove all roles, but denied it.", LogLvl.moderate);
            event.reply(":x:   You did not agree to remove all roles. Try again and select 'True' under confirm").queue();
        }
    }
    private static void roles_removeallcourses(SlashCommandInteractionEvent event) {
        User eventUser = event.getUser();
        Boolean eventOption = event.getOption("confirme").getAsBoolean();
        MessageChannelUnion eventChannel = event.getChannel();
        Map<String, Map<String, Long>> coursemap = CJson.getcoursemap();

        Logger.log("S_Admin - roles removeallcourses", eventUser.getAsMention() + " executed '/admin roles removeallcourses confirme:" + event.getOption("confirme").getAsBoolean() + "'", LogLvl.command);

        if (eventOption) {
            AtomicInteger allCourseRoles = new AtomicInteger(0);
            for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
                allCourseRoles.addAndGet(entry.getValue().size());
            }
            for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
                Map<String, Long> CoursesMap = entry.getValue();
                for (Map.Entry<String, Long> entry2 : CoursesMap.entrySet()) {
                    event.getGuild()
                        .getRoleById(
                            entry2.getValue().longValue()
                        )
                        .delete()
                        .queue(
                            success -> {
                                allCourseRoles.decrementAndGet();
                                Logger.log("S_Admin - roles removeallcourses", "Successfully deleted Role: " + entry2.getKey(), LogLvl.normale);
                                if (allCourseRoles.get() == 0) {
                                    Logger.log("S_Admin - roles removeallcourses", eventUser.getName() + " removed all courseroles, hope it wasn't a mistake", LogLvl.critical);
                                    eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " removed all courseroles, hope it wasn't a mistake").queue();
                                }
                            }, 
                            failure -> {
                                Logger.log("S_Admin - roles removeall", failure.getMessage(), LogLvl.moderate);
                                eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " failed to remove all corseroles").queue();
                            }
                        );
                }
            }
            CJson.clearCourseList();
            Logger.log("S_Admin - roles removeallcourses", "queued all courseroles to be removed", LogLvl.moderate);
            event.reply("Queued all courseroles to be removed").queue();
        } else if (!eventOption) {
            Logger.log("S_Admin - roles removeallcourses", event.getUser().getName() + " tried to queue remove all courseroles, but denied it", LogLvl.critical);
            event.reply(":x:   You did not agree to remove all courseroles. Try again and select 'True' under confirm").queue();
        }

    }

    private static void bot_updatecommands(SlashCommandInteractionEvent event) {
        JDA jda = DiscordBot.JDA;
        User eventUser = event.getUser();
        Boolean eventOption = event.getOption("confirme").getAsBoolean();
        MessageChannelUnion eventChannel = event.getChannel();

        Logger.log("S_Admin - bot updatecommands", eventUser.getName() + " executed '/admin bot updatecommands:" + eventOption, LogLvl.command);

        if (eventOption) {
            jda.updateCommands().queue(
            success -> {
                S_Register_Setup.setup();
                S_Admin_Setup.setup();
                Logger.log("S_Admin - bot updatecommands", "successfully updated all commands", LogLvl.normale);
                eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " successfully updated commands").queue();
            }, 
            failure -> {
                Logger.log("S_Admin - bot updatecommands", "failed to update all commands", LogLvl.normale);
                eventChannel.sendMessage(":x:   " + eventUser.getAsMention().toString() + " failed to update commands").queue();
            }
            );
            event.reply("Queued command update").queue();
        } else if (!eventOption) {
            Logger.log("S_Admin - bot updatecommands", eventUser.getName() + " tried to update the commands", LogLvl.normale);
            event.reply(":x:   Your tried to update the commands. Retry with confirme:True").queue();
        }

    }   
    private static void bot_shutdown(SlashCommandInteractionEvent event) {
        User eventUser = event.getUser();
        Boolean eventOption = event.getOption("confirme").getAsBoolean();
        MessageChannelUnion eventChannel = event.getChannel();

        Logger.log("S_Admin - bot", eventUser.getName() + " executed '/admin bot shutdown confirme:" + eventOption + "'", LogLvl.command);

        if (eventOption) {
            Logger.log("Shutting Bot down ...", eventUser.getName() + " is shutting down the bot", LogLvl.critical);
            event.reply("Shutting the Bot down").queue(
                success -> {
                    try {
                        DiscordBot.JDA.shutdown();
                    } catch (Throwable e) {
                        Logger.log("S_Admin - bot shutdown", eventUser.getName() + " - failed to shout down the bot", LogLvl.moderate);
                        eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed to shut down the bot").queue();
                    }
                }
            );

        } else  if (!eventOption) {
            Logger.log("S_Admin - bot shutdown", eventUser.getName() + " did not agree to shutdown the bot", LogLvl.moderate);
            event.reply(":x:   You did not agree to shutdown the bot. Retry with confirme:True").queue();
        }
    }

    private static void msapi_reauthorize(SlashCommandInteractionEvent event) {
        MessageChannelUnion eventChannel = event.getChannel();
        User eventUser = event.getUser();
        List<OptionMapping> eventOptionList = event.getOptions();
        if (eventOptionList.isEmpty()) {
            Logger.log("S_Admin - msapi_reauthorize", eventUser.getName() + " executed '/admin msapi reauthorize' to reregister the account", LogLvl.command);
            String authurl = MsAuth.getAuthurl();
            event.reply("Klick the button down below, to register with 'gbetododc@outlook.de'")
                .addActionRow(
                    Button.link(authurl, "authenticate")
                )
                .queue();
        } else {
            // String eventOption = event.getOption("authcode").getAsString()
            Logger.log("S_Admin - msapi_reauthorize", eventUser.getName() + " executed '/admin msapi reauthorize' to enter the authcode", LogLvl.command);
            String authcode = event.getOption("authcode").getAsString();
            if (authcode != null) {
                MsAuth.tokenAuthcode(authcode, success -> {
                    if (success) {
                        Logger.log("S_Admin - msapi_reauthorize", eventUser.getName() + " successfully reregeristed the account and got a new token set", LogLvl.normale);
                        eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " successfull token request from the new account").queue();
                    } else if (!success) {
                        Logger.log("S_Admin - msapi_reauthorize", eventUser.getName() + " failed the reregistration with the new accound and couldn't get a new token set", LogLvl.moderate);
                        eventChannel.sendMessage(":x:   " + eventUser.getAsMention() + " failed the token request from the new account").queue();
                    }
                });
            }
            event.reply("queued").queue();
        }
    }
    private static void msapi_refreshtoken(SlashCommandInteractionEvent event) {
        MessageChannelUnion eventChannel = event.getChannel();
        User eventUser = event.getUser();
        Logger.log("S_Admin - msapi_refreshtoken", eventUser.getName() + " executed '/admin msapi refreshtoken'", LogLvl.command);
        MsAuth.tokenRT(success -> {
            if (success) {
                Logger.log("S_Admin - msapi_refreshtoken", eventUser.getName() + " successfully refresh the token via RFToken", LogLvl.normale);
                eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " recieved a new token set via the RFToken").queue();
            } else if (!success) {
                Logger.log("S_Admin - msapi_refreshtoken", eventUser.getName() + " failed to refresh the token via RFToken", LogLvl.moderate);
                eventChannel.sendMessage(":x:     " + eventUser.getAsMention() + " failed to recieve a new token set via the RFToken").queue();
            }
        });
        event.reply("queued").queue();
    }
    private static void msapi_refreshtodo(SlashCommandInteractionEvent event) {
        MessageChannelUnion eventChannel = event.getChannel();
        User eventUser = event.getUser();
        Logger.log("S_Admin - msapi_refrehtodo", eventUser.getName() + " executed '/admin mspai refreshtodo'", LogLvl.command);

        MsGraph.refreshToDoList(success -> {
            if (success) {
                Logger.log("S_Admin - msapi_refrehtodo", eventUser.getName() + " refreshed the ToDoHW List successfully", LogLvl.normale);
                eventChannel.sendMessage(":white_check_mark:     " + eventUser.getAsMention() + " refeshed the ToDo Homework list successfully").queue();
            } else if (!success) {
                Logger.log("S_Admin - msapi_refrehtodo", eventUser.getName() + " failed to refreh to ToDoHW List", LogLvl.moderate);
                eventChannel.sendMessage(":x:     " + eventUser.getAsMention() + " failed to refresh the ToDo Homework list").queue();
            }
        });
        event.reply("queued").queue();
    }
    private static void msapi_ctimehwnot(SlashCommandInteractionEvent event) {
        event.reply("Starting Notification, but can't verify anything").queue();
        HomeworkNotification.Notifie(LocalTime.parse(event.getOption("notificationtime").getAsString()));
    }
}