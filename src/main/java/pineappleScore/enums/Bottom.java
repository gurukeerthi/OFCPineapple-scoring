package pineappleScore.enums;

public enum Bottom {

    THREE_OF_A_KIND(1),
    STRAIGHT(2),
    FLUSH(4),
    FULL_HOUSE(6),
    FOUR_OF_A_KIND(10),
    STRAIGHT_FLUSH(15),
    ROYAL_FLUSH(25);

    private int value=0;

    Bottom(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
