package de.symate.test.svm.regression;
import de.symate.detact.analysis.data.DataHandler;
import de.symate.detact.analysis.data.convert.DataComplete;
import org.apache.log4j.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 14.06.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class RegressionTest {

    private static Logger log = Logger.getLogger(RegressionTest.class);

    public  static String getFunction(DataHandler data) {
        CostEpsGamma ceg = new CostEpsGamma(10, .001, 100);
        SVMRegJSGenerator jrg = new SVMRegJSGenerator(data, ceg);
        String body = jrg.getJSFunBody();
        return "function(v){return "+body+";}";
    }

    public static Object testFD() {
        List<String> list = new ArrayList<String>(4);
        list.add("Dicke");
        list.add("Druck");
        list.add("HaltZeit");
        list.add("Temp");
        DataHandler data = new DataHandler(new DataComplete(new Dataloader("fd.2.1.0", list)));
        double druck  = 18d;
        double temp = 180d;
        double haltzeit = 2d;
        Object result;

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        try {
            String eval = "var f = " + getFunction(data) + "; f([" + druck + ", " + haltzeit + ", " + temp + "]);";
            result = jsEngine.eval(eval);
        } catch (ScriptException e) {
            throw new AssertionError(e);
        }
        return result;
    }

    public static Object testKaffee() {
        List<String> list = new ArrayList<String>(5);
        list.add("Geschmack");
        list.add("Mahlgrad");
        list.add("Mehlmenge");
        list.add("Nasspressen");
        list.add("Wassermenge");

        DataHandler data = new DataHandler(new DataComplete(new Dataloader("kaffee", list)));
        double mahlgrad = 1d;
        double mehlmenge = 50d;
        double nasspressen = 0d;
        double wassermenge = 30d;
        Object result;

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
        try {
            String eval = "var f = " + getFunction(data) + "; f([" + mahlgrad + ", " + mehlmenge + ", " + nasspressen + ", " + wassermenge + "]);";
            log.debug("eval: " + eval);
            result = jsEngine.eval(eval);
        } catch (ScriptException e) {
            log.error("Script konnte nicht ausgeführt werden", e);
            throw new AssertionError(e);
        }
        return result;

    }

    public static void main(String[] args) {
        System.out.println(testFD());
        System.out.println(testKaffee());
    }
}
