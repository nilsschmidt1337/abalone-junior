package org.nschmidt.abalone.winning;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;
import static org.nschmidt.abalone.winning.WinningChecker.wins;

import java.util.Arrays;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public enum WinningInThreeMovesChecker {
    INSTANCE;
    
    public static Field[] winsInThreeMoves(Field state, Player player) {
        final Player opponent = player.switchPlayer();
        
        Field[] firstMoves = allMoves(state, player);
        return Arrays.asList(firstMoves).parallelStream().map(firstMove -> {
            final Field[] result = new Field[3];
            boolean hasSolution = false;
            int solutionCount = 0;
            int solutionCount2 = 0;
            
            // Bedingung 1: Gegner darf nicht gewinnen!
            hasSolution = !wins(firstMove, opponent);
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
                
                double maxScore = -Double.MAX_VALUE;
                
                for (Field secondMove : secondMoves) {
                    
                    hasSolution = !wins(secondMove, opponent);
                    if (!hasSolution) continue;
                    
                    double score = score(secondMove, player);
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
                    maxScore = -Double.MAX_VALUE;
                        
                    for (Field thirdMove : thirdMoves) {
                        
                        hasSolution = !wins(thirdMove, opponent);
                        if (!hasSolution) continue;
                        
                        double score = score(thirdMove, player);
                        if (score > maxScore) {
                            maxScore = score;
                            bestThirdMove = thirdMove;
                        }
                    }
                            
                    // Bedingung 3: Gegner darf nicht gewinnen, wenn wir den dritten Zug gemacht haben!
                    hasSolution = !wins(bestThirdMove, opponent);
                    if (!hasSolution) continue;
                
                    Field[] lastOpponentMoves = allMoves(bestThirdMove, opponent);
                    // Wenn der Gegner keinen Zug machen kann, soll einfach die aktuelle Stellung bewertet werden
                    if (lastOpponentMoves.length == 0) lastOpponentMoves = new Field[] {bestThirdMove};
                    for (Field lastOpponentMove : lastOpponentMoves) {
                        if (!wins(lastOpponentMove, player)) {
                            hasSolution = false;
                            break;
                        }
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
            if (solutionCount == opponentMoves.length && secondOpponentMoves != null && solutionCount2 == secondOpponentMoves.length) {
                // Das ist dann eine Lösung auf diesem Weg
                // (unser zweiter und dritter Zug kann dann in der Realität anders ausfallen, aber wir werden gewinnen) 
                return result;
            }
            
            return new Field[0];
        }).filter(r -> r.length == 3).findAny().orElse(new Field[0]);
    }
}
