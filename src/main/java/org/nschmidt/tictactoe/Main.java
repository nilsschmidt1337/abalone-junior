package org.nschmidt.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        McNode node = McNode.create();
        
        for (int i = 0; i < 100; i++) {
            node.simulate();
            LOGGER.info("{}", node.size());
        }
        
        List<McNode> allNodes = new ArrayList<>();
        node.collect(allNodes);
        
        for (int i = 0; i < allNodes.size(); i++) {
            LOGGER.info("Train {}", i);
            allNodes.get(i).retrain();
        }
        
        McNode game = McNode.create();
        while (!game.state.isGameOver()) {
            for (int i = 0; i < 100; i++) {
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
            LOGGER.info(board);
            game = maxNode;
        }
    }
    
    public static void main2(String[] args) {
        Board.create()
                .applyMove(0)
                .applyMove(1)
                .applyMove(2)
                .toString();

        Board.create()
                .applyMove(0)
                .applyMove(3)
                .applyMove(1)
                .applyMove(4)
                .applyMove(2)
                .wins('X');
        
        int fieldSize = 3;
        int residualBlockCount = 20;
        int numPlanes = 5;
        
        ComputationGraph model = DualResnetModel.getModel(fieldSize, residualBlockCount, numPlanes);
        
        INDArray[] inputs = model.getInputs();
        inputs[0] = Nd4j.create(new double[1][5][3][3]);
        INDArray[] outputs = new INDArray[] {Nd4j.create(new double[1][10]), Nd4j.create(new double[1][1])};
        
        LOGGER.info(Arrays.toString(inputs));

        for (int i = 0; i < 100; i++) {
            model.fit(inputs, outputs);
            INDArray[] output = model.output(inputs);
            LOGGER.info(Arrays.toString(output));
        }
    }
}
