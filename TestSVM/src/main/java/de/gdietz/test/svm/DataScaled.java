package de.gdietz.test.svm;

import java.util.ArrayList;
import java.util.List;

public class DataScaled implements Data {

    private List<Column> data;

    private ColumnType resultType;
    private List<ColumnType> independentTypes;

    public DataScaled(Data origData) {
        data = new ArrayList<Column>();
        for (Column column : origData.getData()) {
            ColumnType type = column.getType();
            Column columnScaled = new Column(type);

            double[] minMax = DataHandler.minMax(column);
            double min = minMax[0];
            double max = minMax[1];
            double range = max - min;
            double mid = (min + max) / 2;

            //double mu = DataHandler.mu(column);
            //double sigma = DataHandler.sigma(column);

            for (double value : column.getValues()) {
                //double z = 0.0;
                //if (sigma != 0.0)
                    //z = (value - mu) / sigma;
                double z = (value - mid) / range * 2;
                columnScaled.add(z);
            }

            data.add(columnScaled);
        }

        resultType = origData.getResultType();
        independentTypes = origData.getIndependentTypes();
    }

    public List<Column> getData() {
        return data;
    }

    public ColumnType getResultType() {
        return resultType;
    }

    public List<ColumnType> getIndependentTypes() {
        return independentTypes;
    }

}
