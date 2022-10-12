package org.nschmidt.abalone;

import static java.lang.System.out;

import java.math.BigInteger;

public enum FieldPrinter {
    INSTANCE;
    
    public static void printField(BigInteger state) {
        printSpaces(3);
        for (int i = 0; i < 37; i++) {
            int fieldValue = (state.testBit(i) ? 1 : 0) + (state.testBit(i + 37) ? 2 : 0);
            breakLine(i);
            out.print(fieldValue);
            printSpaces(1);
        }
        
        out.println();
        out.println();
    }
    
    public static void printFieldDelta(BigInteger state, BigInteger previousState) {
        printSpaces(3);
        for (int i = 0; i < 37; i++) {
            int fieldValue = (state.testBit(i) ? 1 : 0) + (state.testBit(i + 37) ? 2 : 0);
            int previousFieldValue = (previousState.testBit(i) ? 1 : 0) + (previousState.testBit(i + 37) ? 2 : 0);
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
