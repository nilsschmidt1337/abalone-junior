package org.nschmidt.abalone.cnn;

import static org.nschmidt.abalone.playfield.Field.lookAtField;

import java.io.IOException;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.evaluation.classification.EvaluationCalibration;
import org.nd4j.evaluation.curves.Histogram;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.SplitTestAndTrain;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nschmidt.abalone.ai.AI;
import org.nschmidt.abalone.ai.CnnBacktracker;
import org.nschmidt.abalone.ai.FastBacktracker;
import org.nschmidt.abalone.playfield.Field;
import org.nschmidt.abalone.playfield.Player;
import org.nschmidt.abalone.winning.WinningChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvaluationByConvolutedNeuralNetwork {

    private static final boolean REGRESSION = true;

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationByConvolutedNeuralNetwork.class);
    
    private static final int FEATURES_COUNT = 61;
    
    private static MultiLayerNetwork modelBlack = null;
    private static MultiLayerNetwork modelWhite = null;

    public static void main(String[] args) {

        double s1 = 0;
        double s2 = 0;
        do {
            modelBlack = createModel("monte-carlo-black.txt");
            modelWhite = createModel("monte-carlo-white.txt");
            
            s1 = score(Field.INITIAL_FIELD, Player.BLACK);
            s2 = score(Field.EMPTY_FIELD, Player.BLACK);
        } while (Math.abs(s1) > 1e-4 || Math.abs(s2) > 1e-4);

        LOGGER.info("Score {}", s1);
        LOGGER.info("Score {}", s2);
        
        playGame();
    }
    
    private static void playGame() {
        Player currentPlayer;
        Field currentField;
        currentField = Field.INITIAL_FIELD;
        currentPlayer = Player.BLACK; 
        double moves = 1;
        FastBacktracker.clearCache();
        while (!WinningChecker.wins(currentField, currentPlayer)) {
            
            LOGGER.info(currentField.toString());
            
            if (Player.BLACK != currentPlayer) {
                currentField = FastBacktracker.backtrack(currentField, currentPlayer);
                if (currentField == null) {
                    currentField = AI.bestMove(currentField, currentPlayer);
                }
            } else {
                currentField = CnnBacktracker.backtrack(currentField, currentPlayer);
                if (currentField == null) {
                    currentField = AI.bestMove(currentField, currentPlayer);
                }
            }
            
            currentPlayer = currentPlayer.switchPlayer();
            moves++;
            
            if (moves > 1000) {
                break;
            }
        }
        
        LOGGER.info(currentField.toString());
    }

    private static MultiLayerNetwork createModel(String modelDataFile) {
        try (RecordReader recordReader = new CSVRecordReader(0, ',')) {
            recordReader.initialize(new FileSplit(
                    new ClassPathResource(modelDataFile).getFile()));
            
           DataSetIterator iterator = new RecordReaderDataSetIterator(recordReader, 900, FEATURES_COUNT, FEATURES_COUNT, REGRESSION);
            
           DataSet allData = iterator.next();
           allData.shuffle(42);
           
           DataNormalization normalizer = new NormalizerStandardize();
           normalizer.fit(allData);
           normalizer.transform(allData);
           
           SplitTestAndTrain testAndTrain = allData.splitTestAndTrain(0.5);
           DataSet trainingData = testAndTrain.getTrain();
           DataSet testData = testAndTrain.getTest();
           
        MultiLayerConfiguration configuration 
           = new NeuralNetConfiguration.Builder()
             .activation(Activation.RELU)
             .weightInit(WeightInit.XAVIER)
             .l2(0.0001)
             .list()
             .layer(0, new DenseLayer.Builder().nIn(FEATURES_COUNT).nOut(3).build())
             .layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
             .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                 .activation(Activation.IDENTITY)
                 .nIn(3).nOut(1).build())
             .build();
           
           MultiLayerNetwork model = new MultiLayerNetwork(configuration);
           model.init();
           model.fit(trainingData);
           
           INDArray output = model.output(testData.getFeatures());
           EvaluationCalibration eval = new EvaluationCalibration(10, 10);
           eval.eval(testData.getLabels(), output);
           
           
           Histogram histogram = eval.getResidualPlotAllClasses();
           LOGGER.info(histogram.getTitle());
           int binNumber = 1;
           for (int bin : histogram.getBinCounts()) {
               LOGGER.info(String.format("%02d : %d", binNumber, bin));
               binNumber++;
           }
           
           /*
           A residual plot is typically used to find problems with regression. Some data sets are not good candidates for regression, including:

               Heteroscedastic data (points at widely varying distances from the line).
               Data that is non-linearly associated.
               Data sets with outliers.

           These problems are more easily seen with a residual plot than by looking at a plot of the original data set. Ideally, residual values should be equally and randomly spaced around the horizontal axis.
           */
           
           return model;
           
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return null;
    }

    public static double score(Field field, Player player) {
        
        float[] data = new float[Field.FIELD_SIZE];
        
        for (int i = 0; i < Field.FIELD_SIZE; i++) {
            switch (lookAtField(field, i)) {
            case BLACK: 
                data[i] = 1.0f;
                break;
            case WHITE:
                data[i] = -1.0f;
                break;
            case EMPTY:
                data[i] = 0.0f;
                break;
            }
        }
        
        INDArray in = new NDArray(data);
        if (player == Player.BLACK) {
            INDArray out = modelBlack.output(in);
            return out.getDouble(0);
        } else {
            INDArray out = modelWhite.output(in);
            return out.getDouble(0);
        }
    }
}
