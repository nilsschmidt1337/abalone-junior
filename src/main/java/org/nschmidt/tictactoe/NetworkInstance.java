package org.nschmidt.tictactoe;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum NetworkInstance {
    INSTANCE;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkInstance.class);

    static int fieldSize = 3;
    static int residualBlockCount = 20;
    static int numPlanes = 5;
    
    static ComputationGraph model = DualResnetModel.getModel(fieldSize, residualBlockCount, numPlanes);
    
    // Die fünf Ebenen
    //           /\
    //          / X\
    //          \X /  Die letzten beiden Zustände für X, der aktuellste steht oben.
    //          \\//
    //           \/
    
    //           /\
    //          /O \
    //          \ O/  Die letzten beiden Zustände für O
    //          \\//
    //           \/

    //           /\
    //          /XX\
    //          \XX/  Der Spieler, welcher gerade dran ist
    //           \/
    
    
    static PredictionResult predict(McNode node) {
        double[][][][] inputStack = createStack(node);
        INDArray[] inputs = model.getInputs();
        inputs[0] = Nd4j.create(inputStack);

        // LOGGER.info("Input :\n{}", Arrays.toString(inputs));
        INDArray[] output = model.output(inputs);
        // LOGGER.info("Output :\n{}", Arrays.toString(output));
        
        return new PredictionResult(output[1].toDoubleVector()[0], output[0].toDoubleVector());
    }
    
    static void retrain(McNode node, McNode resultNode) {
        if (!node.state.isGameOver() && node.childs.size() != node.state.moves().length) {
            return;
        }
        
        double[][][][] inputStack = createStack(node);
        INDArray[] inputs = model.getInputs();
        inputs[0] = Nd4j.create(inputStack);
        
        double[][] probabilities;
        double[][] value = new double[1][1];
        
        value[0][0] = resultNode.Q;
        probabilities = node.calculateProbabilities();
        
        
        INDArray[] outputs = new INDArray[] {Nd4j.create(probabilities), Nd4j.create(value)};
        
        model.fit(inputs, outputs);
    }

    static double[][][][] createStack(McNode node) {
        double[][][][] inputStack = new double[1][5][3][3];
        
        // Die letzten beiden Zustände für X und O
        addToStack(node.state.X(), 0, inputStack);
        addToStack(node.state.O(), 2, inputStack);
        
        // Die vorletzten beiden Zustände für X und O
        if (node.parent != null) {
            addToStack(node.parent.state.X(), 1,  inputStack);
            addToStack(node.parent.state.O(), 3,  inputStack);
        }
        
        // Welcher Spieler ist dran
        double currentPlayer = node.state.currentPlayer() == 'X' ? 1 : 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                inputStack[0][4][x][y] = currentPlayer;
            }
        }
        
        return inputStack;
    }

    private static void addToStack(boolean[] stateOfPlayer, int planeIndex, double[][][][] inputStack) {
        int c = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                inputStack[0][planeIndex][x][y] = stateOfPlayer[c] ? 1 : 0;
                c++;
            }
        }
    }
}
