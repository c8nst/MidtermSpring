import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameState {
    private final ArrayList<String> playerNames = new ArrayList<String>();
    private final ArrayList<Boolean> humanPlayers = new ArrayList<Boolean>();
    private final ArrayList<ArrayList<String>> hands = new ArrayList<ArrayList<String>>();
    private final ArrayList<String> deck = new ArrayList<String>();
    private final ArrayList<String> discard = new ArrayList<String>();
    private final int[] scores = new int[10];
    private int currentPlayer = 0;
    private int direction = 1;
    private String upCard = "";
    private String calledColor = "";

    public void setupPlayers(int bots, boolean human) {
        playerNames.clear();
        humanPlayers.clear();
        hands.clear();
        if (human) {
            playerNames.add("You");
            humanPlayers.add(Boolean.TRUE);
            hands.add(new ArrayList<String>());
        }
        for (int i = 1; i <= bots; i++) {
            playerNames.add("Bot" + i);
            humanPlayers.add(Boolean.FALSE);
            hands.add(new ArrayList<String>());
        }
    }

    public void resetHandsAndPiles() {
        deck.clear();
        discard.clear();
        for (int i = 0; i < hands.size(); i++) {
            hands.get(i).clear();
        }
        calledColor = "";
        direction = 1;
    }

    public void buildDeck() {
        String[] colors = {"R", "Y", "G", "B"};
        for (int c = 0; c < colors.length; c++) {
            deck.add(colors[c] + "0");
            for (int n = 1; n <= 9; n++) {
                deck.add(colors[c] + n);
                deck.add(colors[c] + n);
            }
            deck.add(colors[c] + "S");
            deck.add(colors[c] + "S");
            deck.add(colors[c] + "R");
            deck.add(colors[c] + "R");
            deck.add(colors[c] + "+2");
            deck.add(colors[c] + "+2");
        }
        for (int i = 0; i < 4; i++) {
            deck.add("W");
            deck.add("W4");
        }
    }

    public void dealInitialHands(Random random, int cardsPerPlayer) {
        Collections.shuffle(deck, random);
        for (int i = 0; i < playerNames.size(); i++) {
            for (int j = 0; j < cardsPerPlayer; j++) {
                hands.get(i).add(draw(random));
            }
        }
        upCard = draw(random);
        while (upCard.startsWith("W")) {
            discard.add(upCard);
            upCard = draw(random);
        }
        currentPlayer = random.nextInt(playerNames.size());
    }

    public String draw(Random random) {
        if (deck.size() == 0) {
            deck.addAll(discard);
            discard.clear();
            Collections.shuffle(deck, random);
        }
        if (deck.size() == 0) {
            return "W";
        }
        return deck.remove(0);
    }

    public void advanceTurn() {
        currentPlayer += direction;
        if (currentPlayer >= playerNames.size()) {
            currentPlayer = 0;
        }
        if (currentPlayer < 0) {
            currentPlayer = playerNames.size() - 1;
        }
    }

    public int calculateWinScore(int winnerIndex) {
        int points = 0;
        for (int i = 0; i < hands.size(); i++) {
            if (i != winnerIndex) {
                for (int j = 0; j < hands.get(i).size(); j++) {
                    points += Card.of(hands.get(i).get(j)).points();
                }
            }
        }
        return points;
    }

    public void awardWin(int winnerIndex) {
        scores[winnerIndex] += calculateWinScore(winnerIndex);
    }

    public List<String> playerNames() {
        return playerNames;
    }

    public List<Boolean> humanPlayers() {
        return humanPlayers;
    }

    public List<ArrayList<String>> hands() {
        return hands;
    }

    public int[] scores() {
        return scores;
    }

    public int currentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int direction() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String upCard() {
        return upCard;
    }

    public void setUpCard(String upCard) {
        this.upCard = upCard;
    }

    public String calledColor() {
        return calledColor;
    }

    public void setCalledColor(String calledColor) {
        this.calledColor = calledColor;
    }

    public List<String> deck() {
        return deck;
    }

    public List<String> discard() {
        return discard;
    }
}
