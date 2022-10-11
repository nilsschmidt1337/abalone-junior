package org.nschmidt.abalone;

import java.awt.Color;
import java.math.BigInteger;

public interface PlayerLogic {
    public Player switchPlayer();
    public BigInteger getNumber();
    public Color getColor();
}
