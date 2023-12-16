package com.gbetododc.DiscordBot;

import java.util.Iterator;
import java.util.Map;

import com.gbetododc.System.Json;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class Roles {

    public static void deleteRole(Role roleObj, String coursetype) {
        if (roleObj.getIdLong() == 1173317439761678339L || roleObj.getIdLong() == 1173303775407116288L) {
            Logger.log("Roles - deleteRole", "Cant delete the role from the Bot or @everyone", LogLvl.moderate);
        } else {
            try {
                Map<String, Map<String, Long>> coursemap = Json.getcoursemap();
                String rolename = roleObj.getName();

                if (coursetype != null) {
                    if (Json.removeCourse(coursemap, rolename, roleObj.getIdLong(), coursetype)) {
                        roleObj.delete()
                            .queue(
                                success -> {
                                    Logger.log("Roles - removeCourse", "Succefully removed courserole from courses.json and discord", LogLvl.normale);
                                }
                            );
                    } else {
                        Logger.log("Roles - removeCourse", "Could not remove the courserole from courses.json. Nothing happend", LogLvl.moderate);
                    }
                } else if (coursetype == null) {
                    roleObj.delete()
                        .queue(
                            success -> {
                                Logger.log("Roles - deleteRole", "Successfully roleObj '" + rolename + "'", LogLvl.normale);
                            }
                        );
                }

            } catch (Error error) {
                Logger.log("Roles - deleteRole", error.toString(), LogLvl.moderate);
            }
        }
        
    }

    public static void createRole(Guild serverguild, String rolename, String coursetype, Integer color) {
        try {
            Map<String, Map<String, Long>> coursemap = Json.getcoursemap();
            if (coursetype != null && !coursemap.get(coursetype).containsKey(rolename)) {
                Logger.log("Roles - createRole", "Provided Coursetype and courserolename which isn't existing in courses.json", LogLvl.normale);
                serverguild.createRole()
                    .setName(rolename)
                    .setColor(color) // 0x546e7a
                    .queue(
                        role -> {
                            Logger.log("Roles - createRole", "Created a course Role: " + role.getName(), LogLvl.normale);
                            Json.addCourse(coursemap, coursetype, rolename, role.getIdLong());
                        }
                    );
            } else if (coursetype != null && coursemap.get(coursetype).containsKey(rolename)) {
                Logger.log("Roles - createRole", "'" + rolename + "' is already in coursetype '" + coursetype + "'", LogLvl.moderate);
                return;
            } else if (coursetype == null) {
                serverguild.createRole()
                    .setName(rolename)
                    .setColor(color)
                    .queue(
                        role -> {
                            Logger.log("Roles - createRole", "Created a normal role: " + role.getName(), LogLvl.normale);
                        }
                    );
            }
        } catch (Error error) {
            Logger.log("S_Admin - roles createRole", error.toString(), LogLvl.moderate);
        }
    }
    
}
