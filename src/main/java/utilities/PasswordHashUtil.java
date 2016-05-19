package utilities;

import java.util.Scanner;

/**
 * Created by lon on 12/05/16.
 */
public class PasswordHashUtil {

    public static void main(String[] args ){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Skriv inn ønsket passord: ");
        String password = scanner.nextLine();

        String salt = Security.generateSalt();
        String hash = Security.generateHash(password, salt);

        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);
    }
}
