package de.gdietz.test.svm;

class Value {

    private ColumnType type;

    private double value;

    public Value(ColumnType type, double value) {
        this.type = type;
        this.value = value;
    }

    public Value(String name, double value) {
        this(new ColumnType(name), value);
    }

    public ColumnType getType() {
        return type;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type + "=" + value;
    }

}

