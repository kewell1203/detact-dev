package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataColumnType;
import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.analysis.data.DataValue;
import de.symate.detact.analysis.data.convert.DataScaled;
import de.symate.detact.analysis.svm.CostParameter;
import de.symate.detact.analysis.svm.KernelSvm;
import de.symate.detact.analysis.svm.kernel.OneDimCauchyKernel;
import de.symate.detact.analysis.svm.kernel.OneDimGaussKernel;
import de.symate.detact.analysis.svm.ratter.RatterKernelFunction;
import de.symate.detact.analysis.svm.ratter.RatterKernelParameter;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * User: Eduard
 */
public class SVMTest2 extends JPanel{
    private static final Color COLOR_POINT0 = new Color(225,140,0);
    private static final Color COLOR_POINT1 = new Color(0,191,225);
    private static final Color COLOR_BACK0 = new Color(250, 250, 210);
    private static final Color COLOR_BACK1 = new Color(224, 255, 255);
    private BufferedImage image;
    private DataHandler data;
//    private RatterKernelFunction kernel;
    private RatterKernel kernel;
    private KernelSvm<RatterKernelParameter> s;
    private SvmHandler<KernelSvm<RatterKernelParameter>, RatterKernelParameter, CostParameter> svmHandler;

    public SVMTest2(DataHandler data){
        this.data = data;
        kernel = RatterKernelFactory.getKernel(RatterKernelTanhGauss.class);
        s = new KernelSvm<RatterKernelParameter>(data, kernel);
    }


    public void drawSVM(int width, int height, svm_model model, DataHandler data, DataColumnType xType, DataColumnType yType,
                                RatterKernelParameter k, CostParameter p) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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

        Graphics2D g = image.createGraphics();

        for(int px = 0; px < width; px++) {
            double x = xMin + (xMax - xMin) * px / (width - 1);
            double y = kernel.getDecision(k, model, data, new double[]{x});
            if(y > yMax) {
                g.setColor(COLOR_BACK1);
                g.drawLine(px, 0, px, height);
            }
             if(y < yMin) {
                g.setColor(COLOR_BACK0);
                g.drawLine(px, 0, px, height);
            }
            else {
                double factor = (yMax - y) / (yMax - yMin);
                int py = (int) (factor * (height - 1));
                g.setColor(COLOR_BACK0);
                g.drawLine(px, 0, px, py-1);
                g.setColor(COLOR_BACK1);
                g.drawLine(px, py+1, px, height);
            }
        }
        DataValue[][] ivs = data.independentValues();


        for(int i = 0; i < ivs.length; i++) {
            double x = ivs[i][1].getDouble();
            int px = (int) ((x - xMin) / (xMax - xMin) * width);
            double y = ivs[i][0].getDouble();
            int py = (int) ((yMax - y) / (yMax - yMin) * height);

            if(data.getResult().getDouble(i) < 0){
                g.setColor(COLOR_POINT0);
                g.fillRect(px-2, py-2, 5, 5);
            } else {
                g.setColor(COLOR_POINT1);
                g.fillRect(px-2, py-2, 5, 5);
            }
        }
        this.image = image;

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }


    public static void main(String[] args) {

        DataHandler data = new DataHandler(new DataScaled(new RatterData("ratterx30")));
        SVMTest2 svm2 = new SVMTest2(data);
        RatterKernelParameter k = new RatterKernelParameter(10.0, .25, 10);

        CostParameter p = new CostParameter(10);

        svm_parameter param = svm2.s.createParameter(k, p);
        svm_problem prob = svm2.s.createProblem(k);
        svm_model model = svm.svm_train(prob, param);
        SvmModelResult smr = new SvmModelResult(model,data);
        double r = svm2.kernel.getDecision(k,model, data,new double[] {0.4});
        svm2.drawSVM(600, 480, model, data, new DataColumnTypeOfflineImpl("omega"), new DataColumnTypeOfflineImpl("d"), k, p);
        JFrame frame = new JFrame("SVMTest2");
        frame.add(svm2);
        svm2.setPreferredSize(new Dimension(600, 480));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void bla() {
        RatterKernelFunction test = new RatterKernelFunction(RatterData.dType, RatterData.omegaType,
                new OneDimDPartKernelFunction(new OneDimDPartTanhKernelFunction()),
                new OneDimGaussKernel());

    }

}
