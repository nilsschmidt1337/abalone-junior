package org.nschmidt.tictactoe;

import java.util.Arrays;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    
    public static void main(String[] args) {
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
        
        // 1. Aktuelle Position von X 
        
        ComputationGraph model = DualResnetModel.getModel(fieldSize, residualBlockCount, numPlanes);
        
        INDArray[] inputs = model.getInputs();
        inputs[0] = Nd4j.create(new double[1][5][3][3]);
        INDArray[] outputs = new INDArray[] {Nd4j.create(new double[1][9]), Nd4j.create(new double[1][1])};
        

        
        LOGGER.info(Arrays.toString(inputs));

        for (int i = 0; i < 100; i++) {
            model.fit(inputs, outputs);
            INDArray[] output = model.output(inputs);
            LOGGER.info(Arrays.toString(output));
        }
    }
}
