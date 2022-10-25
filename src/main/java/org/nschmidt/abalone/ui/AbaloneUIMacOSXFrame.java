package org.nschmidt.abalone.ui;

import static org.nschmidt.abalone.ai.Backtracker.backtrack;
import static org.nschmidt.abalone.playfield.Field.FIELD_HEIGHT;
import static org.nschmidt.abalone.playfield.Field.FIELD_WIDTH;
import static org.nschmidt.abalone.playfield.Field.INITIAL_FIELD;
import static org.nschmidt.abalone.playfield.Player.WHITE;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Label;

import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;

public class AbaloneUIMacOSXFrame extends AbstractAbaloneUIFrame {

    private static final long serialVersionUID = 1L;

    public AbaloneUIMacOSXFrame(Field state, Player currentPlayer) {
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
        Label label = new Label();
        label.setEnabled(enabled);
        label.setBackground(color);
        label.addMouseListener(new LabelMouseListener(index, label));
        
        grid.setConstraints(label, straints);
        add(label);
        return label;
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
        Label label = new Label("Confirm");
        label.setEnabled(false);
        
        label.addMouseListener(new LabelMouseListener(-2, label));
        
        grid.setConstraints(label, straints);
        add(label);
        return label;
    }
    
    public static void main(String[] args) 
    {
      AbaloneUIMacOSXFrame frame = new AbaloneUIMacOSXFrame(INITIAL_FIELD, WHITE);
      frame.setArtificialIntelligence((state, player) -> backtrack(state, player, 10));
      frame.waitOnResult();
    }
}
