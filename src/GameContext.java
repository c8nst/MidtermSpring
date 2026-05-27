import java.util.Random;

public final class GameContext {
    private static Random random = new Random();

    private GameContext() {
    }

    public static Random random() {
        return random;
    }

    public static void setRandom(Random newRandom) {
        random = newRandom;
    }
}
