package pineappleScore.enums;

public enum Middle {

    TWO_PAIR(1),
    THREE_OF_A_KIND(2),
    STRAIGHT(4),
    FLUSH(8),
    FULL_HOUSE(12),
    FOUR_OF_A_KIND(20),
    STRAIGHT_FLUSH(30),
    ROYAL_FLUSH(50);

    private int value;

    Middle(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
