package de.gdietz.test.svm;

import java.lang.reflect.Constructor;

/**
 * User: Eduard
 */
public class RatterKernelFactory {
    private Class[] types = {};
    public static  RatterKernel getKernel(Class<? extends RatterKernel> name) {
        try {
            Constructor<? extends RatterKernel> con = name.getConstructor();
            RatterKernel ratterKernel = (RatterKernel) con.newInstance();
            return ratterKernel;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    };

}
