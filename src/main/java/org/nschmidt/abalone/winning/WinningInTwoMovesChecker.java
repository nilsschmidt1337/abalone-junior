package org.nschmidt.abalone.winning;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.winning.WinningChecker.wins;

import java.util.Arrays;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum WinningInTwoMovesChecker {
    INSTANCE;
    
    public static Field[] winsInTwoMoves(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        
        Field[] firstMoves = allMoves(state, player);
        return Arrays.asList(firstMoves).parallelStream().map(firstMove -> {
            final Field[] result = new Field[2];
            boolean hasSolution = false;
            int solutionCount = 0;
            
            // Bedingung 1: Gegner darf nicht gewinnen!
            hasSolution = !wins(firstMove, opponent);
            if (!hasSolution) return new Field[0];
            result[0] = firstMove;
            
            // Jetzt haben wir einen Zug von dem wir wissen, dass wir ihn sicher setzen können.
            // Jetzt zieht der Gegner
            Field[] opponentMoves = allMoves(firstMove, opponent);
            
            // Wenn der Gegner keinen Zug machen kann, sind wir wieder an der Reihe 
            if (opponentMoves.length == 0) opponentMoves = new Field[] {firstMove};
            
            solutionCount = 0;
            for (Field opponentMove : opponentMoves) {
                
                Field[] secondMoves = allMoves(opponentMove, player);
                for (Field secondMove : secondMoves) {
                    
                    // Bedingung 2: Gegner darf nicht gewinnen, wenn wir den zweiten Zug gemacht haben!
                    hasSolution = !wins(secondMove, opponent);
                    if (!hasSolution) continue;
                    
                    Field[] lastOpponentMoves = allMoves(secondMove, opponent);
                    // Wenn der Gegner keinen Zug machen kann, soll einfach die aktuelle Stellung bewertet werden
                    if (lastOpponentMoves.length == 0) lastOpponentMoves = new Field[] {secondMove};
                    for (Field lastOpponentMove : lastOpponentMoves) {
                        if (!wins(lastOpponentMove, player)) {
                            hasSolution = false;
                            break;
                        }
                    }
                    
                    if (hasSolution) {
                        result[1] = secondMove;
                        break;
                    }
                }
                
                if (hasSolution) {
                    solutionCount++;
                }
            }
            
            // Prüfen, ob wir für jeden Gegnerzug eine schlagfertige Antwort gefunden haben
            if (solutionCount == opponentMoves.length) {
                // Das ist dann eine Lösung auf diesem Weg
                // (unser zweiter Zug kann dann in der Realität anders ausfallen, aber wir werden gewinnen) 
                return result;
            }
            
            return new Field[0];
        }).filter(r -> r.length == 2).findAny().orElse(new Field[0]);
    }
}
