package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataColumnType;

public class QuasiNormData extends FakeData {

    public static final DataColumnType xType = new DataColumnTypeOfflineImpl("x");
    public static final DataColumnType yType = new DataColumnTypeOfflineImpl("y");
    public static final DataColumnType zType = new DataColumnTypeOfflineImpl("z");

    private static final int ITER = 100;
    private static final double ONE_TWELFTH = 1.0 / 12;
    private static final double SIGMA = Math.sqrt(ONE_TWELFTH / ITER);

    public static double quasiNormRandom(double mu, double sigma) {
        double x = 0;
        for (int i = 0; i < ITER; i++)
            x += Math.random() - 0.5;

        x = x / ITER;

        return mu + x * sigma / SIGMA;
    }

    public QuasiNormData(DataColumnType xType, DataColumnType yType,
                         double muX0, double muY0, double sigma0, int count0,
                         double muX1, double muY1, double sigma1, int count1) {
        super(zType, xType, yType);

        for (int i = 0; i < count0; i++) {
            double x = quasiNormRandom(muX0, sigma0);
            double y = quasiNormRandom(muY0, sigma0);
            add(-1, x, y);
        }
        for (int i = 0; i < count1; i++) {
            double x = quasiNormRandom(muX1, sigma1);
            double y = quasiNormRandom(muY1, sigma1);
            add(1, x, y);
        }
    }

    public QuasiNormData(double muX0, double muY0, double sigma0, int count0,
                         double muX1, double muY1, double sigma1, int count1) {
        this(xType, yType, muX0, muY0, sigma0, count0, muX1, muY1, sigma1, count1);
    }

}
