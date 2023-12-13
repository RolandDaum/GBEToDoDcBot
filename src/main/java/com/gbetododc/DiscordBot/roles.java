package com.gbetododc.DiscordBot;

import com.gbetododc.System.json;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class roles {

    public static void deleteRole(Role role) {
        try {
            role.delete().queue();
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    public static void createRole(Guild serverguild, String kursart , String rolename, Integer color) {

        try {
            if (kursart != null) {                                                                      // save the role/kurs name and id in json file, when a kursart is provieded
                serverguild.createRole()
                    .setName(rolename)
                    .setColor(color) // 0x546e7a
                    .queue(
                        role -> {
                            json.addCourse(json.getcoursemap(), kursart, rolename, role.getGuild());
                        }
                    );
            } else if (kursart == null) {                                                               // just create a roll without saving its name and id in json
                serverguild.createRole()
                    .setName(rolename)
                    .setColor(color)
                    .queue();
            }
        } catch (Error error) {
            error.printStackTrace();
        }
    }
    
}
