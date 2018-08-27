package pineappleScore.scoring;

import com.sun.org.apache.xpath.internal.operations.Bool;
import pineappleScore.Players;
import pineappleScore.enums.Bottom;
import pineappleScore.enums.Middle;
import pineappleScore.enums.Top;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Score {
    private static final String FILENAME = System.getProperty("user.home") + "/Desktop/pineappleScore.txt";
    private static Map<String, Boolean> glassBreak = new HashMap<String, Boolean>();
    private static Map<String,Boolean> fantasyPlayers = new HashMap<>();
    static Players players = new Players();
    private static Boolean useless;
    private static Boolean exit;
    private static Boolean fantasy;
    private static Boolean unlimitedFantasy;
    private static int totalP1;
    private static int totalP2;
    private static int totalP3;
    private static int p1,p2,p3;


    public void calculateScore(int numberOfGames) {

        //user input for glassbreak check
        Scanner input = new Scanner(System.in);

        //print player names in output file
        tableHeader();


        for (int i = 0; i < numberOfGames; i++) {

                exit = false;
                useless = false;
                fantasy = false;
                unlimitedFantasy = false;
                p1 = 0;
                p2 = 0;
                p3 = 0;

                System.out.println("Glassbreak for any player ? (Y/N) \n");

                glassBreak.put(players.getPLAYER1(), false);
                glassBreak.put(players.getPLAYER2(), false);
                glassBreak.put(players.getPLAYER3(), false);

                if (input.next().equalsIgnoreCase("y")) {
                   // Get glassbreak players and update map
                    checkGlassBreak();
                    useless = true;
                }

                //exit from current loop when all 3 players have glassbreak
                if (exit)
                    continue;

                System.out.println("High card(*)\tOne pair(1)\tTwo pair(2)\tThree of a kind(3)\nStraight(st)\nFlush(fl)\tFull house(fh)\tQuads(4)\tStr flush(sf)\nROYAL FLUSH(rf)\n");


                System.out.println("Enter BOTTOM row for ");
                rowResult(rowWise("bottom"));

                System.out.println("Enter MIDDLE row for ");
                rowResult(rowWise("middle"));

                System.out.println("Enter TOP row for ");
                rowResult(rowWise("top"));

               // Keep adding points from each game to print final total
                totalP1 += p1;
                totalP2 += p2;
                totalP3 += p3;

                // Output points of each game to file
                printResult(p1, p2, p3);

                glassBreak.clear();

                // Fantasy in Last game extends by one game
                if (i == numberOfGames - 1 && (fantasy || unlimitedFantasy))
                    i -= 1;

            }

        tableFooter();

    }

    private void rowResult(Map<String, Integer> rowMap) {
        int max = -2;
        String rowWinner = null;
        List<String> sameComboPlayer = new ArrayList<>();
        List<Integer> sameComboPoints = new ArrayList<>();
        Map<String, Integer> temp = new LinkedHashMap<>(rowMap);
        for (Map.Entry<String,Integer> entry : rowMap.entrySet()){
            temp.put(entry.getKey(), entry.getValue() - 1);
            if (entry.getValue() == max) {
                sameComboPlayer.add(entry.getKey());
                sameComboPoints.add(entry.getValue());
            }
            if (entry.getValue() > max) {
                max = entry.getValue();
                rowWinner = entry.getKey();
            }
        }

        // Equal or same combo , check for high card in same combo
        if (sameComboPoints.size() > 0 && sameComboPoints.get(0) == max) {
            Scanner equal = new Scanner(System.in);

            //check if 2 or 3 players have same combo
            if (sameComboPlayer.size() == 1)
                System.out.println("Who has higher?\n" + rowWinner + " (1) OR " + sameComboPlayer.get(0) + " (2)");
            else
                System.out.println("Who has higher?\n" + rowWinner + " (1) OR " + sameComboPlayer.get(0) + " (2) OR " + sameComboPlayer.get(1) + " (3)");

            String high = equal.next();

            switch (high) {
                case "1":
                    temp.put(rowWinner, temp.get(rowWinner) + 2);break;


                case "2":
                    temp.put(sameComboPlayer.get(0), temp.get(sameComboPlayer.get(0)) + 2);break;


                case "3":
                    temp.put(sameComboPlayer.get(1), temp.get(sameComboPlayer.get(1)) + 2);break;

            }
        }

        //single row winner
        else {

            temp.put(rowWinner, temp.get(rowWinner) + 2);
        }

        if (useless)
            updateGlassBreakPoints(temp, max+1);

        p1 += temp.get(players.getPLAYER1());
        p2 += temp.get(players.getPLAYER2());
        p3 += temp.get(players.getPLAYER3());

    }


    private void checkGlassBreak() {
        Scanner in = new Scanner(System.in);

        System.out.println("\nWhich player has glassbreak? \n enter numbers continously if 1+ players have glassbreak");
        System.out.println("\n" + players.getPLAYER1() + "(1)\t" + players.getPLAYER2() + "(2)\t" + players.getPLAYER3() + "(3)\n");
        String glassBreakPlayer = in.next();
        if (glassBreakPlayer.contains(String.valueOf(1)))
            glassBreak.put(players.getPLAYER1(), true);
        if (glassBreakPlayer.contains(String.valueOf(2)))
            glassBreak.put(players.getPLAYER2(), true);
        if (glassBreakPlayer.contains(String.valueOf(3)))
            glassBreak.put(players.getPLAYER3(), true);
        // when all three players have glassbreak
        if(glassBreakPlayer.contains(String.valueOf(123)))
            exit=true;

    }

    private Map<String, Integer> rowWise(String row) {
        Scanner in = new Scanner(System.in);
        HashMap<String, Integer> rowDraw = new HashMap<>();

        if (!glassBreak.get(players.getPLAYER1())) {
            System.out.println(players.getPLAYER1());
            rowDraw.put(players.getPLAYER1(), calculatePoints(in.next(), row,players.getPLAYER1()));
        }
        if (!glassBreak.get(players.getPLAYER2())) {
            System.out.println("\n" + players.getPLAYER2());
            rowDraw.put(players.getPLAYER2(), calculatePoints(in.next(), row,players.getPLAYER2()));
        }

        if (!glassBreak.get(players.getPLAYER3())) {
            System.out.println("\n" + players.getPLAYER3());
            rowDraw.put(players.getPLAYER3(), calculatePoints(in.next(), row,players.getPLAYER3()));
        }
        return rowDraw;
    }



    private void updateGlassBreakPoints(Map<String, Integer> temp, int max) {

        if (glassBreak.get(players.getPLAYER1()))
            temp.put(players.getPLAYER1(), ((max) * -1));
        if (glassBreak.get(players.getPLAYER2()))
            temp.put(players.getPLAYER2(), ((max) * -1));
        if (glassBreak.get(players.getPLAYER3()))
            temp.put(players.getPLAYER3(), ((max) * -1));


    }


    private int calculatePoints(String combo, String row,String player) {

        combo = combo.toLowerCase();
        boolean bottom = row.equalsIgnoreCase("bottom");
        boolean middle = row.equalsIgnoreCase("middle");
        boolean top = row.equalsIgnoreCase("top");

        if (combo.startsWith("one") || combo.contains("1") || combo.equalsIgnoreCase("pair") || combo.equalsIgnoreCase("op")) {
            if (top) {
                int points= topRowPairPoints();
                if (points>=7) {
                    System.out.println("FANTASY !!!");
                    fantasy=true;
                }
                return points;
            } else return 0;
        } else if (combo.startsWith("two") || combo.contains("2") || combo.equalsIgnoreCase("two pair") || combo.equalsIgnoreCase("tp")) {
            if (bottom)
                return 0;
            else if (middle)
                return Middle.TWO_PAIR.getValue();
        } else if (combo.startsWith("three") || combo.contains("3") || combo.equalsIgnoreCase("tok")) {
            if (top) {
                System.out.println("FANTASY !!!");
                fantasy=true;
                unlimitedFantasy=true;
                return topRowTOKPoints();
            } else if (bottom)
                return Bottom.THREE_OF_A_KIND.getValue();
            else
                return Middle.THREE_OF_A_KIND.getValue();
        } else if (combo.startsWith("str") || combo.equalsIgnoreCase("st")) {
            if (bottom)
                return Bottom.STRAIGHT.getValue();
            else if (middle)
                return Middle.STRAIGHT.getValue();
        } else if (combo.startsWith("flu") || combo.equalsIgnoreCase("fl")) {
            if (bottom)
                return Bottom.FLUSH.getValue();
            else if (middle)
                return Middle.FLUSH.getValue();
        } else if (combo.startsWith("full") || combo.equalsIgnoreCase("fh")) {
            if (bottom)
                return Bottom.FULL_HOUSE.getValue();
            else if (middle)
                return Middle.FULL_HOUSE.getValue();
        } else if (combo.equalsIgnoreCase("sf")) {
            if (bottom)
                return Bottom.STRAIGHT_FLUSH.getValue();
            else if (middle)
                return Middle.STRAIGHT_FLUSH.getValue();
        } else if (combo.startsWith("four") || combo.equalsIgnoreCase("4") || combo.equalsIgnoreCase("q")) {
            if (bottom) {
                unlimitedFantasy=true;
                return Bottom.FOUR_OF_A_KIND.getValue();
            }
            else if (middle) {
                unlimitedFantasy=true;
                return Middle.FOUR_OF_A_KIND.getValue();
            }
        } else if (combo.startsWith("ro") || combo.equalsIgnoreCase("rf")) {
            if (bottom)
                return Bottom.ROYAL_FLUSH.getValue();
            else if (middle)
                return Middle.ROYAL_FLUSH.getValue();
        }

        return 0;
    }


    private int topRowTOKPoints() {
        Scanner top = new Scanner(System.in);
        System.out.println("Which Three of a kind?");
        String tok = top.next();

        switch (tok.toLowerCase()) {

            case "2":
                return Top.TWO_TOAK.getValue();
            case "3":
                return Top.THREE_TOAK.getValue();
            case "4":
                return Top.FOUR_TOAK.getValue();
            case "5":
                return Top.FIVE_TOAK.getValue();
            case "6":
                return Top.SIX_TOAK.getValue();
            case "7":
                return Top.SEVEN_TOAK.getValue();
            case "8":
                return Top.EIGHT_TOAK.getValue();
            case "9":
                return Top.NINE_TOAK.getValue();
            case "10":
                return Top.TEN_TOAK.getValue();
            case "j":
                return Top.JACK_TOAK.getValue();
            case "q":
                return Top.QUEEN_TOAK.getValue();
            case "k":
                return Top.KING_TOAK.getValue();
            case "a":
                return Top.ACE_TOAK.getValue();
            default:
                return 0;
        }

    }

    private int topRowPairPoints() {
        Scanner top = new Scanner(System.in);

        System.out.println("Which pair?\n");
        String pair = top.next();

        switch (pair.toLowerCase()) {

            case "6":
                return Top.SIX_PAIR.getValue();
            case "7":
                return Top.SEVEN_PAIR.getValue();
            case "8":
                return Top.EIGHT_PAIR.getValue();
            case "9":
                return Top.NINE_PAIR.getValue();
            case "10":
                return Top.TEN_PAIR.getValue();
            case "j":
                return Top.JACK_PAIR.getValue();
            case "q":
                return Top.QUEEN_PAIR.getValue();
            case "k":
                return Top.KING_PAIR.getValue();
            case "a":
                return Top.ACE_PAIR.getValue();
            default:
                return 0;

        }


    }

    private void printResult(int p1, int p2, int p3) {

        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {

            fileWriter = new FileWriter(FILENAME, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(String.format("%-14s%-14s%-14s%1s \r\n","|  "+p1,"|  "+p2,"|  "+p3,"|"));
            bufferedWriter.newLine();


        } catch (IOException e) {
            System.err.println("Error writing the file : ");
            e.printStackTrace();
        } finally {

            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void tableHeader(){

        String head ="+-------------";
        Writer writer=null;
        BufferedWriter bufferedWriter= null;
        try {
             writer = new FileWriter(FILENAME,true);
             bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.append(String.format("%-14s%-14s%-14s%1s \r\n",head,head,head,"+"));
            bufferedWriter.append(String.format("%-14s%-14s%-14s%1s\r\n","| "+players.getPLAYER1(),"| "+players.getPLAYER2(),"| "+players.getPLAYER3(),"|"));
            bufferedWriter.append(String.format("%-14s%-14s%-14s%1s \r\n",head,head,head,"+"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void tableFooter() {

        String foot ="+-------------";
        Writer writer=null;
        BufferedWriter bufferedWriter=null;

        try {
             writer = new FileWriter(FILENAME,true);
             bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.append(String.format("%-14s%-14s%-14s%1s \r\n",foot,foot,foot,"+"));
            bufferedWriter.append(String.format("%-14s%-14s%-14s%1s\r\n","| "+totalP1,"| "+totalP2,"| "+totalP3,"|"));
            bufferedWriter.append(String.format("%-14s%-14s%-14s%1s \r\n",foot,foot,foot,"+"));

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        //Provide player names
        SetPlayerName playerName = new SetPlayerName();
        playerName.playerName(players);

        Score score = new Score();
        System.out.println("Enter Number of games to be played : ");
        Scanner games = new Scanner(System.in);
        score.calculateScore(games.nextInt());
    }
}


