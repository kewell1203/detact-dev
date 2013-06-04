package de.gdietz.test.svm;

import libsvm.svm_model;
/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 30.05.13
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public interface RatterKernel extends KernelFunction<RatterKernelParameter> {
    public double kernel(RatterKernelParameter p, Value[] values1, Value[] values2);
    public double getDecision(RatterKernelParameter p, svm_model model, DataHandler data, double[] omegas);
}
