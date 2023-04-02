/*
 * Copyright (c) 2023. Vahidin Hasić
 */

package ba.etf.elections.passwordGenerator;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class PasswordGenerator {
    public static void main(String[] args) {
        // ask user to input password
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Unesite lozinku:");
        String password = scanner.nextLine();  // Read user input

        System.out.println("Unesite broj iteracija za heširanje:");
        int iterations = scanner.nextInt();  // this should be at least 10, the larger the number the more secure the hash is, but it takes exponentialy more time to generate

        // Hash the user's password with BCrypt and the key
        String key = BCrypt.gensalt(iterations);
        System.out.println("Ključ (key): " + key);
        String hashedPassword = BCrypt.hashpw(password, key);
        System.out.println("Heširana lozinka (systemPassword): " + hashedPassword);
    }
}