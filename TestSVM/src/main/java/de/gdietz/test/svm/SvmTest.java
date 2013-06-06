package de.gdietz.test.svm;

import de.symate.detact.analysis.data.Data;
import de.symate.detact.analysis.data.DataColumnType;
import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.analysis.data.convert.DataScaled;
import de.symate.detact.analysis.svm.CostParameter;
import de.symate.detact.analysis.svm.KernelSvm;
import de.symate.detact.analysis.svm.ratter.RatterKernelParameter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

public class SvmTest {
    private DataHandler data;
//    private RatterKernelFunction kernel;
    private RatterKernel kernel;
    private KernelSvm<RatterKernelParameter> s;
    private SvmHandler<KernelSvm<RatterKernelParameter>, RatterKernelParameter, CostParameter> svmHandler;

    private DataColumnType xType;
    private DataColumnType yType;

    private BufferedImage image;

    public SvmTest(DataHandler data, int width, int height) {
        this.data = data;
//        kernel = new RatterKernelFunction();
        kernel = RatterKernelFactory.getKernel(RatterKernelLinGauss.class);
        s = new KernelSvm<RatterKernelParameter>(data, kernel);
        svmHandler = new SvmHandler<KernelSvm<RatterKernelParameter>, RatterKernelParameter, CostParameter>(data, s);

        List<DataColumnType> independentTypes = data.getIndependentTypes();
        xType = independentTypes.get(0);
        yType = independentTypes.get(1);

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage createImage(int width, int height, double gamma1, double gamma2, double gamma, double cost) {
        RatterKernelParameter k = new RatterKernelParameter(gamma1, gamma2, gamma);
        CostParameter p = new CostParameter(cost);
        return svmHandler.createImage(width, height, k, p, xType, yType);
    }

    public void addImage(final Component observer, final Graphics2D graphics, final int x, final int y, final int width, final int height,
                         final double gamma1, final double gamma2, final double gamma, final double cost) {
        new Thread(new Runnable() {
            public void run() {
                BufferedImage part = createImage(width, height, gamma1, gamma2, gamma, cost);
                graphics.drawImage(part, x, y, observer);
                observer.repaint();
            }
        }).start();
    }

    public void addImages(Component observer, double[] gamma1s, double[] gamma2s, double[] gammas, double[] costs) {
        int width = image.getWidth();
        int height = image.getHeight();

        int countX = gammas.length * gamma1s.length;
        int countY = costs.length * gamma2s.length;

        int partWidth = width / countX;
        int partHeight = height / countY;

        Graphics2D graphics = image.createGraphics();

        final Color COLOR_LINE = new Color(192, 192, 192);
        final Color COLOR_LINE1 = Color.BLACK;

        for (int cx = 0; cx < countX - 1; cx++) {
            int lx = (cx + 1) * partWidth - 1;
            graphics.setColor(((cx + 1) % gamma1s.length) == 0 ? COLOR_LINE1 : COLOR_LINE);
            graphics.drawLine(lx, 0, lx, height - 1);
        }
        for (int cy = 0; cy < countY - 1; cy++) {
            int ly = (cy + 1) * partHeight - 1;
            graphics.setColor(((cy + 1) % gamma2s.length) == 0 ? COLOR_LINE1 : COLOR_LINE);
            graphics.drawLine(0, ly, width - 1, ly);
        }

        for (int g1 = 0; g1 < gamma1s.length; g1++) {
            double gamma1 = gamma1s[g1];
            for (int g2 = 0; g2 < gamma2s.length; g2++) {
                double gamma2 = gamma2s[g2];
                for (int g = 0; g < gammas.length; g++) {
                    double gamma = gammas[g];
                    for (int c = 0; c < costs.length; c++) {
                        double cost = costs[c];

                        int x = (g1 + g * gamma1s.length) * partWidth;
                        int y = (g2 + c * gamma2s.length) * partHeight;

                        addImage(observer, graphics, x, y, partWidth - 1, partHeight - 1, gamma1, gamma2, gamma, cost);
                    }
                }
            }
        }
    }

    private static class FilenameExtensionFilter implements FilenameFilter {

        private String dotExtension;

        private FilenameExtensionFilter(String extension) {
            dotExtension = "." + extension;
        }

        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(dotExtension);
        }

        public String getDotExtension() {
            return dotExtension;
        }

    }

    private String oldDir;

