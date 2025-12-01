
package com.churrascoapp.utils;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ArquivoUtils {
    public static synchronized void appendLine(String path, String line) throws IOException {
        Files.createDirectories(Paths.get(path).getParent());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {
            bw.write(line);
            bw.newLine();
        }
    }

    public static synchronized List<String> readAllLines(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) return new ArrayList<>();
        return Files.readAllLines(Paths.get(path));
    }

    public static synchronized void overwrite(String path, List<String> lines) throws IOException {
        Files.createDirectories(Paths.get(path).getParent());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        }
    }
}
