package com.example.FormulaBuddy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecordFile<T> {

    //private Path path;
    private File file;
    private List<T> records;

    public File getFile() { return file; }
    public List<T> getRecords() { return records; }

    public RecordFile(String fileName, List<T> defaultRecords) {
        Path path = getAppDirectory().resolve(fileName);
        getOrCreateFile(path, defaultRecords);
    }

    private Path getAppDirectory() {
        try {
            // This gives you the absolute path to the running JAR
            Path jarPath = Paths.get(FileHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            // If it's a JAR, get the parent directory
            return Files.isDirectory(jarPath) ? jarPath : jarPath.getParent();
        } catch (Exception e) {
            // Fallback: current working directory
            return Paths.get(System.getProperty("user.dir"));
        }
    }

    private void getOrCreateFile(Path path, List<T> defaultRecords) {
        file = path.toFile();
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                records = new ArrayList<>(defaultRecords);
                save();
            } else {
                if (file.length() == 0) {
                    records = new ArrayList<>(defaultRecords);
                } else {
                    records = new ArrayList<>(load());
                }
            }
        } catch (IOException e) {
            SystemMessageHandler.sendMessages("Error finding file: " + e.getMessage());
        }
    }

    public List<T> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            records = (List<T>) ois.readObject();
        } catch (Exception e) {
            SystemMessageHandler.sendMessages("Error loading file: " + e.getMessage());
            records = new ArrayList<>();
        }
        return records;
    }

    public boolean save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(records);
            return true;
        } catch (IOException e) {
            SystemMessageHandler.sendMessages("Error saving file: " + e.getMessage());
            return false;
        }
    }
}
