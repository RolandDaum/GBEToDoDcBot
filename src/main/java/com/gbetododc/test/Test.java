package com.gbetododc.Test;

import java.util.List;

import com.google.gson.Gson;

import kong.unirest.Unirest;

public class Test {    
  
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