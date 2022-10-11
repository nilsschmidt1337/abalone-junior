package org.nschmidt.abalone;

import java.math.BigInteger;

public enum Player implements PlayerLogic{
    BLACK {
        @Override
        public Player switchPlayer() {
            return WHITE;
        }

        @Override
        public BigInteger getNumber() {
            return BigInteger.TWO;
        }
    },
    WHITE {
        @Override
        public Player switchPlayer() {
            return BLACK;
        }

        @Override
        public BigInteger getNumber() {
            return BigInteger.ONE;
        }
    },
    EMPTY {
        @Override
        public Player switchPlayer() {
            return EMPTY;
        }

        @Override
        public BigInteger getNumber() {
            return BigInteger.ZERO;
        }
    };

    static Player valueOf(BigInteger bigInteger) {
        return switch (bigInteger.intValue()) {
        case 1 -> WHITE;
        case 2 -> BLACK;
        default -> EMPTY;
        };
    }
}
