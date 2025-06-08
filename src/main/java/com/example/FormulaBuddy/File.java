package com.example.FormulaBuddy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class File {

    private static Path PATH;
    private static File FILE;

    public File(String fileName) {
        PATH = getAppDirectory().resolve(fileName);
        
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


}
