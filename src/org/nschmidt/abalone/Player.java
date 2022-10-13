package org.nschmidt.abalone;

import java.awt.Color;

public enum Player implements PlayerLogic{
    BLACK {
        @Override
        public Player switchPlayer() {
            return WHITE;
        }

        @Override
        public int getNumber() {
            return 2;
        }

        @Override
        public Color getColor() {
            return Color.BLACK;
        }

        @Override
        public char getChar() {
            return 'X';
        }
    },
    WHITE {
        @Override
        public Player switchPlayer() {
            return BLACK;
        }

        @Override
        public int getNumber() {
            return 1;
        }

        @Override
        public Color getColor() {
            return Color.WHITE;
        }

        @Override
        public char getChar() {
            return 'O';
        }
    },
    EMPTY {
        @Override
        public Player switchPlayer() {
            return EMPTY;
        }

        @Override
        public int getNumber() {
            return 0;
        }

        @Override
        public Color getColor() {
            return Color.BLUE;
        }

        @Override
        public char getChar() {
            return '·';
        }
    };

    static Player valueOf(int playerNumber) {
        return switch (playerNumber) {
        case 1 -> WHITE;
        case 2 -> BLACK;
        default -> EMPTY;
        };
    }
}
