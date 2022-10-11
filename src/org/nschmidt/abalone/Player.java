package org.nschmidt.abalone;

public enum Player implements PlayerLogic{
    BLACK {
        @Override
        public Player switchPlayer() {
            return WHITE;
        }

        @Override
        public long getNumber() {
            return 2;
        }
    },
    WHITE {
        @Override
        public Player switchPlayer() {
            return BLACK;
        }

        @Override
        public long getNumber() {
            return 1;
        }
    },
    EMPTY {
        @Override
        public Player switchPlayer() {
            return EMPTY;
        }

        @Override
        public long getNumber() {
            return 0;
        }
    };

    static Player valueOf(long remainderUnsigned) {
        return switch ((int) remainderUnsigned) {
        case 1 -> WHITE;
        case 2 -> BLACK;
        default -> EMPTY;
        };
    }
}
