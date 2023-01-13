package org.nschmidt.abalone.cnn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.deeplearning4j.datasets.fetchers.DataSetType;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.ocnn.OCNNOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.common.primitives.Pair;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nschmidt.abalone.playfield.Field;

public class EvaluationByConvolutedNeuralNetwork {

    public static void main(String[] args) {

        InputType inputType = InputType.convolutional(Field.FIELD_HEIGHT, Field.FIELD_WIDTH, 2);

        MultiLayerConfiguration configuration = new NeuralNetConfiguration.Builder()
                .seed(1611)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list()
                .layer(0, new ConvolutionLayer.Builder().nIn(9).nOut(5).build())
                .layer(1, new ConvolutionLayer.Builder().nIn(5).nOut(5).build())
                .layer(2, new ConvolutionLayer.Builder().nIn(5).nOut(5).build())
                .layer(3, new ConvolutionLayer.Builder().nIn(5).nOut(5).build())
                .layer(4, new ConvolutionLayer.Builder().nIn(5).nOut(5).build())
                .layer(5, new ConvolutionLayer.Builder().nIn(5).nOut(5).build())
                .layer(6, new OCNNOutputLayer.Builder().nIn(5).hiddenLayerSize(1).build())
                .backpropType(BackpropType.Standard)
                .setInputType(inputType)
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(configuration);


        network.init();
        
        IntStream.range(1, 1 + 1).forEach(epoch -> {
            DataSetIterator foo = new AbaloneDataSetIterator(1, DataSetType.TRAIN);
            network.fit(foo);
        });

        /*DataSetIterator foo = new AbaloneDataSetIterator(1, DataSetType.TEST);
        Evaluation ev = network.evaluate(foo);

        System.out.println(ev.toString());*/
    }
}
