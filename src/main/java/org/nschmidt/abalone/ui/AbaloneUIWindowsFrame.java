package org.nschmidt.abalone.ui;

import static org.nschmidt.abalone.playfield.Field.FIELD_HEIGHT;
import static org.nschmidt.abalone.playfield.Field.FIELD_WIDTH;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;

import org.nschmidt.abalone.ai.Backtracker;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public class AbaloneUIWindowsFrame extends AbstractAbaloneUIFrame {

    private static final long serialVersionUID = 1L;
    
    public AbaloneUIWindowsFrame(Field state, Player currentPlayer) {
        super(state, currentPlayer);
    }

    @Override
    protected Component addComponent(int x, int y, boolean enabled, Color color, int index) {
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
        button.addActionListener(new ButtonActionListener(index, button));
        
        grid.setConstraints(button, straints);
        add(button);
        return button;
    }
    
    @Override
    protected Component addConfirmComponent() {
        straints.gridx = 0;
        straints.gridy = FIELD_HEIGHT;
        straints.gridheight = 1;
        straints.gridwidth = FIELD_WIDTH;
        straints.fill = GridBagConstraints.BOTH;
        straints.ipadx = 19;
        straints.ipady = 14;
        Button button = new Button("Confirm");
        button.setEnabled(false);
        button.addActionListener(new ButtonActionListener(-2, button));
        
        grid.setConstraints(button, straints);
        add(button);
        return button;
    }
    
    public static void main(String[] args) 
    {
      AbaloneUIWindowsFrame frame = new AbaloneUIWindowsFrame(INITIAL_FIELD, WHITE);
      frame.setArtificialIntelligence(Backtracker::backtrack);
      frame.waitOnResult();
    }
}
