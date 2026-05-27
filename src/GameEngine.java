import java.util.ArrayList;
import java.util.Random;

public class GameEngine {
    private final GameState state;
    private final ConsoleUI ui;

    public GameEngine(GameState state, ConsoleUI ui) {
        this.state = state;
        this.ui = ui;
    }

    public void playGame() {
        state.resetHandsAndPiles();
        state.buildDeck();
        Random random = GameContext.random();
        state.dealInitialHands(random, 7);

        int guard = 0;
        while (guard < 3000) {
            guard++;
            if (!playOneTurn(random)) {
                return;
            }
        }
        ui.printSafetyLimit();
    }

    private boolean playOneTurn(Random random) {
        int playerIndex = state.currentPlayer();
        String name = state.playerNames().get(playerIndex);
        ArrayList<String> hand = state.hands().get(playerIndex);

        ui.printTurn(name, hand, state.upCard(), state.calledColor());

        int chosen = state.humanPlayers().get(playerIndex).booleanValue()
                ? ui.askHumanCardChoice(hand, state.upCard(), state.calledColor())
                : BotStrategy.chooseCardIndex(hand, state.upCard(), state.calledColor());

        chosen = resolveDrawChoice(playerIndex, name, hand, chosen, random);

        if (chosen >= 0) {
            return playChosenCard(playerIndex, name, hand, chosen, random);
        }

        state.advanceTurn();
        return true;
    }

    private int resolveDrawChoice(int playerIndex, String name, ArrayList<String> hand, int chosen, Random random) {
        if (chosen != -1) {
            return chosen;
        }

        String drawn = state.draw(random);
        hand.add(drawn);
        ui.printDraw(name, drawn);

        if (!PlayRules.isLegal(drawn, state.upCard(), state.calledColor())) {
            return -1;
        }

        if (!state.humanPlayers().get(playerIndex).booleanValue()) {
            return hand.size() - 1;
        }

        if (ui.askPlayDrawnCard(drawn)) {
            return hand.size() - 1;
        }
        return -1;
    }

    private boolean playChosenCard(int playerIndex, String name, ArrayList<String> hand, int chosen, Random random) {
        if (chosen >= hand.size()) {
            ui.printPenaltyDraw(name, "selected an invalid index and draws a penalty card.");
            hand.add(state.draw(random));
            state.advanceTurn();
            return true;
        }

        String card = hand.get(chosen);
        if (!PlayRules.isLegal(card, state.upCard(), state.calledColor())) {
            ui.printPenaltyDraw(name, "tried illegal card " + card + " and draws a penalty card.");
            hand.add(state.draw(random));
            state.advanceTurn();
            return true;
        }

        hand.remove(chosen);
        state.discard().add(state.upCard());
        state.setUpCard(card);
        state.setCalledColor("");
        ui.printPlay(name, card);

        if (card.equals("W") || card.equals("W4")) {
            String color = state.humanPlayers().get(playerIndex).booleanValue()
                    ? ui.askColor()
                    : BotStrategy.chooseColor(hand);
            state.setCalledColor(color);
            ui.printCalledColor(name, color);
        }

        if (hand.size() == 1) {
            ui.printUno(name);
        }

        if (hand.size() == 0) {
            int points = state.calculateWinScore(playerIndex);
            state.awardWin(playerIndex);
            ui.printWin(name, points);
            return false;
        }

        TurnEffects.applyAfterPlay(state, ui, card);
        return true;
    }
}
