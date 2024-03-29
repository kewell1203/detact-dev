package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.analysis.data.DataValue;
import libsvm.svm_model;

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

    public SvmModelResult(svm_model model, DataHandler data) {
        this.model = model;
        this.data = data;
        bias = model.rho[0];
        cs = model.sv_coef[0];
        svs = getSVs();
    }

    public double[] getCs() {
        return  cs;
    }

    public double getBias() {
        return bias;
    }

    public int[] getSVindices() {
        int[] sv_indices = new int[model.l];
        for (int i = 0; i < sv_indices.length; i++){
            sv_indices[i] = (int) model.SV[i][0].value;
        }
    return sv_indices;
    }

    public double[][] getSVs() {
        int n = model.l;
        DataValue[][] values = data.independentValues();
        svs = new double[n][values[0].length];
        int[] sv_indices = getSVindices();
        for(int i = 0; i < model.l; i++){
            int l = sv_indices[i];
            for(int j = 0; j < values[0].length; j++){
                svs[i][j] = values[l-1][j].getDouble();
            }
        }
        return svs;
    }

    public double[] getDs() {
        double[] ds = new double[svs.length];
        for(int i = 0; i < svs.length; i++){
            ds[i] = svs[i][0];
        }
        return ds;
    }

    public double[][] getOmegas() {
        int m = svs.length;
        int n = svs[0].length-1;
        double[][] omegas = new double[m][n];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                omegas[i][j] = svs[i][j+1];
            }
        }
        return omegas;
    }
}
