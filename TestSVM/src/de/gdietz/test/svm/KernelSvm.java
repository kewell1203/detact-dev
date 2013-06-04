package de.gdietz.test.svm;

import libsvm.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class KernelSvm<K extends KernelParameter> implements Svm<K, CostParameter> {

    private DataHandler data;
    private KernelFunction<K> kernel;

    public KernelSvm(DataHandler data, KernelFunction<K> kernel) {
        this.data = data;
        this.kernel = kernel;
    }

    public Data getData() {
        return data;
    }

    public svm_problem createProblem(K k) {
        Column dependent = data.getResult();
        Value[][] values = data.independentValues();

        int size = dependent.size();

        svm_problem prob = new svm_problem();

        prob.l = size;
        prob.y = new double[size];
		prob.x = new svm_node[size][size + 1];
		for (int i = 0; i < size; i++) {
            prob.y[i] = dependent.get(i);
            prob.x[i][0] = new svm_node();
            prob.x[i][0].index = 0;
            prob.x[i][0].value = i + 1;
            for (int j = 0; j < size; j++) {
                prob.x[i][j + 1] = new svm_node();
                prob.x[i][j + 1].index = j + 1;
                prob.x[i][j + 1].value = kernel.kernel(k, values[i], values[j]);
            }
		}

        return prob;
    }

    public svm_node[] createNode(K k, Value... independentValues) {
        Value[] independents = data.independentValues(independentValues);
        Value[][] values = data.independentValues();

        int size = data.size();

        svm_node[] x = new svm_node[size + 1];

        x[0] = new svm_node();
        x[0].index = 0;
        for (int j = 0; j < size; j++) {
            x[j + 1] = new svm_node();
            x[j + 1].index = j + 1;
            x[j + 1].value = kernel.kernel(k, independents, values[j]);
        }
        return x;
    }

    public svm_parameter createParameter(K k, CostParameter p) {
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.PRECOMPUTED;
        param.degree = 3;
        param.gamma = 0;
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
