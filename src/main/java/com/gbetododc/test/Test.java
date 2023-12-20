package com.gbetododc.Test;

import java.util.Iterator;
import java.util.Map;
import com.gbetododc.System.Json;

public class Test {
    public static void main(String[] args) {
        System.out.println("String mit String direkt: " + ("MA1" == "MA1"));

        String newString = new String("MA1");
        String string = "MA1";
        System.out.println("String mit newString: " + (newString == string));

        String newString2 = new String("MA1");
        String string2 = "MA1";
        System.out.println("String mit newString.equals(): " + newString.equals(string));

        String stringA = "MA1";
        String stringB = "MA1";
        System.out.println("String mit String via var: " + (stringA == stringB));
    }
}