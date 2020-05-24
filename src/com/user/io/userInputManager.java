package com.user.io;

import java.util.Scanner;

// import java.util.*;

public class userInputManager {
    String newAction;
    String lastAction;

    public userInputManager() {
        Scanner sc = new Scanner(System.in);
        String str = "";
        int i = 1;

        while (!str.equals("exit")) {
            System.out.print("input" + i + ": ");
            System.out.println("Type text and press Enter");
            str = sc.nextLine();
            System.out.println("You typed : " + str);
            i++;
            if (str.equals("exit")) {
                System.out.println("Exiting now");
            }
        }
    }

    public static void keyStrokeInterceptor() {
    }


    public static void main(String[] args) {
        userInputManager user_io = new userInputManager();
//        userInputManager.keyBoardInput();
    }

}
