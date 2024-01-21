package com.gbetododc.DiscordBot.Notification;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.System.Logger;
import com.gbetododc.System.Logger.LogLvl;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class JsonTTble {
    static String jsonFilePathString = DiscordBot.PROJPATH + "\\src\\main\\java\\com\\gbetododc\\DiscordBot\\Notification\\TTble.json";
    
    public static TTble getTTble() {
        try {
            String jsonDataString = new String(Files.readAllBytes(Paths.get(jsonFilePathString)));

            TTble timetable = new Gson().fromJson(jsonDataString, TTble.class);

            return timetable;
        } catch (Throwable e) {
            Logger.log("JsonTimetable - getTimetable", "Failed to read timetable.json. It may be missing or currepted", LogLvl.critical);
            return null;
        }
    }



    public static class TTble {
        private TTble_Timetable Timetable; 
        private TTble_Bände Bände;

        public TTble_Timetable getTimetable() {return Timetable;}
        public TTble_Bände getBände() {return Bände;}

        /**
         * @param weekday Integer from 1-5 [1,2,3,4,5]
         * @param period Integer from 1-11 [1,2,3,4,5,6,7,8,9,10,11]
         * @return List<String> with the coresponding of the courses from the period of the weekday
         */
        private List<String> getCourses(Integer weekday, Integer period) {
            return JsonTTble.getTTble().getTimetable().getWeekday(weekday).getPeriod(period);
        }
    }
    public static class TTble_Timetable {
        private TTble_Day Montag;
        private TTble_Day Dienstag;
        private TTble_Day Mittwoch;
        private TTble_Day Donnerstag;
        private TTble_Day Freitag;
    
        /**
         * @param weekday Integer from 1 to 5 [1,2,3,4,5]
         * @return TTble_Day
         */
        public TTble_Day getWeekday(Integer weekday) {
            switch (weekday) {
                case 1:
                    return Montag;
                case 2:
                    return Dienstag;
                case 3:
                    return Mittwoch;
                case 4:
                    return Donnerstag;
                case 5:
                    return Freitag;
                default:
                    return null;
            }
        }
        // public TTble_Day getMontag() {return Montag;}
        // public TTble_Day getDienstag() {return Dienstag;}
        // public TTble_Day getMittwoch() {return Mittwoch;}
        // public TTble_Day getDonnerstag() {return Donnerstag;}
        // public TTble_Day getFreitag() {return Freitag;}

    } 
    public static class TTble_Day {
        @SerializedName("7:40")
        private String _7_40;
        @SerializedName("8:30")
        private String _8_30;
        @SerializedName("9:35")
        private String _9_35;
        @SerializedName("10:25")
        private String _10_25;
        @SerializedName("11:25")
        private String _11_25;
        @SerializedName("12:15")
        private String _12_15;
        @SerializedName("13:05")
        private String _13_05;
        @SerializedName("13:50")
        private String _13_50;
        @SerializedName("14:35")
        private String _14_35;
        @SerializedName("15:25")
        private String _15_25;
        @SerializedName("16:20")
        private String _16_20;

        /**
         * @param period [1,2,3,4,5,6,7,8,9,10,11]
         * @return List<String> with the coresponding courses of the period
         */
        // public List<String> getPeriodByInteger(Integer period) {
        //     switch (period) {
        //         case 1:
        //             return JsonTTble.getTTble().getBände().getBand(_7_40);
        //         case 2:
        //             return JsonTTble.getTTble().getBände().getBand(_8_30);
        //         case 3:
        //             return JsonTTble.getTTble().getBände().getBand(_9_35);
        //         case 4:
        //             return JsonTTble.getTTble().getBände().getBand(_10_25);
        //         case 5:
        //             return JsonTTble.getTTble().getBände().getBand(_11_25);
        //         case 6:
        //             return JsonTTble.getTTble().getBände().getBand(_12_15);
        //         case 7:
        //             return JsonTTble.getTTble().getBände().getBand(_13_05);
        //         case 8:
        //             return JsonTTble.getTTble().getBände().getBand(_13_50);
        //         case 9:
        //             return JsonTTble.getTTble().getBände().getBand(_14_35);
        //         case 10:
        //             return JsonTTble.getTTble().getBände().getBand(_15_25);
        //         case 11:
        //             return JsonTTble.getTTble().getBände().getBand(_16_20);
        //         default:
        //             Logger.log("JsonTTble - getPeriod()", "Invalid Period '" + period + "'", LogLvl.moderate);
        //             return null;
        //     }
        // }

        /**
         * @param localtime [LocalTime.of(7, 40), LocalTime.of(8,30), LocalTime.of(9,35), LocalTime.of(10,25), LocalTime.of(11,25), LocalTime.of(12,15), LocalTime.of(13,5), LocalTime.of(13,50), LocalTime.of(14,35), LocalTime.of(15,25), LocalTime.of(16,20)]
         * @return List<String> with the coresponding courses of the period
         */
        public List<String> getPeriodByLocalTime(LocalTime localtime) {
            List<String> BandList;
            switch (localtime.toString()) {
                case "07:40":
                    BandList = JsonTTble.getTTble().getBände().getBand(_7_40);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_7_40));} 
                    else {return BandList;}
                case "08:30":
                    BandList = JsonTTble.getTTble().getBände().getBand(_8_30);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_8_30));} 
                    else {return BandList;}
                case "09:35":
                    BandList = JsonTTble.getTTble().getBände().getBand(_9_35);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_9_35));} 
                    else {return BandList;}
                case "10:25":
                    BandList = JsonTTble.getTTble().getBände().getBand(_10_25);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_10_25));} 
                    else {return BandList;}
                case "11:25":
                    BandList = JsonTTble.getTTble().getBände().getBand(_11_25);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_11_25));} 
                    else {return BandList;}
                case "12:15":
                    BandList = JsonTTble.getTTble().getBände().getBand(_12_15);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_12_15));} 
                    else {return BandList;}
                case "13:05":
                    BandList = JsonTTble.getTTble().getBände().getBand(_13_05);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_7_40));} 
                    else {return BandList;}
                case "13:50":
                    BandList = JsonTTble.getTTble().getBände().getBand(_13_50);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_13_50));} 
                    else {return BandList;}
                case "14:35":
                    BandList = JsonTTble.getTTble().getBände().getBand(_14_35);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_14_35));} 
                    else {return BandList;}
                case "15:25":
                    BandList = JsonTTble.getTTble().getBände().getBand(_15_25);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_15_25));} 
                    else {return BandList;}
                case "16:20":
                    BandList = JsonTTble.getTTble().getBände().getBand(_16_20);
                    if (BandList == null) {return new ArrayList<>(Arrays.asList(_16_20));} 
                    else {return BandList;}
                default:
                    return null;
            }
        }
        // public String get_7_40() {return _7_40;}
        // public String get_8_30() {return _8_30;}
        // public String get_9_35() {return _9_35;}
        // public String get_10_25() {return _10_25;}
        // public String get_11_25() {return _11_25;}
        // public String get_12_15() {return _12_15;}
        // public String get_13_05() {return _13_05;}
        // public String get_13_50() {return _13_50;}
        // public String get_14_35() {return _14_35;}
        // public String get_15_25() {return _15_25;}
        // public String get_16_20() {return _16_20;}
    }
    private static class TTble_Bände {
        private List<String> Bd1;
        private List<String> Bd2;
        private List<String> Bd3;
        private List<String> Bd4;
        private List<String> Bd5;
        private List<String> Bd6;
        private List<String> Bd7;
        private List<String> Bd8;
        private List<String> Bd9;
        private List<String> Bd10;

        /**
         * @param BandName [Bd1, Bd2, Bd3, Bd4, Bd5, Bd6, Bd7, Bd8, Bd9, Bd10]
         * @return List<String> with every course on the given band
         */
        private List<String> getBand(String BandName) {
            switch (BandName) {
                case "Bd1":
                    return this.Bd1;
                case "Bd2":
                    return this.Bd2;
                case "Bd3":
                    return this.Bd3;
                case "Bd4":
                    return this.Bd4;
                case "Bd5":
                    return this.Bd5;
                case "Bd6":
                    return this.Bd6;
                case "Bd7":
                    return this.Bd7;
                case "Bd8":
                    return this.Bd8;
                case "Bd9":
                    return this.Bd9;
                case "Bd10":
                    return this.Bd10;
                default:
                    Logger.log("JsonTTble - getBand()", "Invalid Bandname '" + BandName + "'", LogLvl.moderate);
                    return null;
                }
        }
        // public List<String> getBd1() {return Bd1;}
        // public List<String> getBd2() {return Bd2;}
        // public List<String> getBd3() {return Bd3;}
        // public List<String> getBd4() {return Bd4;}
        // public List<String> getBd5() {return Bd5;}
        // public List<String> getBd6() {return Bd6;}
        // public List<String> getBd7() {return Bd7;}
        // public List<String> getBd8() {return Bd8;}
        // public List<String> getBd9() {return Bd9;}
        // public List<String> getBd10() {return Bd10;}
    }
}
