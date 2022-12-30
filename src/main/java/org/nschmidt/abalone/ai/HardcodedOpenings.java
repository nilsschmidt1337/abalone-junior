package org.nschmidt.abalone.ai;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.Field.lookAtField;
import static org.nschmidt.abalone.playfield.Player.BLACK;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.FieldPrinter;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.winning.WinningChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum HardcodedOpenings {
    INSTANCE;
    
    private static final String OPENINGS_TXT = "openings.txt";

    private static final Logger LOGGER = LoggerFactory.getLogger(HardcodedOpenings.class);
    
    private static final Random RND = new Random();
    
    private static final Map<Field, Field> BLACK_OPENINGS = new HashMap<>();
    private static final Map<Field, Field> WHITE_OPENINGS = new HashMap<>();
    private static final Map<Field, Field> BLACK_VARIANT_OPENINGS = new HashMap<>();
    private static final Map<Field, Field> WHITE_VARIANT_OPENINGS = new HashMap<>();

    private static void init() {
        if (!BLACK_OPENINGS.isEmpty() && !WHITE_OPENINGS.isEmpty()) {
            return;
        }
        
        // BLACK_OPENINGS.clear()
        // BLACK_OPENINGS.put(Field.of(new Player[]{WHITE,WHITE,EMPTY,BLACK,BLACK,WHITE,WHITE,WHITE,BLACK,BLACK,BLACK,EMPTY,WHITE,WHITE,EMPTY,BLACK,BLACK,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,BLACK,BLACK,WHITE,WHITE,WHITE,EMPTY,BLACK,BLACK,BLACK,WHITE,WHITE,WHITE,BLACK,BLACK,EMPTY,EMPTY,WHITE}), Field.of(new Player[]{WHITE,WHITE,EMPTY,EMPTY,BLACK,WHITE,WHITE,WHITE,BLACK,BLACK,BLACK,EMPTY,WHITE,WHITE,BLACK,BLACK,BLACK,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,EMPTY,BLACK,BLACK,WHITE,WHITE,WHITE,EMPTY,BLACK,BLACK,BLACK,WHITE,WHITE,WHITE,BLACK,BLACK,EMPTY,EMPTY,WHITE}))

        // Read openings
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(HardcodedOpenings.class.getResourceAsStream(OPENINGS_TXT), StandardCharsets.UTF_8))) {
            loadOpenings(reader, WHITE_OPENINGS);
            loadOpenings(reader, BLACK_OPENINGS);
            loadOpenings(reader, WHITE_VARIANT_OPENINGS);
            loadOpenings(reader, BLACK_VARIANT_OPENINGS);
        } catch (IOException e) {
            LOGGER.error("Can't load opening table.", e);
        }
    
    }

    private static void loadOpenings(BufferedReader reader, Map<Field, Field> openings) throws IOException {
        String player = reader.readLine();
        long openingCount = Long.parseLong(reader.readLine());
        LOGGER.info("Read {} openings for player {}", openingCount, player);
        for (int i = 0; i < openingCount; i++) {
            long white = Long.parseLong(reader.readLine());
            long black = Long.parseLong(reader.readLine());
            
            long answerWhite = Long.parseLong(reader.readLine());
            long answerBlack = Long.parseLong(reader.readLine());
            
            Field board = Field.of(white, black);
            Field answer = Field.of(answerWhite, answerBlack);
            openings.put(board, answer);
            
            for (int j = 0; j < 13; j++) reader.readLine();
        }
    }

    /**
     * Ermittelt einen Eröffnungszug für die 61 Felder Variante.
     * 
     * @param state das aktuelle Spielfeld
     * @param player der Spieler, welcher an der Reihe ist
     * @return ein Eröffnungszug
     */
    public static Field findOpeningMove(Field state, Player player) {
        if (Field.FIELD_SIZE != 61) {
            return null;
        }
        
        init();
        
        // Erstelle nomalisiertes Feld
        final Player[] currentPieces = new Player[Field.FIELD_SIZE];
        for (int i = 0; i < Field.FIELD_SIZE; i++) {
            currentPieces[i] = lookAtField(state, i);
        }
        
        final Field currentField = Field.of(currentPieces);
        final Field result;
        if (player == WHITE) {
            if (RND.nextBoolean() || !WHITE_VARIANT_OPENINGS.containsKey(currentField)) {
                result = WHITE_OPENINGS.get(currentField);
            } else {
                result = WHITE_VARIANT_OPENINGS.get(currentField);
            }
        } else if (player == BLACK) {
            if (RND.nextBoolean() || !BLACK_VARIANT_OPENINGS.containsKey(currentField)) {
                result = BLACK_OPENINGS.get(currentField);
            } else {
                result = BLACK_VARIANT_OPENINGS.get(currentField);
            }
        } else {
            return null;
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        LOGGER.info("Path for openings {}", HardcodedOpenings.class.getResource(OPENINGS_TXT).getPath());
        
        // Read openings
        init();
        
        for (int i = 0; i < 10; i++) {
            LOGGER.info("Iteration {}:", i);
            searchForOpenings();
        }
    }

    static void searchForOpenings() {
        final Map<Field, Field> openings;
        final Map<Field, Field> variants;
        final Player player;
        if (RND.nextBoolean()) {
            LOGGER.info("Will generate a black opening move");
            openings = BLACK_OPENINGS;
            variants = BLACK_VARIANT_OPENINGS;
            player = Player.BLACK;
        } else {
            LOGGER.info("Will generate a white opening move");
            openings = WHITE_OPENINGS;
            variants = WHITE_VARIANT_OPENINGS;
            player = Player.WHITE;
        }
        
        final Player opponent = player.switchPlayer();
        
        Field board = findMove(player).orElse(Field.INITIAL_FIELD);
        Field[] moves;
        
        if (board != Field.INITIAL_FIELD || opponent == Player.WHITE) {
            moves = allMoves(board, opponent);
        } else if (player == Player.WHITE) {
            moves = new Field[] {Field.INITIAL_FIELD};
        } else {
            LOGGER.warn("Did not find a new opening move.");
            return;
        }
        
        List<Field[]> newOpenings = new HashSet<>(Arrays.asList(moves))
        .parallelStream()
        .filter(move -> !openings.containsKey(move))
        .map(move -> {
            if (!WinningChecker.gainPiece(move, player)) {
                Field[] answers = new AlphaBetaAI(6, player).bestVariantMoves(move, 2);
                if (answers.length == 0) return new Field[] {move, null};
                if (answers.length == 1) return new Field[] {move, answers[0], answers[0]};
                if (answers.length == 2 && WinningChecker.gainPiece(answers[1], opponent)) return new Field[] {move, answers[0], answers[0]};
                return new Field[] {move, answers[0], answers[1]};
            } else {
                Field aggressiveMove = new AggressiveAlphaBetaAI(6, player).bestMove(move);
                if (aggressiveMove != null && Field.countPieces(aggressiveMove, opponent) < Field.countPieces(move, opponent)) {
                    return new Field[] {move, aggressiveMove, aggressiveMove};
                }
                
                return new Field[] {move, null};
            }
        })
        .filter(pair -> Objects.nonNull(pair[1]))
        .toList();
        
        if (newOpenings.isEmpty()) {
            LOGGER.warn("Did not find a new opening move.");
            return;
        }
        
        LOGGER.info("Found {} opening move(s)", newOpenings.size());
        
        for(Field[] pair : newOpenings) {
            openings.put(pair[0], pair[1]);
            variants.put(pair[0], pair[2]);
        }

        // Write openings
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(HardcodedOpenings.class.getResource(OPENINGS_TXT).getPath()), false, StandardCharsets.UTF_8)) {
            writeOpenings(writer, WHITE_OPENINGS, Player.WHITE);
            writeOpenings(writer, BLACK_OPENINGS, Player.BLACK);
            writeOpenings(writer, WHITE_VARIANT_OPENINGS, Player.WHITE);
            writeOpenings(writer, BLACK_VARIANT_OPENINGS, Player.BLACK);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Can't save opening table.", e);
        }
    }

    private static Optional<Field> findMove(Player targetPlayer) {
        final int targetDepth = RND.nextInt(3) * 2 + (targetPlayer == Player.WHITE ? 1 : 0);
        LOGGER.info("Target depth is {}", targetDepth);
        Field currentField = Field.INITIAL_FIELD;
        Player currentPlayer = Player.WHITE;
        Map<Field, Field> openings = WHITE_OPENINGS;
        Map<Field, Field> variants = WHITE_VARIANT_OPENINGS;
        for (int depth = 0; depth < targetDepth; depth++) {
            if (currentPlayer != targetPlayer && RND.nextBoolean()) {
                Field[] moves = allMoves(currentField, currentPlayer);
                if (moves.length > 0) {
                    currentField = moves[RND.nextInt(moves.length)];
                }
            } else if (RND.nextBoolean() && variants.containsKey(currentField)) {
                currentField = variants.get(currentField);
            } else if (openings.containsKey(currentField)) {
                currentField = openings.get(currentField);
            } else if (currentPlayer == targetPlayer) {
                LOGGER.info("Find {} answer for all enemy responses to {}", targetPlayer, currentField);
                return Optional.of(currentField);
            } else {
                return Optional.empty();
            }
            
            currentPlayer = currentPlayer.switchPlayer();
            switch (currentPlayer) {
            case BLACK:
                openings = BLACK_OPENINGS;
                variants = BLACK_VARIANT_OPENINGS;
                break;
            case WHITE:
                openings = WHITE_OPENINGS;
                variants = WHITE_VARIANT_OPENINGS;
                break;
            default:
                break;
            }
        }
        
        LOGGER.info("Find {} answer for all enemy responses to {}", targetPlayer, currentField);
        
        if (currentField != Field.INITIAL_FIELD) {
            return Optional.of(currentField);
        }
        
        return Optional.empty();
    }

    private static void writeOpenings(PrintWriter writer, Map<Field, Field> openings, Player player) {
        writer.println(player);
        writer.println(openings.size());
        for (Entry<Field, Field> entry : openings.entrySet()) {
            Field board = entry.getKey();
            Field answer = entry.getValue();
            writer.println(board.getWhite());
            writer.println(board.getBlack());
            writer.println(answer.getWhite());
            writer.println(answer.getBlack());
            writer.println(FieldPrinter.buildStandardFieldDeltaString(answer, board));
        }
    }
}
