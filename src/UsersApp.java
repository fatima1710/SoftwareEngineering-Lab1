
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
            return;
        }

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
    }
}
