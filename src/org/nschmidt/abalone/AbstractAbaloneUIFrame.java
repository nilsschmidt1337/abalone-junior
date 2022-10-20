package org.nschmidt.abalone;

import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.FieldEvaluator.score;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.Player.EMPTY;
import static org.nschmidt.abalone.WinningChecker.wins;

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
import java.util.List;
import java.util.function.BiFunction;

abstract class AbstractAbaloneUIFrame extends Frame {

    private static final String ABALONE_JUNIOR_PLAYER_WINS = "Abalone Junior - Player %s wins!";

    private static final long serialVersionUID = 1L;

    protected GridBagLayout grid = new GridBagLayout();
    protected GridBagConstraints straints = new GridBagConstraints();
    
    private Field previousState;
    private Field[] validMoves;
    private Field currentState;
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
       
        // Reset label
        resetComponent = addComponent(0, 0, true, currentPlayer.getColor(), -1);
        
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 13; x++) {
                if (x == 0 && y == 0) continue;
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
            setTitle("Abalone Junior");
        }
    }

    private void fillGrid(int x, int y) {
        int index = -1; 
        index = calculateIndex(x, y, index);
        
        
        Color color = Color.LIGHT_GRAY;
        
        if (index > -1) {
            Player player = lookAtField(currentState, index);
            color = player.getColor();
        }
        
        fieldComponents.add(addComponent(x, y, index != -1, color, index));
    }

    private int calculateIndex(int x, int y, int index) {
        if (y == 0 && x > 2 && x < 10 && (x + 1) % 2 == 0) index = (x + 1) / 2 - 2;
        if (y == 1 && x > 1 && x < 11 && (x + 0) % 2 == 0) index = (x + 1) / 2 + 3;
        if (y == 2 && x > 0 && x < 12 && (x + 1) % 2 == 0) index = (x + 1) / 2 + 8;
        if (y == 3 && x % 2 == 0) index = (x + 1) / 2 + 15;
        if (y == 4 && x > 0 && x < 12 && (x + 1) % 2 == 0) index = (x + 1) / 2 + 21;
        if (y == 5 && x > 1 && x < 11 && (x + 0) % 2 == 0) index = (x + 1) / 2 + 27;
        if (y == 6 && x > 2 && x < 10 && (x + 1) % 2 == 0) index = (x + 1) / 2 + 31;
        return index;
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
            
            if (index == -2) {
                
                currentPlayer = currentPlayer.switchPlayer();
                
                // Prüfe, ob Computer gewonnen hat (bevor er gezogen hat)
                if (wins(currentState, currentPlayer)) {
                    setTitle(String.format(ABALONE_JUNIOR_PLAYER_WINS, currentPlayer));
                    update(currentState, currentPlayer);
                    return;
                }
                
                System.out.println(currentState);
                System.out.println(currentPlayer.switchPlayer() + " Standard-Score: " + score(currentState, currentPlayer.switchPlayer()));
                currentState.printFieldDelta(previousState);
                Field previous = currentState;
                currentState = artificicalIntelligence.apply(currentState, currentPlayer);
                System.out.println(currentState);
                System.out.println(currentPlayer + " Standard-Score: " + score(currentState, currentPlayer));
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
            if (lastColor != null && player == EMPTY) {
                player = lastColor;
                lastColor = null;
                currentState = populateField(currentState, index, player);
            } else if (lastColor == null && player != EMPTY) {
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
