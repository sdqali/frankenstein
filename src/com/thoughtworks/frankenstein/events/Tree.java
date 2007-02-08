package com.thoughtworks.frankenstein.events;

public class Tree {
    static String name(String scriptLine) {
        return scriptLine.split(",")[0];
    }

    static String[] path(String scriptLine) {
        String[] line = scriptLine.split(",");
        String[] path = new String[line.length - 1];
        System.arraycopy(line, 1, path, 0, line.length - 1);
        return path;
    }

    public static String pathString(String[] path, String delimiter, String surrounding) {
        String p = "";
        for (int i = 0; i < path.length; i++) {
            p += surrounding + path[i] + surrounding + delimiter;

        }
        return p.substring(0, p.length() - 1);
    }

    public static String pathString(String[] path, String delimiter) {
        return pathString(path, delimiter, "");
    }
}
