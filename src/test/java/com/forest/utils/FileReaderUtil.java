package com.forest.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderUtil {
    public static String readJsonFromFile(String path) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder result = new StringBuilder();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            result.append(currentLine.trim());
            currentLine = reader.readLine();
        }
        reader.close();
        return result.toString().replace("\n", "");
    }
}
