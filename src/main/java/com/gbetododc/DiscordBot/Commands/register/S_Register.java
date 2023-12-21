package com.gbetododc.DiscordBot.Commands.register;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import com.gbetododc.System.Json;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class S_Register {
    public static void main(SlashCommandInteractionEvent event) {
        List<OptionMapping> eventOptionList = event.getOptions();
        User eventUser = event.getUser();
        MessageChannelUnion eventChannel = event.getChannel();
        AtomicInteger amountofCoursestoAdd = new AtomicInteger(eventOptionList.size());
        
        Logger.log("S_Register", eventUser.getName() + " executet '/register' with the following options \n" + eventOptionList, LogLvl.Title);

        // TODO: First, remove all courseroles on /register, so we only have the ones, we want to be registred to the user

        for (OptionMapping option : eventOptionList) {            
            String optionValue = option.getAsString();
            Color roleColor = new Color(255,255,255);
            Entry<String, Long> courseInMap = findCourseinList(Json.getcoursemap(), optionValue);

            if (courseInMap != null) {
                net.dv8tion.jda.api.entities.Role courseRole = event.getGuild().getRoleById(courseInMap.getValue().longValue());
                event.getGuild().addRoleToMember(eventUser, courseRole).queue(
                    success -> {
                        Logger.log("S_Register - addCourse", "added couresrole '" + courseRole.getName() + "' to '" + eventUser.getName() + "'",LogLvl.normale);
                        amountofCoursestoAdd.decrementAndGet();
                        if (amountofCoursestoAdd.get() == 0) {
                            Logger.log("S_Register - addCourse", "finished creating/adding all roles to '" + eventUser.getName()+ "'", LogLvl.Title);
                            eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " assined all roles to you").queue();
                        }
                    }
                );
            } else {
                event.getGuild().createRole()
                    .setName(option.getAsString())
                    .setColor(roleColor)
                    .queue(
                        success -> {
                            Logger.log("S_Register - createNew", "through " + eventUser.getName() + " role: " + success.getName() + " was created on server", LogLvl.normale);

                            String optionKey = option.getName();
                            if (Character.isDigit(optionKey.charAt(optionKey.length() - 1))) {
                                optionKey = optionKey.substring(0, optionKey.length() - 2);
                            }

                            Boolean addedCoursetoJson = Json.addCourse(Json.getcoursemap(), optionKey, success.getName(), success.getIdLong());
                            if (addedCoursetoJson) {
                                Logger.log("S_Register - createNew", "added courserole '" + success.getName() + "' as coursetype '" + optionKey + "' with the color '" + roleColor + "' to courses.json", LogLvl.normale);
                                event.getGuild().addRoleToMember(eventUser, success).queue(
                                    added -> {
                                        amountofCoursestoAdd.decrementAndGet();
                                        Logger.log("S_Register - createNew", "added new created courserole '" + success.getName() + "' to '" + eventUser.getName() + "'", LogLvl.normale);
                                        if (amountofCoursestoAdd.get() == 0) {
                                            Logger.log("S_Register - createNew", "finished creating/adding all roles to '" + eventUser.getName()+ "'", LogLvl.Title);
                                            eventChannel.sendMessage(":white_check_mark:   " + eventUser.getAsMention() + " assined all roles to you").queue();
                                        }
                                    }
                                );
                            } else {
                                Logger.log("S_Register - createNew", "failed to save rolecourse '" + success.getName() + "' in courses.json under '" + optionKey + "'; Deleting role in Discord again.", LogLvl.moderate);
                                success.delete().queue();
                            }
                        },
                        failure -> {
                            Logger.log("S_Register - roles add", failure.getMessage(), LogLvl.moderate);
                        }
                    );
            }
        }
        event.reply("Queued roles to be added to you.").queue();
    }

    private static Map.Entry<String, Long> findCourseinList(Map<String, Map<String, Long>> coursemap, String course) {
        for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
            Map<String, Long> CoursesMap = entry.getValue();
            for (Map.Entry<String, Long> entry2 : CoursesMap.entrySet()) {
                if (entry2.getKey().equals(course)) {
                    return entry2;
                }
            }
        }
        return null;
    }









    
    // public static void main(SlashCommandInteractionEvent event) {
    //     User EventUser = event.getUser();

    //     removeAllRoles(event);
        
    //     for (int i = 0; i < event.getOptions().size(); i++) {
    //         String Kursname = event.getOptions().get(i).getAsString();
    //         Role RoleID = GetRoleIDByName(event, Kursname);

    //         if (RoleID == null) {
    //             event.getJDA().getGuildById(GUILDID)
    //                 .createRole().setName(Kursname)
    //                 .setColor(0x011765)
    //                 .queue(
    //                     (CreatedRole) -> {
    //                         event.getJDA().getGuildById(GUILDID)
    //                             .addRoleToMember(EventUser, GetRoleIDByName(event, Kursname))
    //                             .queue();
    //                     }
    //                 );
    //         } else {
    //             event.getJDA().getGuildById(GUILDID)
    //                 .addRoleToMember(EventUser, GetRoleIDByName(event, Kursname))
    //                 .queue();
    //         }
    //     }
    //     event.reply("Roles has been asinged to user: " + event.getUser().getName()).queue();
    // }

    // // Get the ID of a role by the role name or if not existing return null
    // private static Role GetRoleIDByName(SlashCommandInteractionEvent event, String RoleName) {
    //     return event.getJDA().getGuildById(GUILDID).getRolesByName(RoleName, true).stream().findFirst().orElse(null);
    // }

    // // Remove all roles a user has
    // public static void removeAllRoles(SlashCommandInteractionEvent event) {
    //     // TODO: Später nur die Kursrollen entfernen und nicht alle
    //     List<Role> EventUserRoles = event.getMember().getRoles();
    //     for (Role role : EventUserRoles) {
    //         role.getGuild().removeRoleFromMember(event.getUser(), role).queue();
    //     }
    // }
}