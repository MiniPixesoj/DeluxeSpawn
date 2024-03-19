package com.pixesoj.utils.common;

public enum JavaVersion {
    JAVA_1_8,
    JAVA_1_9,
    JAVA_10,
    JAVA_11,
    JAVA_12,
    JAVA_13,
    JAVA_14,
    JAVA_15,
    JAVA_16,
    JAVA_17,
    JAVA_18,
    JAVA_19,
    JAVA_20,
    UNKNOWN;

    public static JavaVersion getCurrentJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.8")) {
            return JAVA_1_8;
        } else if (version.startsWith("1.9")) {
            return JAVA_1_9;
        } else if (version.startsWith("10")) {
            return JAVA_10;
        } else if (version.startsWith("11")) {
            return JAVA_11;
        } else if (version.startsWith("12")) {
            return JAVA_12;
        } else if (version.startsWith("13")) {
            return JAVA_13;
        } else if (version.startsWith("14")) {
            return JAVA_14;
        } else if (version.startsWith("15")) {
            return JAVA_15;
        } else if (version.startsWith("16")) {
            return JAVA_16;
        } else if (version.startsWith("17")) {
            return JAVA_17;
        } else if (version.startsWith("18")) {
            return JAVA_18;
        } else if (version.startsWith("19")) {
            return JAVA_19;
        } else if (version.startsWith("20")) {
            return JAVA_20;
        } else {
            return UNKNOWN;
        }
    }

    public static int getCurrentJavaVersionNo() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.8")) {
            return 8;
        } else if (version.startsWith("1.9")) {
            return 9;
        } else if (version.startsWith("10")) {
            return 10;
        } else if (version.startsWith("11")) {
            return 11;
        } else if (version.startsWith("12")) {
            return 12;
        } else if (version.startsWith("13")) {
            return 13;
        } else if (version.startsWith("14")) {
            return 14;
        } else if (version.startsWith("15")) {
            return 15;
        } else if (version.startsWith("16")) {
            return 16;
        } else if (version.startsWith("17")) {
            return 17;
        } else if (version.startsWith("18")) {
            return 18;
        } else if (version.startsWith("19")) {
            return 19;
        } else if (version.startsWith("20")) {
            return 20;
        } else {
            return 0;
        }
    }
}