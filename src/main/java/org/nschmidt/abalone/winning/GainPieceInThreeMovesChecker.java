package org.nschmidt.abalone.winning;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.winning.WinningChecker.gainPiece;

import java.util.Arrays;
import java.util.HashSet;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum GainPieceInThreeMovesChecker {
    INSTANCE;
    
    public static Field[] gainPieceInThreeMoves(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        
        Field[] firstMoves = allMoves(state, player);
        firstMoves = new HashSet<>(Arrays.asList(firstMoves)).toArray(new Field[0]);
        return Arrays.asList(firstMoves).parallelStream().map(firstMove -> {
            final Field[] result = new Field[3];
            boolean hasSolution = false;
            int solutionCount = 0;
            
            // Bedingung 1: Gegner darf nicht gewinnen!
            hasSolution = !gainPiece(firstMove, opponent);
            if (!hasSolution) return new Field[0];
            
            result[0] = firstMove;
            
            // Jetzt haben wir einen Zug von dem wir wissen, dass wir ihn sicher setzen können.
            // Jetzt zieht der Gegner
            Field[] opponentMoves = allMoves(firstMove, opponent);
            opponentMoves = new HashSet<>(Arrays.asList(opponentMoves)).toArray(new Field[0]);
            
            // Wenn der Gegner keinen Zug machen kann, sind wir wieder an der Reihe 
            if (opponentMoves.length == 0) opponentMoves = new Field[] {firstMove};
            
            solutionCount = 0;
            for (Field opponentMove : opponentMoves) {
                
                hasSolution = gainPiece(opponentMove, player);
                if (hasSolution) {
                    solutionCount++;
                    continue;
                }
                
                Field[] inTwo = GainPieceInTwoMovesChecker.gainPieceInTwoMoves(opponentMove, player);
                hasSolution = inTwo.length == 2;
                
                if (hasSolution) {
                    result[1] = inTwo[0];
                    result[2] = inTwo[1];
                    solutionCount++;
                } else {
                    Field[] inOne = GainPieceInOneMoveChecker.gainPieceInOneMove(opponentMove, player);
                    hasSolution = inOne.length == 1;
                    if (hasSolution) {
                        result[1] = inOne[0];
                        solutionCount++;
                    } else {
                        break;
                    }
                    
                }
            }
            
            // Prüfen, ob wir für jeden Gegnerzug eine schlagfertige Antwort gefunden haben
            if (solutionCount == opponentMoves.length) {
                // Das ist dann eine Lösung auf diesem Weg
                // (unser zweiter Zug kann dann in der Realität anders ausfallen, aber wir werden gewinnen) 
                return result;
            }
            
            return new Field[0];
        }).filter(r -> r.length == 3).findAny().orElse(new Field[0]);
    }
}
