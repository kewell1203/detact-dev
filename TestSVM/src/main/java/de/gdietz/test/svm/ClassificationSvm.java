package de.gdietz.test.svm;


import org.encog.mathutil.libsvm.svm_node;
import org.encog.mathutil.libsvm.svm_parameter;
import org.encog.mathutil.libsvm.svm_problem;

import java.util.List;

public class ClassificationSvm implements Svm<GammaParameter, CostParameter> {

    private DataHandler data;

    public ClassificationSvm(DataHandler data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public svm_problem createProblem(GammaParameter k) {
        Column dependent = data.getResult();
        List<Column> independents = data.getIndependents();

		int size = data.size();
        int psize = data.psize();

        svm_problem prob = new svm_problem();

        prob.l = size;
        prob.y = new double[size];
		prob.x = new svm_node[size][psize];
		for (int i = 0; i < size; i++) {
            prob.y[i] = dependent.get(i);
            for (int j = 0; j < psize; j++) {
                prob.x[i][j] = new svm_node();
                prob.x[i][j].index = j;
                prob.x[i][j].value = independents.get(j).get(i);
            }
		}

        return prob;
    }

    public svm_node[] createNode(GammaParameter k, Value... independentValues) {
        Value[] independents = data.independentValues(independentValues);

        int psize = data.psize();

        svm_node[] x = new svm_node[psize];

        for (int j = 0; j < psize; j++) {
            x[j] = new svm_node();
            x[j].index = j;
            x[j].value = independents[j].getValue();
        }

        return x;
    }

    public svm_parameter createParameter(GammaParameter k, CostParameter p) {
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;
        param.degree = 3;
        param.gamma = k.getGamma();
        param.coef0 = 0;
        param.nu = 0.5;
        param.cache_size = 40;
        param.C = p.getCost();
        param.eps = 1e-3;
        param.p = 0.1;
        param.shrinking = 1;
        param.probability = 0;
        param.nr_weight = 0;
        param.weight_label = new int[0];
        param.weight = new double[0];

        return param;
    }

}
