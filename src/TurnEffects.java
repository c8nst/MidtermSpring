import java.util.Random;

public final class TurnEffects {
    private TurnEffects() {
    }

    public static void applyAfterPlay(GameState state, ConsoleUI ui, String card) {
        String rank = Card.of(card).rank();
        Random random = GameContext.random();

        if (rank.equals("SKIP")) {
            state.advanceTurn();
            state.advanceTurn();
        } else if (rank.equals("REVERSE")) {
            state.setDirection(state.direction() * -1);
            if (state.playerNames().size() == 2) {
                state.advanceTurn();
                state.advanceTurn();
            } else {
                state.advanceTurn();
            }
        } else if (rank.equals("DRAW_TWO")) {
            state.advanceTurn();
            String victim = state.playerNames().get(state.currentPlayer());
            drawCardsToCurrentPlayer(state, random, 2);
            ui.printDrawCards(victim, 2);
            state.advanceTurn();
        } else if (rank.equals("WILD_DRAW_FOUR")) {
            state.advanceTurn();
            String victim = state.playerNames().get(state.currentPlayer());
            drawCardsToCurrentPlayer(state, random, 4);
            ui.printDrawCards(victim, 4);
            state.advanceTurn();
        } else {
            state.advanceTurn();
        }
    }

    private static void drawCardsToCurrentPlayer(GameState state, Random random, int count) {
        for (int i = 0; i < count; i++) {
            state.hands().get(state.currentPlayer()).add(state.draw(random));
        }
    }
}
