package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;
import static org.nschmidt.abalone.WinningInTwoMovesChecker.winsInTwoMoves;

import java.util.Arrays;

public enum OptimisationInThreeMovesChecker {
    INSTANCE;
    
    public static Field[] optimisationInThreeMoves(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        final long startScore = score(state, player);
        
        Field[] firstMoves = allMoves(state, player);
        return Arrays.asList(firstMoves).parallelStream().map(firstMove -> {
            final Field[] result = new Field[3];
            boolean hasSolution = false;
            int solutionCount = 0;
            int solutionCount2 = 0;
            
            // Bedingung 1: Gegner darf nicht gewinnen!
            hasSolution = !wins(firstMove, opponent) && WinningInTwoMovesChecker.winsInTwoMoves(firstMove, opponent).length != 2;
            if (!hasSolution) return new Field[0];
            result[0] = firstMove;
            
            // Jetzt haben wir einen Zug von dem wir wissen, dass wir ihn sicher setzen können.
            // Jetzt zieht der Gegner
            Field[] opponentMoves = allMoves(firstMove, opponent);
            Field[] secondOpponentMoves = null;
            
            // Wenn der Gegner keinen Zug machen kann, sind wir wieder an der Reihe 
            if (opponentMoves.length == 0) opponentMoves = new Field[] {firstMove};
            
            for (Field opponentMove : opponentMoves) {
                
                Field[] secondMoves = allMoves(opponentMove, player);
                Field bestSecondMove;
                if (secondMoves.length == 0) {
                    bestSecondMove = opponentMove;
                } else {
                    bestSecondMove = secondMoves[0];
                }
                
                long maxScore = Long.MIN_VALUE;
                
                for (Field secondMove : secondMoves) {
                    
                    hasSolution = !wins(secondMove, opponent) && WinningInTwoMovesChecker.winsInTwoMoves(secondMove, opponent).length != 2;
                    if (!hasSolution) continue;
                    
                    long score = score(secondMove, player);
                    if (score > maxScore) {
                        maxScore = score;
                        bestSecondMove = secondMove;
                    }
                }
                
                secondOpponentMoves = allMoves(bestSecondMove, player);
                // Wenn der Gegner keinen Zug machen kann, sind wir wieder an der Reihe 
                if (secondOpponentMoves.length == 0) secondOpponentMoves = new Field[] {bestSecondMove};
                    
                solutionCount2 = 0;
                for (Field secondOpponentMove : secondOpponentMoves) {
                    
                    Field[] thirdMoves = allMoves(secondOpponentMove, player);
                    Field bestThirdMove;
                    if (thirdMoves.length == 0) {
                        bestThirdMove = secondOpponentMove;
                    } else {
                        bestThirdMove = thirdMoves[0];
                    }
                    maxScore = Long.MIN_VALUE;
                        
                    for (Field thirdMove : thirdMoves) {
                        
                        hasSolution = !wins(thirdMove, opponent);
                        if (!hasSolution) continue;
                        
                        long score = score(thirdMove, player);
                        if (score > maxScore) {
                            maxScore = score;
                            bestThirdMove = thirdMove;
                        }
                    }
                            
                    // Bedingung 3: Gegner darf nicht gewinnen, wenn wir den dritten Zug gemacht haben!
                    hasSolution = !wins(bestThirdMove, opponent);
                    if (!hasSolution) continue;
                
                    long score = score(bestThirdMove, player);
                    if (score < startScore) {
                        hasSolution = false;
                        break;
                    }
            
                    if (hasSolution) {
                        result[2] = bestThirdMove;
                        solutionCount2++;
                    }
                }

                if (hasSolution) {
                    result[1] = bestSecondMove;
                    solutionCount++;
                }
            }
            
            // Prüfen, ob wir für jeden Gegnerzug eine schlagfertige Antwort gefunden haben
            if (solutionCount == opponentMoves.length) {
                // Das ist dann eine Lösung auf diesem Weg
                // (unser zweiter und dritter Zug kann dann in der Realität anders ausfallen, aber wir werden gewinnen) 
                return result;
            }
            
            return new Field[0];
        }).filter(r -> r.length == 3).findAny().orElse(new Field[0]);
    }
}
