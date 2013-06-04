package de.gdietz.test.svm;

import java.util.ArrayList;
import java.util.List;

public class DataHandler implements Data {

    private Data data;

    private Column result;

    private List<Column> independents;

    public DataHandler(Data data) {
        this.data = data;
        result = get(getResultType());
        independents = new ArrayList<Column>();
        for (ColumnType independent : getIndependentTypes())
            independents.add(get(independent));
    }

    public List<Column> getData() {
        return data.getData();
    }

    public ColumnType getResultType() {
        return data.getResultType();
    }

    public List<ColumnType> getIndependentTypes() {
        return data.getIndependentTypes();
    }

    public Column get(ColumnType type) {
        for (Column column : getData())
            if (column.getType().equals(type))
                return column;
        throw new NoSuchParameterException("Parameter not found.");
    }

    public int getIndependentIndex(ColumnType type) {
        for (int i = 0; i < independents.size(); i++)
            if (independents.get(i).getType().equals(type))
                return i;
        throw new NoSuchParameterException("Parameter not found.");
    }

    public Column getResult() {
        return result;
    }

    public List<Column> getIndependents() {
        return independents;
    }

    public int size() {
        // TODO check, if all sizes equal
        return result.size();
    }

    public int psize() {
        return independents.size();
    }

    public Value[] independentValues(int index) {
        int psize = psize();
        Value[] values = new Value[psize];
        for (int i = 0; i < psize; i++) {
            Column column = independents.get(i);
            values[i] = new Value(column.getType(), column.get(index));
        }
        return values;
    }

    public Value[] independentValues(Value... independentValues) {
        int psize = psize();
        Value[] values = new Value[psize];
        for (Value independentValue : independentValues)
            values[getIndependentIndex(independentValue.getType())] = independentValue;
        return values;
    }

    public Value[][] independentValues() {
        int size = size();
        int psize = psize();

        Value[][] values = new Value[size][psize];

        for (int j = 0; j < psize; j++) {
            Column column = independents.get(j);
            ColumnType type = column.getType();
            for (int i = 0; i < size; i++) {
                double value = column.get(i);
                values[i][j] = new Value(type, value);
            }
        }

        return values;
    }

    public static double[] minMax(Column column) {
        List<Double> values = column.getValues();
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        for (double value : values) {
            if (value < min)
                min = value;
            if (value > max)
                max = value;
        }

        return new double[] {min, max};
    }

    public double[] minMax(ColumnType type) {
        return minMax(get(type));
    }

    public static double sum(Column column) {
        List<Double> values = column.getValues();
        double sum = 0.0;
        for (double value : values)
            sum += value;
        return sum;
    }

    public static double mu(Column column) {
        return sum(column) / column.size();
    }

    public static double sigmaSqr(Column column) {
        List<Double> values = column.getValues();
        double mean = mu(column);
        double qsum = 0.0;
        for (double value : values) {
            double diff = value - mean;
            qsum += diff * diff;
        }
        return qsum / (column.size() - 1);
    }

    public static double sigma(Column column) {
        return Math.sqrt(sigmaSqr(column));
    }

}
