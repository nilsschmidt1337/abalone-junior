package org.nschmidt.abalone;

import static java.lang.System.out;

import java.math.BigInteger;

public enum FieldPrinter {
    INSTANCE;
    
    private static final BigInteger THREE = BigInteger.valueOf(3L);
    
    public static void printField(BigInteger state) {
        BigInteger nextValue = state;
        printSpaces(3);
        for (int i = 0; i < 37; i++) {
            BigInteger fieldValue = nextValue.remainder(THREE);
            nextValue = nextValue.divide(THREE);
            breakLine(i);
            out.print(fieldValue);
            printSpaces(1);
        }
        
        out.println();
        out.println();
    }
    
    public static void printFieldDelta(BigInteger state, BigInteger previousState) {
        BigInteger previousValue = previousState;
        BigInteger nextValue = state;
        printSpaces(3);
        for (int i = 0; i < 37; i++) {
            BigInteger fieldValue = nextValue.remainder(THREE);
            BigInteger previousFieldValue = previousValue.remainder(THREE);
            nextValue = nextValue.divide(THREE);
            previousValue = previousValue.divide(THREE);
            breakLine(i);
            out.print(fieldValue);
            if (fieldValue == previousFieldValue) {
                printSpaces(1);
            } else {
                out.print('_');
            }
        }
        
        out.println();
        out.println();
    }

    private static void printSpaces(int count) {
        for (int i = 0; i < count; i++) {
            out.print(' ');
        }
    }

    private static void breakLine(int i) {
        switch (i) {
        case 4, 28:
            out.println();
            printSpaces(2);
            break;
        case 9, 22:
            out.println();
            printSpaces(1);
            break;
        case 15:
            out.println();
            printSpaces(0);
            break;
        case 33:
            out.println();
            printSpaces(3);
            break;
        default:
        }
    }
}
