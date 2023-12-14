package com.gbetododc.DiscordBot;

import com.gbetododc.System.ConsoleColors;
import com.gbetododc.System.json;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class roles {

    public static void deleteRole(Role role) {
        try {
            role.delete().queue();
            // TODO: Delete role from courses.json, if it is in the list
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    public static void createRole(Guild serverguild, String rolename, String kursart, Integer color) {

        try {
            if (kursart != null && !json.getcoursemap().get(kursart).containsKey(rolename)) {                 // save the role/kurs name and id in json file, when a kursart is provieded
                System.out.println(ConsoleColors.GREEN + "ROLES.java |   " + ConsoleColors.RESET + "Provided Coursetype and courserolename which isn't existing in courses.json");
                serverguild.createRole()
                    .setName(rolename)
                    .setColor(color) // 0x546e7a
                    .queue(
                        role -> {
                            System.out.println(ConsoleColors.GREEN + "ROLES.java |   " + ConsoleColors.RESET + "Created a course Role: " + role.getName());
                            json.addCourse(json.getcoursemap(), kursart, rolename, role.getGuild());
                        }
                    );
            } else if (kursart != null && json.getcoursemap().get(kursart).containsKey(rolename)) {
                System.out.println(ConsoleColors.YELLOW + "ROLES.java |   " + ConsoleColors.RESET + "Role: " + rolename + " is allready in coursetype: " + kursart + " nothing has been done.");
                return;
            } else if (kursart == null) {                                                               // just create a roll without saving its name and id in json
                serverguild.createRole()
                    .setName(rolename)
                    .setColor(color)
                    .queue(
                        role -> {
                            System.out.println(ConsoleColors.GREEN + "ROLES.java |   " + ConsoleColors.RESET + "Created a normale Role: " + role.getName());
                        }
                    );
            }
        } catch (Error error) {
            error.printStackTrace();
        }
    }
    
}
