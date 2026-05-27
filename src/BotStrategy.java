import java.util.ArrayList;
import java.util.List;

public final class BotStrategy {
    private BotStrategy() {
    }

    public static int chooseCardIndex(List<String> hand, String upCard, String calledColor) {
        int drawTwo = findFirstLegalByRank(hand, upCard, calledColor, "DRAW_TWO");
        if (drawTwo >= 0) {
            return drawTwo;
        }
        int skip = findFirstLegalByRank(hand, upCard, calledColor, "SKIP");
        if (skip >= 0) {
            return skip;
        }
        int number = findFirstLegalByRank(hand, upCard, calledColor, "NUMBER");
        if (number >= 0) {
            return number;
        }
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).startsWith("W")) {
                return i;
            }
        }
        return -1;
    }

    public static String chooseColor(List<String> hand) {
        int r = 0;
        int y = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < hand.size(); i++) {
            String c = Card.of(hand.get(i)).color();
            if (c.equals("R")) {
                r++;
            } else if (c.equals("Y")) {
                y++;
            } else if (c.equals("G")) {
                g++;
            } else if (c.equals("B")) {
                b++;
            }
        }
        if (r >= y && r >= g && r >= b) {
            return "R";
        } else if (y >= r && y >= g && y >= b) {
            return "Y";
        } else if (g >= r && g >= y && g >= b) {
            return "G";
        } else {
            return "B";
        }
    }

    private static int findFirstLegalByRank(List<String> hand, String upCard, String calledColor, String targetRank) {
        for (int i = 0; i < hand.size(); i++) {
            String card = hand.get(i);
            if (Card.of(card).rank().equals(targetRank) && PlayRules.isLegal(card, upCard, calledColor)) {
                return i;
            }
        }
        return -1;
    }
}
