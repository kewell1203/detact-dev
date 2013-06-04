package de.gdietz.test.svm;

public class NoSuchParameterException extends RuntimeException {

    public NoSuchParameterException(String s) {
        super(s);
    }

    public NoSuchParameterException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
