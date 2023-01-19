package org.nschmidt.abalone.cnn;

import static org.nschmidt.abalone.playfield.Field.lookAtField;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.nd4j.common.io.ClassPathResource;
import org.nschmidt.abalone.ai.AI;
import org.nschmidt.abalone.ai.FastBacktracker;
import org.nschmidt.abalone.move.MoveDetector;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.FieldEvaluator;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.winning.WinningChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluationByMonteCarlo {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationByMonteCarlo.class);
    private static final  Random RND = new Random();
    
    public static void main(String[] args) throws IOException {
        
        int iterations = 2_000_000;
        try (PrintWriter myWriter = new PrintWriter(new ClassPathResource("monte-carlo-black.txt").getFile(), StandardCharsets.UTF_8.toString())) {
            for (int i = 0; i < iterations; i++) {
                LOGGER.info("Write entry {}", i);
                writeEntryBlack(myWriter);
                myWriter.flush();
            }
        }
        
        try (PrintWriter myWriter = new PrintWriter(new ClassPathResource("monte-carlo-white.txt").getFile(), StandardCharsets.UTF_8.toString())) {
            for (int i = 0; i < iterations; i++) {
                LOGGER.info("Write entry {}", i);
                writeEntryWhite(myWriter);
                myWriter.flush();
            }
        }
    }
    
    private static void writeEntryBlack(PrintWriter myWriter) {
        Player currentPlayer = Player.BLACK;
        Field currentField = Field.INITIAL_FIELD;
        
        int preCalculatedMoves;
        preCalculatedMoves = RND.nextInt(50) * 2;
        
        for (int i = 0; i < preCalculatedMoves; i++) {
            Field[] moves = MoveDetector.allMoves(currentField, currentPlayer);
            currentField = moves[RND.nextInt(moves.length)];
            currentPlayer = currentPlayer.switchPlayer();
        }
        
        writeTrainingData(myWriter, currentPlayer, currentField);
    }
    
    private static void writeEntryWhite(PrintWriter myWriter) {
        Player currentPlayer = Player.BLACK;
        Field currentField = Field.INITIAL_FIELD;
        
        int preCalculatedMoves;
        preCalculatedMoves = RND.nextInt(50) * 2 + 1;
        
        for (int i = 0; i < preCalculatedMoves; i++) {
            Field[] moves = MoveDetector.allMoves(currentField, currentPlayer);
            currentField = moves[RND.nextInt(moves.length)];
            currentPlayer = currentPlayer.switchPlayer();
        }
        
        writeTrainingData(myWriter, currentPlayer, currentField);
    }

    private static void writeTrainingData(PrintWriter myWriter, Player currentPlayer, Field currentField) {
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < Field.FIELD_SIZE; i++) {
            switch (lookAtField(currentField, i)) {
            case BLACK: 
                sb2.append("1.0,");
                break;
            case WHITE:
                sb2.append("-1.0,");
                break;
            case EMPTY:
                sb2.append("0.0,");
                break;
            }
        }
        
        sb2.append(FieldEvaluator.score(currentField, currentPlayer));
        myWriter.println(sb2.toString());
    }

    private static void writeEntryOld(PrintWriter myWriter) {
        Player currentPlayer = Player.BLACK;
        Field currentField = Field.INITIAL_FIELD;
        
        Player playerToSimulate;
        int preCalculatedMoves;
        if (RND.nextBoolean()) {
            preCalculatedMoves = 24 + RND.nextInt(30) * 2;
            playerToSimulate = Player.WHITE;
        } else {
            preCalculatedMoves = 24 + RND.nextInt(30) * 2 + 1;
            playerToSimulate = Player.BLACK;
        }
        
        // Warm-up
        for (int i = 0; i < preCalculatedMoves; i++) {
            if (currentPlayer == playerToSimulate) {
                Field[] moves = MoveDetector.allMoves(currentField, currentPlayer);
                currentField = moves[RND.nextInt(moves.length)];
            } else {
                currentField = AI.bestMove(currentField, currentPlayer);
            }
            
            currentPlayer = currentPlayer.switchPlayer();
        }
        
        Field toEvaluate = currentField;
        Player playerToEvaluate = currentPlayer;
        LOGGER.info("Player {}: will evaluate ({}|{}) {}", playerToEvaluate, Field.countPieces(toEvaluate, Player.BLACK), Field.countPieces(toEvaluate, Player.WHITE), toEvaluate);
        
        final int iterations = 3;
        double total = iterations;
        
        CompletableFuture<Double> f1 = CompletableFuture.supplyAsync(() -> simulate(toEvaluate, playerToEvaluate, iterations));
        CompletableFuture<Double> f2 = CompletableFuture.supplyAsync(() -> simulate(toEvaluate, playerToEvaluate, iterations));
        CompletableFuture.allOf(f1, f2).join();
   
        final double a;
        final double b;
   
        try {
            a = f1.get();
            b = f2.get();
            double score = (a + b) / (total * 2.0);
            
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < Field.FIELD_SIZE; i++) {
                switch (lookAtField(toEvaluate, i)) {
                case BLACK: 
                    sb2.append("1.0,");
                    break;
                case WHITE:
                    sb2.append("-1.0,");
                    break;
                case EMPTY:
                    sb2.append("0.0,");
                    break;
                }
            }
            
            if (playerToEvaluate == Player.WHITE) {
                LOGGER.info("Iterations {} ; Result {}", iterations, -score);
                sb2.append(-score);
            } else {
                LOGGER.info("Iterations {} ; Result {}", iterations, score);
                sb2.append(score);
            }
            
            if (Math.abs(score - 1.0) > 0.01) {
                LOGGER.info("Entry {}", sb2.toString());
                myWriter.println(sb2.toString());
            }
        
        } catch (ExecutionException e) {
            LOGGER.error("", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static double simulate(Field toEvaluate, Player playerToEvaluate, final int iterations) {
        Player currentPlayer;
        Field currentField;
        double score = 0;
        for (int i = 0; i < iterations; i++) {
            LOGGER.info("Iteration {} of {}", i, iterations);
            currentField = toEvaluate;
            currentPlayer = playerToEvaluate; 
            double moves = 1;
            FastBacktracker.clearCache();
            while (!WinningChecker.wins(currentField, currentPlayer)) {
                if (playerToEvaluate != currentPlayer) {
                    currentField = FastBacktracker.backtrack(currentField, currentPlayer);
                    if (currentField == null) {
                        currentField = AI.bestMove(currentField, currentPlayer);
                    }
                } else {
                    currentField = AI.bestMove(currentField, currentPlayer);
                }
                
                currentPlayer = currentPlayer.switchPlayer();
                moves++;
                
                if (moves > 1000) {
                    break;
                }
            }
            
            if (playerToEvaluate == currentPlayer || moves > 1000) {
                score = score + 1.0 / moves;
            }
        }
        return score;
    }
}
