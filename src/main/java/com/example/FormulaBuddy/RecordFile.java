package com.example.FormulaBuddy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordFile<T extends Serializable> {

    private File file;
    private List<T> records;
    private final T[] defaultRecords;

    public List<T> getRecords() { return records; }

    public RecordFile(String fileName, T[] defaultRecords) {
        Path path = getAppDirectory().resolve(fileName);
        this.defaultRecords = defaultRecords;
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

    private void getOrCreateFile(Path path, T[] defaultRecords) {
        file = path.toFile();
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                records = new ArrayList<>(List.of(defaultRecords));
                save();
            } else {
                if (file.length() == 0) {
                    records = new ArrayList<>(List.of(defaultRecords));
                } else {
                    records = new ArrayList<>(load());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean resetRecords() {
        records = new ArrayList<>(Arrays.asList(defaultRecords));
        return save();
    }

    @SuppressWarnings("unchecked")
    public List<T> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            records = (List<T>) ois.readObject();
        } catch (Exception e) {
            records = new ArrayList<>();
        }
        return records;
    }

    public boolean addRecord(T record) {
        if (records.contains(record)) {
            return false;
        }

        records.add(record);
        save();
        return true;
    }

    public boolean deleteRecord(T record) {
        boolean success = records.remove(record);

        if (success) {
            return save();
        } else {
            return false;
        }
    }

    public boolean replaceRecord(T oldRecord, T newRecord) {
        int index = records.indexOf(oldRecord);
        if (index >= 0) {
            records.set(index, newRecord);
            return save();
        } else {
            return false;
        }
    }

    public boolean save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(records);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