    public void save(JFrame parent, String name) throws IOException {
        FileDialog dialog = new FileDialog(parent, "Save Graphics", FileDialog.SAVE);
        FilenameExtensionFilter filter = new FilenameExtensionFilter("png");
        dialog.setFilenameFilter(filter);
        String dotExtension = filter.getDotExtension();

        if (oldDir != null)
            dialog.setDirectory(oldDir);
        if (name != null)
            dialog.setFile(name);

        dialog.setVisible(true);

        String filename = dialog.getFile();
        String dir = dialog.getDirectory();

        if (filename == null)
            return;

        oldDir = dir;

        File file = new File(dir, filename);

        String path = file.getAbsolutePath();
        if (!path.endsWith(dotExtension))
            file = new File(path + dotExtension);

        if (file.exists()) {
            int overwrite = JOptionPane.showConfirmDialog(parent, filename + " exists.\nOverwrite file?", "File exists", JOptionPane.YES_NO_OPTION);
            if (overwrite == JOptionPane.NO_OPTION)
                return;
        }

        ImageIO.write(image, "png", file);
    }

    public void createFrame(String name, double[] gamma1s, double[] gamma2s, double[] gammas, double[] costs) {
        final JFrame frame = new JFrame("SVMTest: " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        JMenuItem save = new JMenuItem("Save");

        final String defaultFileName = name;
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    save(frame, defaultFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        menu.add(save);
        frame.setJMenuBar(menu);

        Icon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);

        frame.add(label);
        frame.pack();
        frame.setVisible(true);

        addImages(label, gamma1s, gamma2s, gammas, costs);
    }

    private static final int SIZE_X_PART = 400;
    private static final int SIZE_X_MAX = 1000;
    private static final int SIZE_Y_PART = 300;
    private static final int SIZE_Y_MAX = 600;

    private static int[] getImageSize(double[] gamma1s, double[] gamma2s, double[] gammas, double[] costs) {
        int countX = gammas.length * gamma1s.length;
        int countY = costs.length * gamma2s.length;

        int width = SIZE_X_PART * countX;
        int height = SIZE_Y_PART * countY;

        double ratio = Math.min((double) SIZE_X_MAX / width, (double) SIZE_Y_MAX / height);
        if (ratio < 1.0) {
            width = (int) (width * ratio);
            height = (int) (height * ratio);
        }

        return new int[] {width, height};
    }

    public static void ratterTest(String name, double[] gamma1s, double[] gamma2s, double[] gammas, double[] costs) {
        Data data = new RatterData(name);
        DataHandler dataHandler = new DataHandler(new DataScaled(data));
        int[] size = getImageSize(gamma1s, gamma2s, gammas, costs);
        SvmTest test = new SvmTest(dataHandler, size[0], size[1]);

        test.createFrame(name, gamma1s, gamma2s, gammas, costs);
    }

    public static void ratterTest(String name, double gamma1, double gamma2, double gamma, double cost) {
        double[] gamma1s = new double[] {gamma1};
        double[] gamma2s = new double[] {gamma2};
        double[] gammas = new double[] {gamma};
        double[] costs = new double[] {cost};

        ratterTest(name, gamma1s, gamma2s, gammas, costs);
    }


    public static void ratterTest(String name) {
        double[] gamma1s = new double[] {10};
        double[] gamma2s = new double[] {0.25};
        double[] gammas = new double[] {.01, .1, 1, 10, 100};
        double[] costs = new double[] {10};

        ratterTest(name, gamma1s, gamma2s, gammas, costs);
    }

    public static void quasiNormTest(double muX0, double muY0, double sigma0, int count0,
                                     double muX1, double muY1, double sigma1, int count1,
                                     double[] gamma1s, double[] gamma2s, double[] gammas, double[] costs) {
        Data data = new QuasiNormData(muX0, muY0, sigma0, count0, muX1, muY1, sigma1, count1);
        DataHandler dataHandler = new DataHandler(data);
        int[] size = getImageSize(gamma1s, gamma2s, gammas, costs);
        SvmTest test = new SvmTest(dataHandler, size[0], size[1]);

        test.createFrame("quasiNorm", gamma1s, gamma2s, gammas, costs);
    }

    public static void quasiNormTest(double muX0, double muY0, double sigma0, int count0,
                                     double muX1, double muY1, double sigma1, int count1) {
        double[] gamma1s = new double[] {0.01, 1, 100};
        double[] gamma2s = new double[] {0.25, 1, 4};
        double[] gammas = new double[] {10, 100, 1000};
        double[] costs = new double[] {10, 100, 1000};

        quasiNormTest(muX0, muY0, sigma0, count0, muX1, muY1, sigma1, count1, gamma1s, gamma2s, gammas, costs);
    }

    public static void quasiNormTest() {
        quasiNormTest(0.0, 0.0, 1.0, 20, 0.0, 2.0, 1.0, 30);
    }

    public static void main(String[] args) {
        ratterTest("ratterx30");
    }

}
