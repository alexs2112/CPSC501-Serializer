package object_creator;

import java.util.Scanner;

public class ObjectCreator {
    public static void main(String[] args) {
        System.out.println(">> Hello, World!");
        Scanner scanner = new Scanner(System.in);

        System.out.println(">> Say something and I will repeat it to you.");
        String s = scanner.next();
        System.out.println(">> " + s);
    }
}
