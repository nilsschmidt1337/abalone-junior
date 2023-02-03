package org.nschmidt.abalone.playfield;

import java.util.Objects;

public class Transposition extends Field {

    private int depth;
    
    private static final Transposition DUMMY = new Transposition(0, Field.EMPTY_FIELD);
    
    public Transposition(int depth, Field state) {
        super();
        this.black = state.black;
        this.white = state.white;
        this.depth = depth;
    }
    
    public static Transposition of(int depth, Field state) {
        DUMMY.black = state.black;
        DUMMY.white = state.white;
        DUMMY.depth = depth;
        return DUMMY;
    }
    
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Transposition other) && black == other.black && white == other.white && depth == other.depth;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(black, white, depth);
    }
}
