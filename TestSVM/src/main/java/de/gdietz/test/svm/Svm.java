package de.gdietz.test.svm;

import org.encog.mathutil.libsvm.svm_node;
import org.encog.mathutil.libsvm.svm_parameter;
import org.encog.mathutil.libsvm.svm_problem;

public interface Svm<K extends KernelParameter, P extends SvmParameter> {

    public Data getData();

    public svm_problem createProblem(K k);

    public svm_node[] createNode(K k, Value... independentValues);

    public svm_parameter createParameter(K k, P p);

}
