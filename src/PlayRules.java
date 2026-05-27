public final class PlayRules {
    private PlayRules() {
    }

    public static boolean isLegal(Card card, Card upCard, String calledColor) {
        if (card.isWild()) {
            return true;
        }
        if (card.color().equals(upCard.color())) {
            return true;
        }
        if (!calledColor.equals("") && card.color().equals(calledColor)) {
            return true;
        }
        if (card.rank().equals(upCard.rank()) && !card.rank().equals("NUMBER")) {
            return true;
        }
        if (card.rank().equals("NUMBER") && upCard.rank().equals("NUMBER") && card.number() == upCard.number()) {
            return true;
        }
        return false;
    }

    public static boolean isLegal(String card, String upCard, String calledColor) {
        return isLegal(Card.of(card), Card.of(upCard), calledColor);
    }
}
