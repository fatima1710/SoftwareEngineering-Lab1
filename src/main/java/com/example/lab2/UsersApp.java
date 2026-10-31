package com.example.lab2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class UsersApp {

    public static void main(String[] args) {

        // load valid users from the input file
        ArrayList<User> users = loadUsers();

        Collections.sort(users, (u1, u2) ->
                u1.getName().compareTo(u2.getName()));

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter("out.txt"))) {

            for (User user : users) {
                writer.println(
                        user.getName() + " " + user.getPassword()
                );
            }

        } catch (IOException e) {
            System.out.println("Error writing file");
        }
    }// end of main

    // loads valid users from the file and returns them as an ArrayList
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            File file = new File("users.txt");
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.trim().split("\\s+");

                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    try {
                        User user = new User(username, password);
                        users.add(user);

                    } catch (IllegalArgumentException e) {
                        System.out.println(line + " " + e.getMessage());
                    }
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return users;
        }
        return users;
    }
}
