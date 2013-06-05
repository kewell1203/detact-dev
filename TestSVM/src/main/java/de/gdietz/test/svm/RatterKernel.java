package de.gdietz.test.svm;

import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.analysis.data.DataValue;
import de.symate.detact.analysis.svm.KernelFunction;
import libsvm.svm_model;

/**
 * User: Eduard
 */
public interface RatterKernel extends KernelFunction<RatterKernelParameter> {

    public double kernel(RatterKernelParameter p, DataValue[] values1, DataValue[] values2);

    public double getDecision(RatterKernelParameter p, svm_model model, DataHandler data, double[] omegas);

}
