package org.nschmidt.abalone.playfield;

import java.util.Objects;

public class ScoreCacheEntry extends Field {

    private Player player;
    private Player toMove;
    
    private static final ScoreCacheEntry DUMMY = new ScoreCacheEntry(Field.EMPTY_FIELD, Player.BLACK, Player.BLACK);
    
    public ScoreCacheEntry(Field state, Player player, Player toMove) {
        super();
        this.black = state.black;
        this.white = state.white;
        this.player = player;
        this.toMove = toMove;
    }
    
    public static ScoreCacheEntry of(Field state, Player player, Player toMove) {
        DUMMY.black = state.black;
        DUMMY.white = state.white;
        DUMMY.player = player;
        DUMMY.toMove = toMove;
        return DUMMY;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ScoreCacheEntry other) && player == other.player && toMove == other.toMove && black == other.black && white == other.white;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(black, white, player, toMove);
    }
}
