package de.gdietz.test.svm;

import libsvm.*;

import java.awt.*;
import java.awt.image.*;
import java.util.logging.Logger;

public class SvmHandler<S extends Svm<K, P>, K extends KernelParameter, P extends SvmParameter> {

    private static Logger log = Logger.getLogger(SvmHandler.class.getName());

    private DataHandler data;
    private S s;

    public SvmHandler(DataHandler data, S s) {
        this.data = data;
        this.s = s;
    }

    private static final Color COLOR_BACK_0 = new Color(255, 224, 224);
    private static final Color COLOR_BACK_1 = new Color(224, 255, 224);
    private static final Color COLOR_POINT_0 = new Color(255, 0, 0);
    private static final Color COLOR_POINT_1 = new Color(0, 255, 0);
    private static final Color COLOR_CORRECT = Color.WHITE;
    private static final Color COLOR_WRONG = Color.BLACK;

    private static void drawBack(BufferedImage image, int x, int y, boolean predict) {
        Color color = predict ? COLOR_BACK_1 : COLOR_BACK_0;
        image.setRGB(x, y, color.getRGB());
    }

    private static boolean drawMarker(Graphics2D graphics, int x, int y, boolean value, boolean predict) {
        Color color = value ? COLOR_POINT_1 : COLOR_POINT_0;
        graphics.setColor(color);
        graphics.fillRect(x - 2, y - 2, 5, 5);

        boolean correct = ! (value ^ predict);
        color = correct ? COLOR_CORRECT : COLOR_WRONG;
        graphics.setColor(color);
        graphics.drawLine(x - 1, y, x + 1, y);
        graphics.drawLine(x, y - 1, x, y + 1);

        return correct;
    }

    public BufferedImage createImage(int width, int height, K k, P p, ColumnType xType, ColumnType yType, Value... otherValues) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        log.info("Creating SVM problem and model.");

        int psize = otherValues.length + 2;
        Value[] values = new Value[psize];
        for (int j = 0; j < otherValues.length; j++)
            values[j + 2] = otherValues[j];

        svm_problem prob = s.createProblem(k);
        svm_parameter param = s.createParameter(k, p);

        svm_model model = svm.svm_train(prob, param);
        log.info("Drawing background.");

        double[] xMinMax = data.minMax(xType);
        double[] yMinMax = data.minMax(yType);

        double xSize = xMinMax[1] - xMinMax[0];
        double ySize = yMinMax[1] - yMinMax[0];
        double xMid = (xMinMax[0] + xMinMax[1]) * 0.5;
        double yMid = (yMinMax[0] + yMinMax[1]) * 0.5;

        double xMin = xMid - xSize * 0.6;
        double xMax = xMid + xSize * 0.6;
        double yMin = yMid - ySize * 0.6;
        double yMax = yMid + ySize * 0.6;

        for (int py = 0; py < height; py++) {
            double y = yMax + (yMin - yMax) * py / (height - 1);
            values[1] = new Value(yType, y);

            for (int px = 0; px < width; px++) {
                double x = xMin + (xMax - xMin) * px / (width - 1);
                values[0] = new Value(xType, x);

                double z = svm.svm_predict(model, s.createNode(k, values));
                drawBack(image, px, py, z >= 0.0);
            }
        }

        log.info("Drawing data points.");

        Column zColumn = data.getResult();
        Column xColumn = data.get(xType);
        Column yColumn = data.get(yType);

        Graphics2D graphics = image.createGraphics();

        int correctCount = 0;
        int wrongCount = 0;

        int size = data.size();
        for (int i = 0; i < size; i++) {
            double z = zColumn.get(i);
            double x = xColumn.get(i);
            double y = yColumn.get(i);

            int px = (int) ((x - xMin) / (xMax - xMin) * (width - 1) + .5);
            int py = (int) ((y - yMax) / (yMin - yMax) * (height - 1) + .5);

            values = data.independentValues(i);

            double zPred = svm.svm_predict(model, s.createNode(k, values));

            boolean correct = drawMarker(graphics, px, py, z >= 0.0, zPred >= 0.0);

            if (correct)
                correctCount++;
            else
                wrongCount++;
        }

        String info = k.toString() + "; " + p.toString();
        String[] infoParts = info.split("; ");

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        int line = 0;
        for (String str : infoParts)
            graphics.drawString(str, 3, 10 + 10 * line++);

        double ratio = (double) correctCount / (correctCount + wrongCount);
        String ratioInfo = String.format("%.1f%%", ratio * 100);
        graphics.drawString(ratioInfo, 3, height - 3);

        return image;
    }

}
