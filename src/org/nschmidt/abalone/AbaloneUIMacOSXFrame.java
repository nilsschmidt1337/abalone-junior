package org.nschmidt.abalone;

import static org.nschmidt.abalone.Backtracker.backtrack;
import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.MoveDetector.allMoves;
import static org.nschmidt.abalone.WinningChecker.wins;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;

public class AbaloneUIMacOSXFrame extends Frame {

    private static final String ABALONE_JUNIOR_PLAYER_WINS = "Abalone Junior - Player %d wins!";

    private static final long serialVersionUID = 1L;

    private GridBagLayout grid = new GridBagLayout();
    private GridBagConstraints straints = new GridBagConstraints();
    
    private long previousState;
    private long[] validMoves;
    private long currentState;
    private long currentPlayer;
    private long lastColor = -1;
    
    private final Label confirmLabel;
    private final Label resetLabel;
    
    private final List<Label> fieldLabels = new ArrayList<>();
    
    private transient LongBinaryOperator artificicalIntelligence = (state, player) -> state;
    
    public AbaloneUIMacOSXFrame(long state, long currentPlayer) {
        init(state, currentPlayer);
        
        setLayout(grid);
        addWindowListener(new UIFrameWindowListener());
       
        // Reset label
        if (currentPlayer == 1L) {
            resetLabel = addLabel(0, 0, true, Color.WHITE, -1);
        } else {
            resetLabel = addLabel(0, 0, true, Color.BLACK, -1);
        }
        
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 13; x++) {
                if (x == 0 && y == 0) continue;
                fillGrid(x, y);
            }
        }
        
        confirmLabel = addConfirmLabel();
           
        pack();
        setVisible(true);
    }

    private void init(long state, long currentPlayer) {
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
            long player = lookAtField(currentState, index);
            if (player == 0L) color = Color.BLUE;
            if (player == 1L) color = Color.WHITE;
            if (player == 2L) color = Color.BLACK;
        }
        
        fieldLabels.add(addLabel(x, y, index != -1, color, index));
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

    private Label addLabel(int x, int y, boolean enabled, Color color, int index) {
        straints.gridx = x;
        straints.gridy = y;
        straints.gridheight = 1;
        straints.gridwidth = 1;
        straints.fill = GridBagConstraints.BOTH;
        straints.ipadx = 19;
        straints.ipady = 14;
        Label label = new Label();
        label.setEnabled(enabled);
        label.setBackground(color);
        label.addMouseListener(new LabelMouseListener(index));
        
        grid.setConstraints(label, straints);
        add(label);
        return label;
    }
    
    private Label addConfirmLabel() {
        straints.gridx = 0;
        straints.gridy = 7;
        straints.gridheight = 1;
        straints.gridwidth = 13;
        straints.fill = GridBagConstraints.BOTH;
        straints.ipadx = 19;
        straints.ipady = 14;
        Label label = new Label("Confirm");
        label.setEnabled(false);
        
        label.addMouseListener(new LabelMouseListener(-2));
        
        grid.setConstraints(label, straints);
        add(label);
        return label;
    }
    
    private class LabelMouseListener implements MouseListener {
        final int index;
        public LabelMouseListener(int index) {
            super();
            this.index = index;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (index == -1) {
                resetField();
                return;
            }
            
            if (index == -2) {
                
                currentPlayer = currentPlayer % 2 + 1;
                
                // Prüfe, ob Computer gewonnen hat (bevor er gezogen hat)
                if (wins(currentState, currentPlayer)) {
                    setTitle(String.format(ABALONE_JUNIOR_PLAYER_WINS, currentPlayer));
                    update(currentState, currentPlayer);
                    return;
                }
                
                currentState = artificicalIntelligence.applyAsLong(currentState, currentPlayer);
                currentPlayer = currentPlayer % 2 + 1;
                update(currentState, currentPlayer);
                
                // Prüfe, ob Spieler gewonnen hat (bevor er gezogen hat)
                if (wins(currentState, currentPlayer)) {
                    setTitle(String.format(ABALONE_JUNIOR_PLAYER_WINS, currentPlayer));
                }
                
                return;
            }
            
            long player = lookAtField(currentState, index);
            if (lastColor > 0 && player == 0) {
                player = lastColor;
                lastColor = -1;
                currentState = populateField(currentState, index, player);
            } else if (lastColor == -1 && player != 0) {
                lastColor = player;
                player = 0;
                currentState = populateField(currentState, index, 0);
            }
            
            if (player == 0L) ((Label) e.getSource()).setBackground(Color.BLUE);
            if (player == 1L) ((Label) e.getSource()).setBackground(Color.WHITE);
            if (player == 2L) ((Label) e.getSource()).setBackground(Color.BLACK);
            
            confirmLabel.setEnabled(validMoves.length == 0 && previousState == currentState);
            for (long move : validMoves) {
                if (move == currentState) {
                    confirmLabel.setEnabled(true);
                    break;
                }
            }
        }
        
        public void paint(Label label) {
            if (index < 0) return;
            long player = lookAtField(currentState, index);
            if (player == 0L) label.setBackground(Color.BLUE);
            if (player == 1L) label.setBackground(Color.WHITE);
            if (player == 2L) label.setBackground(Color.BLACK);
        }
        
        private void resetField() {
            currentState = previousState;
            lastColor = -1;
            redraw();
        }
        
        private void update(long state, long player) {
            init(state, player);
            redraw();
        }
        
        private void redraw() {
            for (Label label : fieldLabels) {
                if (label.getMouseListeners()[0] instanceof LabelMouseListener listener) {
                    listener.paint(label);
                }
            }
            
            confirmLabel.setEnabled(validMoves.length == 0);
            if (currentPlayer == 1L) {
                resetLabel.setBackground(Color.WHITE);
            } else {
                resetLabel.setBackground(Color.BLACK);
            }
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
    
    public static void main(String[] args) 
    {
      AbaloneUIMacOSXFrame frame = new AbaloneUIMacOSXFrame(INITIAL_FIELD, 1L);
      frame.setArtificialIntelligence((state, player) -> backtrack(state, player, 10));
      frame.waitOnResult();
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
    
    public long getCurrentState() {
        return currentState;
    }

    public void setArtificialIntelligence(LongBinaryOperator artificicalIntelligence) {
        this.artificicalIntelligence = artificicalIntelligence;
    }
}
