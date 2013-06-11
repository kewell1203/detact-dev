package de.gdietz.test.svm;

import de.symate.detact.analysis.data.*;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RatterData implements Data {

    public static final DataColumnType stableType = new DataColumnTypeOfflineImpl("stable");

    public static final DataColumnType omegaType = new DataColumnTypeOfflineImpl("omega");
    public static final DataColumnType dType = new DataColumnTypeOfflineImpl("d");

    private List<DataColumn> data;

    private List<DataColumnType> dependentTypes;
    private List<DataColumnType> independentTypes;

    public RatterData(String name) {
        dependentTypes = new ArrayList<DataColumnType>(1);
        dependentTypes.add(stableType);
        independentTypes = new ArrayList<DataColumnType>(2);
        independentTypes.add(dType);
        independentTypes.add(omegaType);

        DataColumn stable = new DataColumnImpl(stableType);
        DataColumn d = new DataColumnImpl(dType);
        DataColumn omega = new DataColumnImpl(omegaType);

        data = new ArrayList<DataColumn>();
        data.add(stable);
        data.add(d);
        data.add(omega);

        try {
            String fileName = name + ".csv";
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);

            if (is == null)
                throw new FileNotFoundException("File not found in class path: " + fileName);

            Scanner scanner = new Scanner(new BufferedInputStream(is));

            String line = scanner.nextLine();
            String[] contents = line.split(",");
            if (contents.length != 4 ||
                    !contents[1].equals("\"Drehzahl\"") || !contents[2].equals("\"Stegbreite\"") ||
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

                    stable.add(stableValue ? 1.0 : -1.0);
                    d.add(dValue);
                    omega.add(omegaValue);
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