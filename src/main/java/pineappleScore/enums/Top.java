package pineappleScore.enums;

public enum Top {
    SIX_PAIR(1),
    SEVEN_PAIR(2),
    EIGHT_PAIR(3),
    NINE_PAIR(4),
    TEN_PAIR(5),
    JACK_PAIR(6),
    QUEEN_PAIR(7),
    KING_PAIR(8),
    ACE_PAIR(9),
    TWO_TOAK(10),
    THREE_TOAK(11),
    FOUR_TOAK(12),
    FIVE_TOAK(13),
    SIX_TOAK(14),
    SEVEN_TOAK(15),
    EIGHT_TOAK(16),
    NINE_TOAK(17),
    TEN_TOAK(18),
    JACK_TOAK(19),
    QUEEN_TOAK(20),
    KING_TOAK(21),
    ACE_TOAK(22);

    private int value;
     Top(int value){this.value=value;}
    public int getValue(){return this.value;}

}
