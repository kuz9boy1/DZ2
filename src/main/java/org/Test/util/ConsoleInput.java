package org.Test.util;

import java.util.Scanner;

public class ConsoleInput implements AutoCloseable{
    private final Scanner scanner;

    public ConsoleInput() {
        this.scanner = new Scanner(System.in);
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : "";
    }

    public Long readLong(String prompt) {
        String raw = readLine(prompt);
        if (raw.isBlank()) {
            System.out.println("Значение не введено.");
            return null;
        }
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный id: " + raw);
            return null;
        }
    }

    public Integer readInt(String prompt) {
        String raw = readLine(prompt);
        if (raw.isBlank()) {
            System.out.println("Значение не введено.");
            return null;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            System.out.println("Ожидалось целое число, получено: " + raw);
            return null;
        }
    }

    @Override
    public void close() {
        scanner.close();
    }
}
