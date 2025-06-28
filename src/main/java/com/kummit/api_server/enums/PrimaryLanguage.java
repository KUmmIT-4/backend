package com.kummit.api_server.enums;

public enum PrimaryLanguage {
    C("C"),
    CPP("C++"),
    PYTHON("Python"),
    JAVA("Java"),
    JAVASCRIPT("JavaScript");

    private final String dbValue;
    PrimaryLanguage(String dbValue) { this.dbValue = dbValue; }
    @Override public String toString() { return dbValue; }
}
