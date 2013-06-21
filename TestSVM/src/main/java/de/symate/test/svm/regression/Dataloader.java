package de.symate.test.svm.regression;

import de.gdietz.test.svm.DataColumnTypeOfflineImpl;
import de.symate.detact.analysis.data.Data;
import de.symate.detact.analysis.data.DataColumn;
import de.symate.detact.analysis.data.DataColumnImpl;
import de.symate.detact.analysis.data.DataColumnType;
import org.apache.commons.logging.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Eduard
 * Date: 14.06.13
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
public class Dataloader implements Data{
    private List<DataColumn> data;
    private List<DataColumnType> dependentTypes;
    private List<DataColumnType> independentTypes;
    public static Log log;

    public Dataloader(String fileName, List<String> columnNames) {

        DataColumnType dependent   = new DataColumnTypeOfflineImpl(columnNames.get(0));
        dependentTypes = new ArrayList<DataColumnType>(1);
        dependentTypes.add(dependent);

        data = new ArrayList<DataColumn>(columnNames.size());
        data.add(new DataColumnImpl(dependent));

        List<String> independentNames = columnNames.subList(1, columnNames.size());
        independentTypes = new ArrayList<DataColumnType>(independentNames.size());
        for(String independentName : independentNames) {
           DataColumnType dct = new DataColumnTypeOfflineImpl(independentName);
           independentTypes.add(dct);
           data.add(new DataColumnImpl(dct));
        }

        String line;
        String s ;
        int j = 0;
        int idx ;
        int[] relIdx = new int[columnNames.size()];
        try {
            BufferedInputStream is = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(fileName + ".csv"));
            BufferedReader bs = new BufferedReader(new InputStreamReader(is));
            line = bs.readLine() ;
            String[] splitStr = line.split(",");

            for(String column : columnNames){
                for(int i = 0; i < splitStr.length; i++) {
                    s = splitStr[i].replaceAll("\"","");
                    if(column.equals(s)) {
                        relIdx[j] = i;
                        j++;
                        break;
                    }
                    if(i == splitStr.length-1)
                        log.error("Parameter not matched!");
                }
            }
            while ((line = bs.readLine()) != null) {
                splitStr = line.split(",");
                for (int i = 0; i < relIdx.length; i++) {
                    idx = relIdx[i];
                    if (splitStr[idx].equals("NA"))
                        data.get(i).add(Double.parseDouble("NaN"));
                    else
                        data.get(i).add(Double.parseDouble(splitStr[idx]));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("there is no file named" + fileName );
        } catch (IOException e){
            System.out.println("IOException.");
        }
    }

    @Override
    public List<DataColumn> getData() {
        return data;
    }

    @Override
    public List<DataColumnType> getDependentTypes() {
        return dependentTypes;
    }

    @Override
    public List<DataColumnType> getIndependentTypes() {
        return independentTypes;
    }

    @Override
    public List<DataColumnType> getControllableTypes() {
        return independentTypes;
    }

    private final List<DataColumnType> EMPTY_TYPE_LIST = new ArrayList<DataColumnType>();

    @Override
    public List<DataColumnType> getConditionTypes() {
        return EMPTY_TYPE_LIST;
    }

    @Override
    public List<DataColumnType> getGroupingTypes() {
        return EMPTY_TYPE_LIST;
    }

}
