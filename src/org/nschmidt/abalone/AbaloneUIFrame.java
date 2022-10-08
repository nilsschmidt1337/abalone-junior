package org.nschmidt.abalone;

import static org.nschmidt.abalone.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.Field.lookAtField;
import static org.nschmidt.abalone.Field.populateField;
import static org.nschmidt.abalone.MoveDetector.allMoves;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class AbaloneUIFrame extends Frame {

    private static final long serialVersionUID = 1L;

    private GridBagLayout grid = new GridBagLayout();
    private GridBagConstraints straints = new GridBagConstraints();
    
    private final long previousState;
    private final long[] validMoves;
    private long currentState;
    private long lastColor = -1;
    
    private final Button confirmButton;
    
    private final List<Button> fieldButtons = new ArrayList<>();
    
    public AbaloneUIFrame(long state, long currentPlayer) {
        this.previousState = state;
        this.currentState = state;
        this.validMoves = allMoves(state, currentPlayer);
        setTitle("Abalone Junior");
        setLayout(grid);
        addWindowListener(new UIFrameWindowListener());
       
        // Reset button
        addButton(0, 0, true, Color.RED, -1);
        
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 13; x++) {
                if (x == 0 && y == 0) continue;
                fillGrid(x, y);
            }
        }
        
        confirmButton = addConfirmButton();
           
        pack();
        setVisible(true);
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
        
        fieldButtons.add(addButton(x, y, index != -1, color, index));
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

    private Button addButton(int x, int y, boolean enabled, Color color, int index) {
        straints.gridx = x;
        straints.gridy = y;
        straints.gridheight = 1;
        straints.gridwidth = 1;
        straints.fill = GridBagConstraints.BOTH;
        straints.ipadx = 19;
        straints.ipady = 14;
        Button button = new Button();
        button.setEnabled(enabled);
        button.setBackground(color);
        button.addActionListener(new ButtonActionListener(index));
        
        grid.setConstraints(button, straints);
        add(button);
        return button;
    }
    
    private Button addConfirmButton() {
        straints.gridx = 0;
        straints.gridy = 7;
        straints.gridheight = 1;
        straints.gridwidth = 13;
        straints.fill = GridBagConstraints.BOTH;
        straints.ipadx = 19;
        straints.ipady = 14;
        Button button = new Button("Confirm");
        button.setEnabled(false);
        button.addActionListener(new ButtonActionListener(-2));
        
        grid.setConstraints(button, straints);
        add(button);
        return button;
    }
    
    private class ButtonActionListener implements ActionListener {
        final int index;
        public ButtonActionListener(int index) {
            super();
            this.index = index;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (index == -1) {
                resetField();
                return;
            }
            
            if (index == -2) {
                close();
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
            
            if (player == 0L) ((Button) e.getSource()).setBackground(Color.BLUE);
            if (player == 1L) ((Button) e.getSource()).setBackground(Color.WHITE);
            if (player == 2L) ((Button) e.getSource()).setBackground(Color.BLACK);
            
            confirmButton.setEnabled(validMoves.length == 0 && previousState == currentState);
            for (long move : validMoves) {
                if (move == currentState) {
                    confirmButton.setEnabled(true);
                    break;
                }
            }
        }
        
        public void paint(Button button) {
            if (index < 0) return;
            long player = lookAtField(currentState, index);
            if (player == 0L) button.setBackground(Color.BLUE);
            if (player == 1L) button.setBackground(Color.WHITE);
            if (player == 2L) button.setBackground(Color.BLACK);
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
      AbaloneUIFrame frame = new AbaloneUIFrame(INITIAL_FIELD, 1L);
      frame.waitOnResult();
    }

    public void resetField() {
        currentState = previousState;
        for (Button button : fieldButtons) {
            if (button.getActionListeners()[0] instanceof ButtonActionListener listener) {
                listener.paint(button);
            }
        }
        
        confirmButton.setEnabled(validMoves.length == 0);
    }

    private void waitOnResult() {
        while(this.isShowing()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void close() {
        dispose();
    }

    public long getCurrentState() {
        return currentState;
    }
}
