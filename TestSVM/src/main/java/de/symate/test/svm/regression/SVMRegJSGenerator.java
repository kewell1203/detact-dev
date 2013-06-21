package de.symate.test.svm.regression;
import de.gdietz.test.svm.SvmModelResult;
import de.symate.detact.analysis.data.DataHandler;
import libsvm.*;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 18.06.13
 * Time: 16:26
 * To change this template use File | Settings | File Templates.
 */
public class SVMRegJSGenerator {
    private DataHandler data;
    private svm_model model;
    public SVMRegJSGenerator(DataHandler data, CostEpsGamma ceg) {
        this.data = data;

        svm_parameter param =  new svm_parameter();
        param.svm_type = svm_parameter.EPSILON_SVR;
        param.kernel_type = svm_parameter.RBF;
        param.gamma = ceg.getGamma();
        param.nu = 0.5;
        param.C = ceg.getCost();
        param.cache_size = 500;
        param.eps = ceg.getEps();

        svm_problem prob = new svm_problem();
        int dataSize = data.size();
        prob.y = new double[dataSize];
        prob.l = dataSize;
        prob.x = new svm_node[dataSize][];

        for (int i = 0; i < dataSize; i++) {
            prob.x[i] = new svm_node[data.psize()];
            for (int j = 0; j < data.psize(); j++){
                svm_node node = new svm_node();
                node.index = j + 1 ;
                node.value = data.independentValues()[i][j].getDouble();
                prob.x[i][j] = node;
            }
            prob.y[i] = data.resultValue(i).getDouble();
        }


        this.model = svm.svm_train(prob, param);
    }


    public String getJSFunBody(){
        int n = data.psize();
        int l = model.l;
        SvmModelResult smr = new SvmModelResult(model, data);
        double bias = smr.getBias();
        double[] cs = smr.getCs();
        double[][] svs = smr.getSVs();
        double gamma = model.param.gamma;
        String s = "";
        String el ;
        for (int i = 0; i < l; i++) {
            el = "";
            for (int j = 0; j < n-1; j++){
                el += "Math.pow((" + svs[i][j] + " - v[" + j +  "]), 2) + ";
            }
            el += "Math.pow((" + svs[i][n-1] + " - v[" + (n-1) + "]), 2)";
            s += cs[i] + " * Math.exp( (" + el + ") * -" + gamma + ") + ";
        }
        s = s  + "- " + bias;
        return s;
    }
}
