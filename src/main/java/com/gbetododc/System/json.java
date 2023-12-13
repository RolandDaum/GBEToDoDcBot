package com.gbetododc.System;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;
import java.io.FileWriter;
import java.io.IOException;

import com.gbetododc.DiscordBot.roles;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;

public class json {

    static Dotenv dotenv = Dotenv.configure().load();
    static String GUILDID = dotenv.get("GUILDID");
    static String jsonFilePath = "C:\\Users\\daumr\\Desktop\\gbetododc\\src\\main\\java\\com\\gbetododc\\DiscordBot\\courses.json";


    public static Map<String, Map<String, Long>> getcoursemap() {

        try {
            String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            Gson gson = new Gson();

            Map<String, Map<String, Long>> coursesejson = gson.fromJson(jsonData, Map.class);

            // System.out.println(coursesejson.get("LK").containsKey("MA1"));
            // System.out.println(coursesejson.get("LK").get("MA1"));
            // coursesejson.get("LK").remove("MA1");
            // System.out.println(coursesejson);
            // saveToJsonFile(coursesejson, jsonFilePath);

            return coursesejson;

        } catch (IOException error) {
            error.printStackTrace();
            return null;
        }
    }

    public static Map<String, Map<String, Long>> removeCourse(Event event, Map<String, Map<String, Long>> coursemap, String kursart, String  kursname) {
        if (coursemap.containsKey(kursart) && coursemap.get(kursart).containsKey(kursname)) {
            coursemap.get(kursart).remove(kursname);
        }
        return coursemap;
    }
    public static Map<String, Map<String, Long>> addCourse(Map<String, Map<String, Long>> coursemap, String kursart, String newkursname, Guild kursroleguild) {

        if (!coursemap.get(kursart).containsKey(newkursname)) {

            coursemap.get(kursart).put(newkursname, kursroleguild.getIdLong()); // class to create roles 
            saveToJsonFile(coursemap, jsonFilePath);
        }

        return coursemap;

    }

    private static void saveToJsonFile(Map<String, Map<String, Long>> coursemap, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String jsonString = gson.toJson(coursemap);

            fileWriter.write(jsonString);

            System.out.println("Wrote data successfully to: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}