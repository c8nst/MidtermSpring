import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int bots = 3;
        int games = 1;
        boolean human = false;
        boolean quiet = false;
        long seed = System.currentTimeMillis();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--bots") && i + 1 < args.length) {
                bots = Integer.parseInt(args[++i]);
            } else if (args[i].equals("--games") && i + 1 < args.length) {
                games = Integer.parseInt(args[++i]);
            } else if (args[i].equals("--human")) {
                human = true;
            } else if (args[i].equals("--quiet")) {
                quiet = true;
            } else if (args[i].equals("--seed") && i + 1 < args.length) {
                seed = Long.parseLong(args[++i]);
            } else if (args[i].equals("--self-test")) {
                selfTest();
                return;
            } else if (args[i].equals("--help")) {
                System.out.println("Usage: scripts/run.sh [--bots N] [--games N] [--human] [--quiet] [--seed N]");
                return;
            }
        }

        GameContext.setRandom(new java.util.Random(seed));
        GameState state = new GameState();
        ConsoleUI ui = new ConsoleUI(new Scanner(System.in));
        ui.setQuiet(quiet);

        state.setupPlayers(bots, human);

        if (state.playerNames().size() < 2 || state.playerNames().size() > 4) {
            System.out.println("UNO needs 2 to 4 players.");
            return;
        }

        GameEngine engine = new GameEngine(state, ui);

        for (int g = 1; g <= games; g++) {
            ui.printGameHeader(g);
            engine.playGame();
        }

        ui.printFinalScores(state.playerNames(), state.scores());
    }

    static void selfTest() {
        CharacterizationTests.main(new String[0]);
    }
}
