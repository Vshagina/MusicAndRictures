package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String musicInputFilePath = "C:\\Users\\shagi\\Desktop\\music\\inFile.txt";
        String musicOutputFilePath = "C:\\Users\\shagi\\Desktop\\music\\outFile.txt";

        String imageInputFilePath = "C:\\Users\\shagi\\Desktop\\pictures\\inFile1.txt";
        String imageOutputFilePath = "C:\\Users\\shagi\\Desktop\\pictures\\outFile1.txt";

        List<String> musicURLs = readURLsFromFile(musicInputFilePath);
        List<String> imageURLs = readURLsFromFile(imageInputFilePath);

        writeURLsToFile(musicOutputFilePath, musicURLs);
        writeURLsToFile(imageOutputFilePath, imageURLs);

        Thread musicThread = new Thread(() -> {
            downloadMusicFiles(musicURLs);
            playMusicFiles(musicURLs);
        });

        Thread imageThread = new Thread(() -> {
            downloadImageFiles(imageURLs);
        });

        musicThread.start();
        imageThread.start();

        try {
            musicThread.join();
            imageThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readURLsFromFile(String filePath) {
        List<String> URLs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                URLs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return URLs;
    }

    private static void writeURLsToFile(String filePath, List<String> URLs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String url : URLs) {
                writer.write(url);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadMusicFiles(List<String> musicURLs) {
        String downloadDirectory = "C:\\Users\\shagi\\Desktop\\music\\music";
        for (String url : musicURLs) {
            String fileName = getFileNameFromURL(url);
            URL downloadURL = null;
            try {
                downloadURL = new URL(url);
                Path targetPath = new File(downloadDirectory, fileName).toPath();
                Files.copy(downloadURL.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void downloadImageFiles(List<String> imageURLs) {
        String downloadDirectory = "C:\\Users\\shagi\\Desktop\\pictures\\pictures";
        for (String url : imageURLs) {
            String fileName = getFileNameFromURL(url);
            URL downloadURL = null;
            try {
                downloadURL = new URL(url);
                Path targetPath = new File(downloadDirectory, fileName).toPath();
                Files.copy(downloadURL.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getFileNameFromURL(String url) {
        int lastSlashIndex = url.lastIndexOf('/');
        String fileName = url.substring(lastSlashIndex + 1);
        return fileName;
    }

    private static void playMusicFiles(List<String> musicURLs) {
        for (String url : musicURLs) {
            String fileName = getFileNameFromURL(url);
            String filePath = "C:\\Users\\shagi\\Desktop\\music\\music\\" + fileName;

            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                AdvancedPlayer player = new AdvancedPlayer(fileInputStream);
                player.play();
            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}