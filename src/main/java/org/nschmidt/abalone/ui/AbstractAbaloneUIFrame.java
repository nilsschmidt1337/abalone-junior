package org.nschmidt.abalone.ui;

import static org.nschmidt.abalone.move.MoveDetector.allMoves;
import static org.nschmidt.abalone.playfield.Adjacency.indexAt;
import static org.nschmidt.abalone.playfield.Field.FIELD_HEIGHT;
import static org.nschmidt.abalone.playfield.Field.FIELD_WIDTH;
import static org.nschmidt.abalone.playfield.Field.PIECE_COUNT;
import static org.nschmidt.abalone.playfield.Field.countPieces;
import static org.nschmidt.abalone.playfield.Field.lookAtField;
import static org.nschmidt.abalone.playfield.Field.populateField;
import static org.nschmidt.abalone.playfield.FieldEvaluator.score;
import static org.nschmidt.abalone.playfield.Player.EMPTY;
import static org.nschmidt.abalone.winning.WinningChecker.wins;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractAbaloneUIFrame extends Frame {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAbaloneUIFrame.class);

    private static final String ABALONE_JUNIOR_PLAYER_WINS = "Abalone Junior - Player %s wins!";
    
    private static final String ABALONE_JUNIOR_SCORE = "Abalone Junior - Score [%s %s | %s %s]";

    private static final long serialVersionUID = 1L;

    protected GridBagLayout grid = new GridBagLayout();
    protected GridBagConstraints straints = new GridBagConstraints();
    
    private transient Deque<Field> previousStates = new LinkedList<>();
    private transient Deque<Player> previousPlayers = new LinkedList<>();
    
    private transient Field previousState;
    private transient Field[] validMoves;
    private transient Field currentState;
    private Player currentPlayer;
    private Player lastColor = null;
    
    private final Component confirmComponent;
    private final Component resetComponent;
    
    private final List<Component> fieldComponents = new ArrayList<>();
    
    protected transient BiFunction<Field, Player, Field> artificicalIntelligence = (state, player) -> state;
    
    protected AbstractAbaloneUIFrame(Field state, Player currentPlayer) {
        init(state, currentPlayer);
        
        setLayout(grid);
        addWindowListener(new UIFrameWindowListener());
       
        // Reset Component
        resetComponent = addComponent(0, 0, true, currentPlayer.getColor(), -1);
        
        // Undo Component
        addComponent(1, 0, true, Color.ORANGE, -3);
        
        for (int y = 0; y < FIELD_HEIGHT; y++) {
            for (int x = 0; x < FIELD_WIDTH; x++) {
                if (x <= 1 && y == 0) continue;
                fillGrid(x, y);
            }
        }
        
        confirmComponent = addConfirmComponent();
           
        pack();
        setVisible(true);
    }

    private void init(Field state, Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.previousState = state;
        this.currentState = state;
        this.validMoves = allMoves(state, currentPlayer);
        if (wins(state, currentPlayer)) {
            setTitle(String.format(ABALONE_JUNIOR_PLAYER_WINS, currentPlayer));
        } else {
            setTitle(String.format(ABALONE_JUNIOR_SCORE, 
                                   Player.WHITE, PIECE_COUNT - countPieces(state, Player.BLACK),
                                   Player.BLACK, PIECE_COUNT - countPieces(state, Player.WHITE)));
        }
    }

    private void fillGrid(int x, int y) {
        final int index = indexAt(y, x);
        
        Color color = Color.LIGHT_GRAY;
        
        if (index > -1) {
            Player player = lookAtField(currentState, index);
            color = player.getColor();
        }
        
        fieldComponents.add(addComponent(x, y, index != -1, color, index));
    }

    protected abstract Component addComponent(int x, int y, boolean enabled, Color color, int index);
    
    protected abstract Component addConfirmComponent();
    
    protected class ButtonActionListener implements ActionListener {
        private final LabelMouseListener listener;
        public ButtonActionListener(int index, Component source) {
            listener = new LabelMouseListener(index, source);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            listener.mouseClicked(new MouseEvent(confirmComponent, 0, 0, 0, 0, 0, 0, getFocusTraversalKeysEnabled()));
        }
        
        public void paint() {
            listener.paint();
        }
    }
    
    protected class LabelMouseListener implements MouseListener {
        private final int index;
        private final Component source;
        public LabelMouseListener(int index, Component source) {
            this.index = index;
            this.source = source;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (index == -1) {
                resetField();
                return;
            }
            
            if (index == -3) {
                if (!previousPlayers.isEmpty()) {
                    currentPlayer = previousPlayers.pop();
                    previousState = previousStates.pop();
                    currentState = previousState;
                    update(currentState, currentPlayer);
                }
                
                return;
            }
            
            if (index == -2) {
                previousPlayers.push(currentPlayer);
                previousStates.push(previousState);
                
                confirmComponent.setEnabled(false);
                currentPlayer = currentPlayer.switchPlayer();
                
                // Prüfe, ob Computer gewonnen hat (bevor er gezogen hat)
                if (wins(currentState, currentPlayer)) {
                    setTitle(String.format(ABALONE_JUNIOR_PLAYER_WINS, currentPlayer));
                    update(currentState, currentPlayer);
                    return;
                }
                
                LOGGER.info("{} Standard-Score: {}", currentPlayer.switchPlayer(), score(currentState, currentPlayer.switchPlayer(), currentPlayer.switchPlayer()));
                currentState.printFieldDelta(previousState);
                Field previous = currentState;
                setTitle("Waiting for move...");
                currentState = artificicalIntelligence.apply(currentState, currentPlayer);
                LOGGER.info("{} Standard-Score: {}", currentPlayer, score(currentState, currentPlayer, currentPlayer));
                currentState.printFieldDelta(previous);
                currentPlayer = currentPlayer.switchPlayer();
                update(currentState, currentPlayer);
                
                // Prüfe, ob Spieler gewonnen hat (bevor er gezogen hat)
                if (wins(currentState, currentPlayer)) {
                    setTitle(String.format(ABALONE_JUNIOR_PLAYER_WINS, currentPlayer));
                }
                
                return;
            }
            
            Player player = lookAtField(currentState, index);
            if (lastColor != null) {
                player = lastColor;
                lastColor = null;
                currentState = populateField(currentState, index, player);
            } else if (player != EMPTY) {
                lastColor = player;
                player = EMPTY;
                currentState = populateField(currentState, index, EMPTY);
            }
            
            source.setBackground(player.getColor());
            
            confirmComponent.setEnabled(validMoves.length == 0 && previousState == currentState);
            for (Field move : validMoves) {
                if (currentState.equals(move)) {
                    confirmComponent.setEnabled(true);
                    break;
                }
            }
        }
        
        public void paint() {
            if (index < 0) return;
            Player player = lookAtField(currentState, index);
            source.setBackground(player.getColor());
        }
        
        private void resetField() {
            LOGGER.info("Score: {} {}", Player.WHITE, score(currentState, Player.WHITE, Player.WHITE));
            LOGGER.info("Score: {} {}", Player.BLACK, score(currentState, Player.BLACK, Player.BLACK));
            final StringBuilder sb = new StringBuilder();
            sb.append("Field.of(new Player[]{");
            sb.append(lookAtField(currentState, 0));
            for (int i = 1; i < Field.FIELD_SIZE; i++) {
                sb.append(',');
                sb.append(lookAtField(currentState, i));
            }
            
            sb.append("})");
            LOGGER.info(sb.toString());
            currentState = previousState;
            lastColor = null;
            redraw();
        }
        
        private void update(Field state, Player player) {
            init(state, player);
            redraw();
        }
        
        private void redraw() {
            for (Component component : fieldComponents) {
                if (component instanceof Label label && 
                        label.getMouseListeners()[0] instanceof LabelMouseListener listener) {
                    listener.paint();
                }
                
                if (component instanceof Button button && 
                    button.getActionListeners()[0] instanceof ButtonActionListener listener) {
                    listener.paint();
                }
            }
            
            confirmComponent.setEnabled(validMoves.length == 0);
            resetComponent.setBackground(currentPlayer.getColor());
        }
        @Override
        public void mousePressed(MouseEvent e) {
            // not needed
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            // not needed
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            // not needed
        }
        @Override
        public void mouseExited(MouseEvent e) {
            // not needed
        }
    }

    private class UIFrameWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            e.getWindow().dispose();
        }
    }
    
    public void waitOnResult() {
        while(this.isShowing()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public Field getCurrentState() {
        return currentState;
    }

    public void setArtificialIntelligence(BiFunction<Field, Player, Field> artificicalIntelligence) {
        this.artificicalIntelligence = artificicalIntelligence;
    }
}
