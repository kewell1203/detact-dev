package de.gdietz.test.svm;

import org.encog.mathutil.libsvm.svm_model;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 */
public class SvmModelResult {
    svm_model model;
    DataHandler data;
    double[] cs;
    double bias;
    double[][] svs;
    double[] ds;
    double[][] omegas;

    public SvmModelResult(svm_model model, DataHandler data) {
        this.model = model;
        this.data = data;
        bias = model.rho[0];
        cs = model.sv_coef[0];
        svs = getSVs();
        ds = getDs();
        omegas = getOmegas();
    }

    public double[] getCs() {
        return  cs;
    }

    public double getBias() {
        return bias;
    }

    public double[][] getSVs() {
        int n = model.l;
        Value[][] values = data.independentValues();
        svs = new double[n][values[0].length];
        int[] sv_indices = model.sv_indices;
        for(int i = 0; i < model.l; i++){
            int l = sv_indices[i];
            for(int j = 0; j < values[0].length; j++){
                svs[i][j] = values[l-1][j].getValue();
            }
        }
        return svs;
    }

    public double[] getDs() {
        ds = new double[svs.length];
        for(int i = 0; i < svs.length; i++){
            ds[i] = svs[i][0];
        }
        return ds;
    }

    public double[][] getOmegas() {
        int m = svs.length;
        int n = svs[0].length-1;
        omegas = new double[m][n];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                omegas[i][j] = svs[i][j+1];
            }
        }
        return omegas;
    }
}
