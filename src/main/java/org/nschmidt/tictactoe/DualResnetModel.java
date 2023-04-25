package org.nschmidt.tictactoe;

import org.deeplearning4j.nn.graph.ComputationGraph;

/**
 * Definiert und lädt AlphaGo's "Zero dual ResNet" Architektur nach DL4J.
 * Hier vereinfacht für TicTacToe. 
 *
 * Die "dual residual architecture" ist die stärkste von DeepMind für AlphaGo
 * Zero entworfene Architektur.
 * 
 * Sie startet mit einem dreidimensionalen "Convolution Layer Block",
 * gefolgt von einer Reihe "Residual Blocks" (40). 
 * 
 * Das Netzwerk ist liefert zwei Ergebnisarrays zurück ("two heads").
 * Ein Array für Zugwahrscheinlichkeiten (Policies) und ein Array mit einem Wert von -1 bis 1 zur Bewertung der Position (Value).
 *
 */
public class DualResnetModel {

    // In Anlehnung an Max Pumperlas AlphaGo Zero Model:
    
    public static ComputationGraph getModel(int fieldSize, int residualBlocks, int numPlanes) {

        TicTacToeZeroBuilder builder = new TicTacToeZeroBuilder();
        String input = "in";

        builder.addInputs(input);
        String initBlock = "init";
        String convOut = builder.addConvBatchNormBlock(initBlock, input, numPlanes, true);
        String towerOut = builder.addResidualTower(residualBlocks, convOut);
        String policyOut = builder.addPolicyHead(towerOut);
        String valueOut = builder.addValueHead(towerOut);
        builder.addOutputs(policyOut, valueOut);

        ComputationGraph model = new ComputationGraph(builder.buildAndReturn());
        model.init();

        return model;
    }
}