package com.gbetododc.test;

import java.util.Iterator;
import java.util.Map;

import com.gbetododc.System.Json;

public class Test {
    public static void main(String[] args) {
        Map<String, Map<String, Long>> coursemap = Json.getcoursemap();

        for (Map.Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {

            Map<String, Long> CourseIDMap = entry.getValue();
            
            for (Map.Entry<String, Long> entry2 : CourseIDMap.entrySet()) {
                System.out.println(entry2.getKey());
                System.out.println(entry2.getValue());
            }
        }
    }
}