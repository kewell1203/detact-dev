package de.gdietz.test.svm;

import java.util.ArrayList;
import java.util.List;

public class QuasiNormData implements Data {

    private static final int ITER = 100;
    private static final double ONE_TWELFTH = 1.0 / 12;
    private static final double SIGMA = Math.sqrt(ONE_TWELFTH / ITER);

    private ColumnType resultType;

    private List<ColumnType> independentTypes;

    private List<Column> data;

    public static double quasiNormRandom(double mu, double sigma) {
        double x = 0;
        for (int i = 0; i < ITER; i++)
            x += Math.random() - 0.5;

        x = x / ITER;

        return mu + x * sigma / SIGMA;
    }

    public QuasiNormData(double muX0, double muY0, double sigma0, int count0,
                         double muX1, double muY1, double sigma1, int count1) {
        resultType = new ColumnType("z");
        ColumnType independentXType = new ColumnType("x");
        ColumnType independentYType = new ColumnType("y");

        independentTypes = new ArrayList<ColumnType>();
        independentTypes.add(independentXType);
        independentTypes.add(independentYType);

        Column result = new Column(resultType);
        Column independentX = new Column(independentXType);
        Column independentY = new Column(independentYType);

        data = new ArrayList<Column>();
        data.add(independentX);
        data.add(independentY);
        data.add(result);

        for (int i = 0; i < count0; i++) {
            double x = quasiNormRandom(muX0, sigma0);
            double y = quasiNormRandom(muY0, sigma0);

            independentX.add(x);
            independentY.add(y);
            result.add(-1);
        }

        for (int i = 0; i < count1; i++) {
            double x = quasiNormRandom(muX1, sigma1);
            double y = quasiNormRandom(muY1, sigma1);

            independentX.add(x);
            independentY.add(y);
            result.add(1);
        }
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
