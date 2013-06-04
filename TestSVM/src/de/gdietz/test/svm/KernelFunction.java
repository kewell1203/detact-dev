package de.gdietz.test.svm;

public interface KernelFunction<K extends KernelParameter> {

    public double kernel(K k, Value[] values1, Value[] values2);

}
