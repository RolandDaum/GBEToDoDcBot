package com.gbetododc.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import com.gbetododc.System.json;

public class Test {

    public static void main(String[] args) {
        System.out.println((System.getProperty("user.dir") + "\\src\\main\\java\\com\\gbetododc\\DiscordBot\\courses.json"));

        // String filePath = new File("").getAbsolutePath();
        // System.out.println(filePath);
    }

    public static void stingEqul() {
        System.out.println("String mit String direkt: " + ("MA1" == "MA1"));

        String newString = new String("MA1");
        String string = "MA1";
        System.out.println("String mit newString: " + (newString == string));

        String newString2 = new String("MA1");
        String string2 = "MA1";
        System.out.println("String mit newString.equals(): " + newString2.equals(string2));

        String stringA = "MA1";
        String stringB = "MA1";
        System.out.println("String mit String via var: " + (stringA == stringB));
    }
}