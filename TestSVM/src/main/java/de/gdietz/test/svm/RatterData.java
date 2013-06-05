package de.gdietz.test.svm;

import de.symate.detact.analysis.data.*;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

public class RatterData implements Data {

    private List<DataColumn> data;
    private DataColumnType resultType;
    private List<DataColumnType> independentTypes;

    public RatterData(String name) {
        resultType = new DataColumnTypeOfflineImpl("stable");
        DataColumnType dType = new DataColumnTypeOfflineImpl("d");
        DataColumnType omegaType = new DataColumnTypeOfflineImpl("omega");

        independentTypes = new ArrayList<DataColumnType>();
        independentTypes.add(dType);
        independentTypes.add(omegaType);
//        independentTypes.add(omegaType);
//        independentTypes.add(dType);
        DataColumn result = new DataColumnImpl(resultType);
        DataColumn d = new DataColumnImpl(dType);
        DataColumn omega = new DataColumnImpl(omegaType);

        data = new ArrayList<DataColumn>();
        data.add(result);
        data.add(d);
        data.add(omega);
//        data.add(omega);
//        data.add(d);
        try {
            String fileName = name + ".csv";
            InputStream is = ClassLoader.getSystemResourceAsStream(fileName);

            if (is == null)
                throw new FileNotFoundException("File not found in class path: " + fileName);

            Scanner scanner = new Scanner(new BufferedInputStream(is));

            String line = scanner.nextLine();
            String[] contents = line.split(",");
            if (!contents[1].equals("\"Drehzahl\"") || !contents[2].equals("\"Stegbreite\"") ||
                    !contents[3].equals("\"Stabil\""))
                throw new NoSuchParameterException("Wrong Header Line");

            while (scanner.hasNext()) {
                line = scanner.nextLine();
                contents = line.split(",");
                try {
                    String omegaString = contents[1];
                    String dString = contents[2];
                    String stableString = contents[3];

                    double omegaValue = Double.parseDouble(omegaString);
                    double dValue = Double.parseDouble(dString);
                    boolean stableValue = Boolean.parseBoolean(stableString);

                    result.add(stableValue ? 1.0 : -1.0);
                    omega.add(omegaValue);
                    d.add(dValue);
                } catch (NumberFormatException e) {
                    throw new NoSuchParameterException("Wrong content", e);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new NoSuchParameterException("Wrong content", e);
                }
            }
        } catch (IOException e) {
            throw new NoSuchParameterException("Could not load ratter data", e);
        }
    }

    public List<DataColumn> getData() {
        return data;
    }

    public DataColumnType getResultType() {
        return resultType;
    }

    public List<DataColumnType> getIndependentTypes() {
        return independentTypes;
    }

    @Override
    public List<DataColumnType> getDependentTypes() {
        List<DataColumnType> result = new ArrayList<DataColumnType>(1);
        result.add(resultType);
        return result;
    }

    @Override
    public List<DataColumnType> getControllableTypes() {
        return new ArrayList<DataColumnType>(0);
    }

    @Override
    public List<DataColumnType> getConditionTypes() {
        return new ArrayList<DataColumnType>(0);
    }

    @Override
    public List<DataColumnType> getGroupingTypes() {
        return new ArrayList<DataColumnType>(0);
    }

}
