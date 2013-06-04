package de.gdietz.test.svm;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

public class RatterData implements Data {

    private List<Column> data;
    private ColumnType resultType;
    private List<ColumnType> independentTypes;

    public RatterData(String name) {
        resultType = new ColumnType("stable");
        ColumnType dType = new ColumnType("d");
        ColumnType omegaType = new ColumnType("omega");

        independentTypes = new ArrayList<ColumnType>();
        independentTypes.add(dType);
        independentTypes.add(omegaType);
//        independentTypes.add(omegaType);
//        independentTypes.add(dType);
        Column result = new Column(resultType);
        Column d = new Column(dType);
        Column omega = new Column(omegaType);

        data = new ArrayList<Column>();
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

    public List<Column> getData() {
        return data;
    }

    public ColumnType getResultType() {
        return resultType;
    }

    public List<ColumnType> getIndependentTypes() {
        return independentTypes;
    }

}
