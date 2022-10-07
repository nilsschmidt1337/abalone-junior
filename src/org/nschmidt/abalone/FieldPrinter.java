package org.nschmidt.abalone;

import static java.lang.System.out;

public enum FieldPrinter {
    INSTANCE;
    
    public static void printField(long state) {
        long nextValue = state;
        printSpaces(3);
        for (int i = 0; i < 37; i++) {
            long fieldValue = Long.remainderUnsigned(nextValue, 3L);
            nextValue = Long.divideUnsigned(nextValue, 3L);
            breakLine(i);
            out.print(fieldValue);
            printSpaces(1);
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
