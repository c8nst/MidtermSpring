import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private boolean quiet;

    public ConsoleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setQuiet(boolean quiet) {
        this.quiet = quiet;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public void printGameHeader(int gameNumber) {
        if (!quiet) {
            System.out.println("\n=== Game " + gameNumber + " ===");
        }
    }

    public void printTurn(String playerName, List<String> hand, String upCard, String calledColor) {
        if (!quiet) {
            System.out.println("\nUp card: " + upCard + (calledColor.equals("") ? "" : " called " + calledColor));
            System.out.println(playerName + " hand: " + formatHand(hand));
        }
    }

    public void printDraw(String playerName, String drawn) {
        if (!quiet) {
            System.out.println(playerName + " draws " + drawn);
        }
    }

    public void printPenaltyDraw(String playerName, String reason) {
        if (!quiet) {
            System.out.println(playerName + " " + reason);
        }
    }

    public void printPlay(String playerName, String card) {
        if (!quiet) {
            System.out.println(playerName + " plays " + card);
        }
    }

    public void printCalledColor(String playerName, String color) {
        if (!quiet) {
            System.out.println(playerName + " calls " + color);
        }
    }

    public void printUno(String playerName) {
        if (!quiet) {
            System.out.println(playerName + " says UNO!");
        }
    }

    public void printWin(String playerName, int points) {
        if (!quiet) {
            System.out.println(playerName + " wins and scores " + points);
        }
    }

    public void printDrawCards(String playerName, int count) {
        if (!quiet) {
            System.out.println(playerName + " draws " + (count == 2 ? "two." : "four."));
        }
    }

    public void printSafetyLimit() {
        if (!quiet) {
            System.out.println("Game stopped at safety limit.");
        }
    }

    public void printFinalScores(List<String> playerNames, int[] scores) {
        System.out.println("\nFinal scores:");
        for (int i = 0; i < playerNames.size(); i++) {
            System.out.println(playerNames.get(i) + ": " + scores[i]);
        }
    }

    public int askHumanCardChoice(List<String> hand, String upCard, String calledColor) {
        while (true) {
            System.out.print("Choose card index/code or draw: ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("DRAW")) {
                return -1;
            }
            try {
                int index = Integer.parseInt(input);
                if (index >= 0 && index < hand.size()) {
                    return index;
                }
            } catch (Exception ignored) {
            }
            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).equals(input)) {
                    if (PlayRules.isLegal(hand.get(i), upCard, calledColor)) {
                        return i;
                    }
                    System.out.println("That card is not legal.");
                }
            }
            System.out.println("Card not found.");
        }
    }

    public boolean askPlayDrawnCard(String drawn) {
        System.out.print("Play drawn card " + drawn + "? y/n: ");
        String answer = scanner.nextLine();
        return answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes");
    }

    public String askColor() {
        while (true) {
            System.out.print("Call color R/Y/G/B: ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("R")) {
                return "R";
            }
            if (input.equals("Y")) {
                return "Y";
            }
            if (input.equals("G")) {
                return "G";
            }
            if (input.equals("B")) {
                return "B";
            }
            System.out.println("Bad color.");
        }
    }

    public static String formatHand(List<String> hand) {
        String out = "";
        for (int i = 0; i < hand.size(); i++) {
            out += i + ":" + hand.get(i);
            if (i < hand.size() - 1) {
                out += " ";
            }
        }
        return out;
    }
}
