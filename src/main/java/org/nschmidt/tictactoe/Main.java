package org.nschmidt.tictactoe;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final int TOTAL_GAMES_TO_SIMULATE = 10;
    private static final int TOTAL_TRAINING_SESSION_COUNT = 100;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        for (int trainingSessionCounter = 0; trainingSessionCounter < TOTAL_TRAINING_SESSION_COUNT; trainingSessionCounter++) {
            
            List<McNode> allNodes = new ArrayList<>();
            
            McNode node = McNode.create();
            allNodes.add(node);
            while (!node.state.isGameOver()) {
                for (int i = 0; i < TOTAL_GAMES_TO_SIMULATE; i++) {
                    node.simulate();
                }
                
                node = node.chooseRandomNode();
                allNodes.add(node);
                String board = node.state.toString();
                LOGGER.info("explore {}", board);
            }
            
            int allNodesSize = allNodes.size();
            for (int i = 0; i < allNodesSize; i++) {
                LOGGER.info("Session {}: Train {} of {}", trainingSessionCounter + 1, i + 1, allNodesSize);
                allNodes.get(i).retrain();
            }
            
            playGame();
        }
    }

    private static void playGame() {
        McNode game = McNode.create();
        while (!game.state.isGameOver()) {
            for (int i = 0; i < TOTAL_GAMES_TO_SIMULATE; i++) {
                game.simulate();
            }
            
            double maxN = 0;
            McNode maxNode = game;
            for (McNode n : game.childs) {
                if (n.N > maxN) {
                    maxN = n.N;
                    maxNode = n;
                }
            }
            
            String board = maxNode.state.toString();
            LOGGER.info("compete {}", board);
            game = maxNode;
        }
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
