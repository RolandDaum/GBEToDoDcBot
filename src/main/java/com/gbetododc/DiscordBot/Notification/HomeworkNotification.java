package com.gbetododc.DiscordBot.Notification;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.gbetododc.DiscordBot.DiscordBot;
import com.gbetododc.MSAuthGraph.JsonTodoHW;
import com.gbetododc.MSAuthGraph.MsGraph;
import com.gbetododc.MSAuthGraph.MsGraph.ToDoHW_Task;
import com.gbetododc.System.CJson;
import net.dv8tion.jda.api.entities.Role;

public class HomeworkNotification {
    public static List<LocalTime> PeriodStartTimes = new ArrayList<>(
        Arrays.asList(
            LocalTime.of(7, 40),
            LocalTime.of(8,30),
            LocalTime.of(9,35),
            LocalTime.of(10,25),
            LocalTime.of(11,25),
            LocalTime.of(12,15),
            LocalTime.of(13,5),
            LocalTime.of(13,50),
            LocalTime.of(14,35),
            LocalTime.of(15,25),
            LocalTime.of(16,20)
        )
    );
    public static void Notifie(LocalTime notificationTime) {
        // Checken, ob es Mo-Fr ist
        Integer weekday = LocalDate.now(Clock.system(ZoneId.of("Europe/Berlin"))).getDayOfWeek().getValue();
        if (weekday < 6) {
            // Checken, ob es zur Zeit Kurse gibt
            List<String> courses = JsonTTble.getTTble().getTimetable().getWeekday(weekday).getPeriodCpursesByLocalTime(notificationTime);
            if (courses != null) {
                // ToDo List aktualisieren
                MsGraph.refreshToDoList(success -> {
                    if (success) {
                        // Gucken, ob es überhaupt Aufgaben gibt, die heute fällig sind.
                        // TODO: Was passiert, wenn es keien Aufgaben gibt?
                        List<ToDoHW_Task> todoHWTasksList = JsonTodoHW.getToDoHW().getValue();
                        // System.out.println(courses);
                        for (ToDoHW_Task toDoHW_Task : todoHWTasksList) {
                            // Guckt, ob das Fälligkeitsdatum der Aufgabe am Heutigen Tag ist.
                            LocalDateTime taskDueDateTime = LocalDateTime.parse(toDoHW_Task.getDueDateTime().getDateTime());
                            ZoneId taskDueTimeZone = ZoneId.of(toDoHW_Task.getDueDateTime().getTimeZone());
                            LocalDate DueDate = taskDueDateTime.atZone(taskDueTimeZone).withZoneSameInstant(ZoneId.of("Europe/Berlin")).toLocalDate();

                            if (LocalDate.now(Clock.system(ZoneId.of("Europe/Berlin"))).equals(DueDate)) {
                                // Gucken, ob die heute fällige Aufgabe im Aktuellen Band liegt
                                String taskCourse = getCourseNameFromTitle(toDoHW_Task.getTitle());
                                if (courses.contains(taskCourse.toString())) {
                                    // System.out.println("ToDo is in period: " + toDoHW_Task.getTitle());
                                    sendDM(taskCourse, "** ## Hausaufgabe - " + taskCourse + "**" + getHWfromTitle(toDoHW_Task.getTitle()));
                                    // mark task as completed
                                    toDoHW_Task.setStatus("completed");
                                    MsGraph.updateTask(toDoHW_Task);
                                } else {
                                    // System.out.println(taskCourse + " ist nicht in dieser Periode");
                                }

                            } else {
                                // System.out.println("Die Aufgabe ist nicht heute fällig");
                            }
                        }
                    }
                });
            } 
            else if (courses == null) {
                // System.out.println("In dieser Stunde/Periode gibt es keine Kurse");
            }
        } else {
            // System.out.println("Es ist der " + weekday + " in der Woche");
        }
    
    }
    /**
     * @param taskTitle takes the entire taskTitle
     * @return parsed Coursname; May be modified (Ds1 becomes ds1, but MA1 stays MA1)
     */
    private static String getCourseNameFromTitle(String taskTitle) {
        String Coursename = "";
        for (Integer i = 0; i < taskTitle.length(); i++) {
            if (taskTitle.charAt(i) == ' ' || taskTitle.charAt(i) == ':') {
                break;
            } else {
                Coursename += taskTitle.charAt(i);
            }
        }
        if (Coursename.length() >= 3) {
            Character char0 = Coursename.charAt(0);
            Character cahr1 = Coursename.charAt(1);
            // Kursschreibweise Überprüfung - (Ds1 wird zu ds1, aber MA1 bleibt MA1)
            if (Character.isUpperCase(char0) && Character.isLowerCase(cahr1)) {
                return Character.toLowerCase(char0) + Coursename.substring(1, Coursename.length());
            } else {
                return Coursename;
            }
        }
        return Coursename;
    }
    /**
     * @param taskTitle takes the entire taskTitle
     * @return returns the HW Task if split up by ':', else just returns the input
     */
    public static String getHWfromTitle(String taskTitle) {
        for (Integer i = 0; i < taskTitle.length(); i++) {
            if (taskTitle.charAt(i) == ':') {
                taskTitle = taskTitle.substring(i+1, taskTitle.length());
                Integer spaceCount = 0;
                for (Integer u = 0; u < taskTitle.length(); u++) {
                    if (taskTitle.charAt(u) == ' ') {
                        spaceCount++;
                    } else {
                        return taskTitle.substring(spaceCount, taskTitle.length());
                    }
                }
            }
        };
        return taskTitle;
    }
    /**
     * @param course coursename as String
     * @param Message HW Task to be send
     */
    private static void sendDM(String course, String Message) {
        // Geht einmal über die ganze coursemap
        Map<String, Map<String, Long>> coursemap = CJson.getcoursemap();
        for (Entry<String, Map<String, Long>> entry : coursemap.entrySet()) {
            for (Entry<String, Long> innerEntry : entry.getValue().entrySet()) {

                // Guckt, ob es eine Rolle mit dem Coursenamen gibt, wenn ja, dann sendet er die Aufgabe an alle Member mit dieser Rolle
                if (innerEntry.getKey().equals(course)) {

                    Role courseRole = DiscordBot.JDA.getRoleById(innerEntry.getValue());
                    List<net.dv8tion.jda.api.entities.Member> membersToNotifie = DiscordBot.MAIINSERVERGUILD.getMembersWithRoles(courseRole);
                    for (net.dv8tion.jda.api.entities.Member member : membersToNotifie) {
                        member.getUser().openPrivateChannel().queue(
                            success -> {
                                success.sendMessage(Message).queueAfter(2, TimeUnit.SECONDS);
                            }
                        );
                    }
                }
            }
        }
    }
}