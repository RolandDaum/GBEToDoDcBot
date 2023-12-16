package com.gbetododc.System;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import com.gbetododc.System.Logger.LogLvl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.cdimascio.dotenv.Dotenv;

public class Json {

    static Dotenv dotenv = Dotenv.configure().load();
    static String GUILDID = dotenv.get("GUILDID");
    static String jsonFilePath = "C:\\Users\\daumr\\Desktop\\gbetododc\\src\\main\\java\\com\\gbetododc\\DiscordBot\\courses.json";

    public static Map<String, Map<String, Long>> getcoursemap() {
        try {
            String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            Gson gson = new Gson();
            Map<String, Map<String, Long>> coursesejson = gson.fromJson(jsonData, new TypeToken<Map<String, Map<String, Long>>>() {}.getType() );

            Logger.log("Json - getcoursemap","Returning requested coursemap",LogLvl.normale);
            return coursesejson;

        } catch (IOException error) {
            error.printStackTrace();
            return null;
        }
    }

    public static void addCourse(Map<String, Map<String, Long>> coursemap, String coursetype, String newkursname, Long roleID) {

        if (!coursemap.get(coursetype).containsKey(newkursname)) {
            coursemap.get(coursetype).put(newkursname, roleID);
            saveToJsonFile(coursemap, jsonFilePath);
            Logger.log("Json - addCourse", "Successfully added course to coursemap", LogLvl.normale);
        }

    }
    public static Boolean removeCourse(Map<String, Map<String, Long>> coursemap, String  coursename, Long courseid, String coursetype) {
        if (coursemap.get(coursetype).containsKey(coursename) && coursemap.get(coursetype).get(coursename).longValue() == courseid) {
            coursemap.get(coursetype).remove(coursename);
            saveToJsonFile(coursemap, jsonFilePath);
            Logger.log("Json - removeCourse", "Successfully removed course from coursemap", LogLvl.normale);
            return true;
        } else {
            Logger.log("Json - removeCourse", "The provided course '" + coursename + "' could not be found under coursetype '" + coursetype + "' or the courseroleID doesn't match", LogLvl.moderate);
            return false;
        }
    }

    public static void clearCourseList() {
        Map<String, Map<String, Long>> coursemap = Json.getcoursemap();

        for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
            Map<String, Long> innerMap = entry.getValue();

            Iterator<Map.Entry<String, Long>> iterator = innerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
        Logger.log("Json - clearCourseList", "Removed all courseroles from courselist", LogLvl.normale);
        saveToJsonFile(coursemap, jsonFilePath);
    }

    private static void saveToJsonFile(Map<String, Map<String, Long>> coursemap, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(coursemap);
            fileWriter.write(jsonString);

            Logger.log("Json - saveToJsonFile", "Successfully saved course map to " + filePath, LogLvl.normale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}