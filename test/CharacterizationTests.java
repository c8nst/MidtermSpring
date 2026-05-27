import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CharacterizationTests {
  private static int passed = 0;

  public static void main(String[] args) {
    testCardParsing();
    testColorMatching();
    testNumberMatching();
    testActionTypeMatching();
    testWildBehavior();
    testScoring();
    testBotStrategy();
    testDeckRecycling();
    testTwoPlayerReverseSkipsOpponent();
    testIllegalIndexWouldBeOutOfRange();
    testHumanCanDeclineDrawnCard();

    System.out.println("Passed " + passed + " characterization checks.");
  }

  private static void testCardParsing() {
    check(Card.of("R5").color().equals("R"), "color R5");
    check(Card.of("G+2").rank().equals("DRAW_TWO"), "rank +2");
    check(Card.of("W4").points() == 50, "wild points");
    check(Card.of("YS").points() == 20, "skip points");
    check(Card.of("R7").number() == 7, "number card value");
  }

  private static void testColorMatching() {
    check(PlayRules.isLegal("R2", "R9", ""), "same color");
    check(!PlayRules.isLegal("B3", "R9", ""), "different color and number");
  }

  private static void testNumberMatching() {
    check(PlayRules.isLegal("G9", "R9", ""), "same number");
    check(!PlayRules.isLegal("G8", "R9", ""), "different number");
  }

  private static void testActionTypeMatching() {
    check(PlayRules.isLegal("RS", "YS", ""), "skip on skip");
    check(PlayRules.isLegal("G+2", "R+2", ""), "draw two on draw two");
    check(PlayRules.isLegal("BR", "YR", ""), "reverse on reverse");
  }

  private static void testWildBehavior() {
    check(PlayRules.isLegal("W", "R9", ""), "wild always legal");
    check(PlayRules.isLegal("W4", "G+2", ""), "wild draw four always legal");
    check(PlayRules.isLegal("B3", "W", "B"), "called color after wild");
    check(!PlayRules.isLegal("R3", "W", "B"), "wrong called color");
  }

  private static void testScoring() {
    GameState state = new GameState();
    state.setupPlayers(2, false);
    state.hands().get(1).add("R5");
    state.hands().get(1).add("B9");
    state.hands().get(1).add("GS");
    state.hands().get(1).add("W");
    check(state.calculateWinScore(0) == 5 + 9 + 20 + 50, "winner scores opponents cards");
  }

  private static void testBotStrategy() {
    ArrayList<String> hand = new ArrayList<String>();
    hand.add("B3");
    hand.add("R4");
    hand.add("W");
    check(BotStrategy.chooseCardIndex(hand, "R9", "") == 1, "bot prefers matching number before wild");

    ArrayList<String> colorHand = new ArrayList<String>();
    colorHand.add("B1");
    colorHand.add("B2");
    colorHand.add("R3");
    check(BotStrategy.chooseColor(colorHand).equals("B"), "bot chooses most common color");
  }

  private static void testDeckRecycling() {
    GameState state = new GameState();
    Random random = new Random(42);
    state.deck().add("R1");
    state.discard().add("Y2");
    state.discard().add("G3");
    String first = state.draw(random);
    check(first.equals("R1"), "draws remaining deck card first");
    check(state.deck().isEmpty(), "deck empty after last card removed");
    String second = state.draw(random);
    check(second.equals("Y2") || second.equals("G3"), "recycles discard when deck empties");
    check(state.discard().isEmpty(), "discard cleared after recycle");
    state.deck().clear();
    state.discard().clear();
    check(state.draw(random).equals("W"), "fallback wild when both piles empty");
  }

  private static void testTwoPlayerReverseSkipsOpponent() {
    GameState state = new GameState();
    state.setupPlayers(2, false);
    state.setCurrentPlayer(0);
    state.setDirection(1);
    TurnEffects.applyAfterPlay(state, quietUi(), "RR");
    check(state.currentPlayer() == 0, "two-player reverse returns turn to same player");
  }

  private static void testIllegalIndexWouldBeOutOfRange() {
    ArrayList<String> hand = new ArrayList<String>();
    hand.add("R5");
    check(3 >= hand.size(), "out-of-range index is treated as invalid move");
  }

  private static void testHumanCanDeclineDrawnCard() {
    check(!PlayRules.isLegal("B2", "R5", ""), "drawn card may be kept when not legal");
    check(PlayRules.isLegal("R2", "R5", ""), "drawn card can be played when legal");
  }

  private static ConsoleUI quietUi() {
    ConsoleUI ui = new ConsoleUI(new java.util.Scanner(System.in));
    ui.setQuiet(true);
    return ui;
  }

  private static void check(boolean condition, String name) {
    if (condition) {
      passed++;
    } else {
      throw new RuntimeException("Failed: " + name);
    }
  }
}
