package org.nschmidt.abalone.playfield;

import java.awt.Color;

public interface PlayerLogic {
    public Player switchPlayer();
    public int getNumber();
    public char getChar();
    public Color getColor();
}
