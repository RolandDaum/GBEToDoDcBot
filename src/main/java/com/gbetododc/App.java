package com.gbetododc;

import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class App {
    public static void main( String[] args ) {
        DiscordBot.main();
        MSGraph.main();
    }


    

    public static String selenium() {
        String email = "gbetododc@outlook.de";
        String password = "pWqPoFp12m0RJ2x1";

        WebDriver driver = new ChromeDriver();
        driver.get("https://login.microsoftonline.com/");

        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}


        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(email);
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}

        driver.findElement(By.id("i0118")).sendKeys(password);
        // driver.findElement(By.cssSelector("input[type='passwd']")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        driver.findElement(By.cssSelector("input[type='submit']")).click();

        driver.get("https://to-do.live.com/tasks/AQMkADAwATNiZmYAZC02ZDJjLWY0MmMtMDACLTAwCgAuAAAD2l9XJL9noki-hdT_-gBT3yABAHn1bKpiBqpGrQx7HaO9sNkAAAIBRQAAAA==");

        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

        driver.findElement(By.id("mectrl_headerPicture")).click();



        try {Thread.sleep(20000);} catch (InterruptedException e) {e.printStackTrace();}


        String content = driver.findElement(By.xpath("/html/body/div/div/div[3]/span/div/div[2]/div/div[3]/div/div/div/div/div")).getText();
        driver.quit();

        return(content);

    }
}


