package com.example.FormulaBuddy;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileHandler {
    private static final String FORMULA_RECORDS_NAME = "formula_records.ser";
    private static final Path FORMULA_RECORDS_PATH = getAppDirectory().resolve(FORMULA_RECORDS_NAME);
    private static List<FormulaRecord> formulaRecords = new ArrayList<>();

    private static final String COMMON_FUNCTION_RECORDS_NAME = "common_functions_records.ser";
    private static final Path COMMON_FUNCTION_RECORDS_PATH = getAppDirectory().resolve(COMMON_FUNCTION_RECORDS_NAME);
    private static List<CommonFunction> commonFunctions = new ArrayList<>();


    public static Path getAppDirectory() {
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

    public static

    // Save a list of records to a file
    public static void saveRecords(List<FormulaRecord> records) {
        try {
            Files.createDirectories(FORMULA_RECORDS_PATH.getParent()); // Ensure directory exists
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(FORMULA_RECORDS_PATH))) {
                out.writeObject(records);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle better in production
        }
    }

    public static

    // Load records from the file
    @SuppressWarnings("unchecked")
    public static List<FormulaRecord> loadRecords() {
        if (!Files.exists(FORMULA_RECORDS_PATH)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(FORMULA_RECORDS_PATH))) {
            return (List<FormulaRecord>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Handle better in production
            return new ArrayList<>();
        }
    }
}
