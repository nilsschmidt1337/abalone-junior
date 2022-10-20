package org.nschmidt.abalone;

import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;
import static org.nschmidt.abalone.WinningInOneMoveChecker.winsInOneMove;
import static org.nschmidt.abalone.WinningInTwoMovesChecker.winsInTwoMoves;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static org.nschmidt.abalone.WinningInThreeMovesChecker.winsInThreeMoves;

public enum Backtracker {
    INSTANCE;
    
    public static Field backtrack(Field state, Player player, int depth) {
        System.out.println("Try to find win in one move...");
        final Player opponent = player.switchPlayer();
        Field[] winsInOne = winsInOneMove(state, player);
        if (winsInOne.length == 1) {
            return winsInOne[0];
        }
        
        System.out.println("Try to find win in two moves...");
        Field[] winsInTwo = winsInTwoMoves(state, player);
        if (winsInTwo.length == 2) {
            return winsInTwo[0];
        }
        
        System.out.println("Try to find optimum in two moves...");
        Field[] optimumInThree = OptimisationInTwoMovesChecker.optimisationInTwoMoves(state, player);
        if (optimumInThree.length == 2) {
            return optimumInThree[0];
        }
        
        Field[] moves = allMoves(state, player);
        long maxScore = Long.MIN_VALUE;
        Field maxMove = null;
        for (Field move : moves) {
            // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
            Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
            if (oppenentWinsInOne.length == 1) {
                continue;
            }
            // Mache keinen Zug, bei dem der Gegner in zwei Zügen gewinnt
            Field[] oppenentWinsInTwo = winsInTwoMoves(move, opponent);
            if (oppenentWinsInTwo.length == 0) {
                maxMove = move;
                break;
            }
        }
        
        if (maxMove == null) {
            maxMove = moves[0];
        }
        
        if (score(state, player) == Long.MIN_VALUE) {
            System.out.println("Try to escape a dangerous situation...");
            boolean notEscaped = true;
            Arrays.sort(moves, (m1, m2) -> Long.compare(score(m2, player), score(m1, player)));
            for (Field move : moves) {
                long score = score(move, player);
                if (score > maxScore) {
                    // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                    Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
                    if (oppenentWinsInOne.length == 1) {
                        System.out.println("Opponent can win in one step...");
                        continue;
                    }
                    // Mache keinen Zug, bei dem der Gegner in zwei Zügen gewinnt
                    Field[] oppenentWinsInTwo = winsInTwoMoves(move, opponent);
                    if (oppenentWinsInTwo.length == 2) {
                        System.out.println("Opponent can win in two steps...");
                        continue;
                    }
                    
                    System.out.println("Able to escape... (Score " + score + ")");
                    notEscaped = false;
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            if (notEscaped) {
                for (Field move : moves) {
                    long score = score(move, player);
                    if (score > maxScore) {
                        // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                        Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
                        if (oppenentWinsInOne.length == 1) {
                            System.out.println("Opponent can win in one step...");
                            continue;
                        }
                        
                        System.out.println("Able to escape, but opponent can win in two steps... (Score " + score + ")");
                        maxScore = score;
                        maxMove = move;
                    }
                }
            }
            
            return maxMove;
        }

        
        boolean foundStrategicSolution = false;
        System.out.println("Try to find a strategic solution...");
        Arrays.sort(moves, (m1, m2) -> Long.compare(score(m2, player), score(m1, player)));
        for (Field move : moves) {
            long initialScore = MonteCarloEvaluator.score(move, player);
            Field result = playRound(move, opponent, depth);
            long score = MonteCarloEvaluator.score(result, player);
            if (score > maxScore && initialScore > maxScore) {
                // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                Field[] oppenentWinsInOne = winsInOneMove(move, opponent);
                if (oppenentWinsInOne.length == 1) {
                    continue;
                }
                // Mache keinen Zug, bei dem der Gegner in zwei Zügen gewinnt
                Field[] oppenentWinsInTwo = winsInTwoMoves(move, opponent);
                if (oppenentWinsInTwo.length == 2) {
                    continue;
                }
                // Mache keinen Zug, bei dem der Gegner in drei Zügen gewinnt
                Field[] oppenentWinsInThree = winsInThreeMoves(move, opponent);
                if (oppenentWinsInThree.length == 3) {
                    continue;
                }
                // Mache keinen Zug, bei dem der Gegner eine zusätzliche Figur isolieren kann
                final Set<Integer> isolatedPieces = new TreeSet<>();
                final int originalIsolatedPiecesCount;
                for (int i = 0; i < 37; i++) {
                    if (FieldEvaluator.isIsolated(move, player, i)) {
                        isolatedPieces.add(i);
                    }
                }
                originalIsolatedPiecesCount = isolatedPieces.size();
                for (Field attack : MoveDetector.allAttackMoves(move, opponent)) {
                    for (int i = 0; i < 37; i++) {
                        if (FieldEvaluator.isIsolated(attack, player, i)) {
                            isolatedPieces.add(i);
                        }
                    }   
                }
                
                if (isolatedPieces.size() > originalIsolatedPiecesCount) {
                    continue;
                }
                
                
                System.out.println("Found a solution... (Score " + score + ")");
                foundStrategicSolution = true;
                maxScore = initialScore;
                maxMove = move;
            }
        }
        
        if (!foundStrategicSolution) {
            System.out.println("Try to find a classic solution...");
            for (Field move : moves) {
                long score = MonteCarloEvaluator.score(move, player);
                if (score > maxScore) {
                    // Mache keinen Zug, bei dem der Gegner in einem Zug gewinnt
                    Field[] oppenentWinsInOne = WinningInOneMoveChecker.winsInOneMove(move, opponent);
                    if (oppenentWinsInOne.length == 1) {
                        continue;
                    }
                    // Lasse zu, dass der Gegner in zwei Zügen gewinnen kann, um den Handlungspielraum zu vergrößern
                    
                    System.out.println("Found a classic solution... (Score " + score + ")");
                    maxScore = score;
                    maxMove = move;
                }
            }
        }
        
        return maxMove;
    }
    
    
    private static Field playRound(Field state, Player currentPlayer, int maxDepth) {
        int depth = 0;
        while (depth < maxDepth) {
            Field[] moves = allMoves(state, currentPlayer);
            
            long maxScore = Long.MIN_VALUE;
            Field maxMove = moves[0];
            
            for (Field move : moves) {
                long score = score(move, currentPlayer);
                if (score > maxScore) {
                    maxScore = score;
                    maxMove = move;
                }
            }
            
            state = maxMove;
            
            currentPlayer = currentPlayer.switchPlayer();
            if (wins(state, currentPlayer)) break;
            depth++;
        }
        
        return state;
    }
}
