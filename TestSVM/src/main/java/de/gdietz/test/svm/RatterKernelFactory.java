package de.gdietz.test.svm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 30.05.13
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
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
