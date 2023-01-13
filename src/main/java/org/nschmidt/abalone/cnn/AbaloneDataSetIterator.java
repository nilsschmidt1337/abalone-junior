package org.nschmidt.abalone.cnn;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import org.datavec.api.io.filters.RandomPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.fetchers.DataSetType;
import org.nschmidt.abalone.playfield.Field;

public class AbaloneDataSetIterator extends RecordReaderDataSetIterator {

    private static final long serialVersionUID = 1L;
    
    private static RecordReader rr = new ImageRecordReader(Field.FIELD_HEIGHT, Field.FIELD_WIDTH, 3, new ParentPathLabelGenerator(), null);

    public AbaloneDataSetIterator(int batchSize, DataSetType set) {
        super(rr, batchSize);
        
        File localCache = getLocalCacheDir();
        Random rng = new Random(123);
        File datasetPath;
        switch (set) {
            case TRAIN:
                datasetPath = new File(localCache, "/train/");
                break;
            case TEST:
                datasetPath = new File(localCache, "/test/");
                break;
            case VALIDATION:
                throw new IllegalArgumentException("You will need to manually create and iterate a validation directory, CIFAR-10 does not provide labels");

            default:
                datasetPath = new File(localCache, "/train/");
        }

        // set up file paths
        RandomPathFilter pathFilter = new RandomPathFilter(rng, BaseImageLoader.ALLOWED_FORMATS);
        FileSplit filesInDir = new FileSplit(datasetPath, BaseImageLoader.ALLOWED_FORMATS, rng);
        InputSplit[] filesInDirSplit = filesInDir.sample(pathFilter, 1);

        try {
            rr.initialize(filesInDirSplit[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected File getLocalCacheDir(){
        try {
            return new File(AbaloneDataSetIterator.class.getResource("data.txt").toURI()).getParentFile();
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
