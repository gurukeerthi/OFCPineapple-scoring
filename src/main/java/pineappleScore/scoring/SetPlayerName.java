package pineappleScore.scoring;

import pineappleScore.Players;

import java.util.Scanner;

public class SetPlayerName {

    public void playerName(Players players){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter name for player 1");
        players.setPLAYER1(scanner.next());

        System.out.println("Enter name for player 2");
        players.setPLAYER2(scanner.next());

        System.out.println("Enter name for player 3");
        players.setPLAYER3(scanner.next());

    }


}
