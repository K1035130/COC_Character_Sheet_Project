package com.coc.sheet.service;

public final class DerivedStatCalculator {

    private DerivedStatCalculator() {
    }

    public static int calcHp(int con, int siz) {
        return (con + siz) / 10;
    }

    public static int calcMp(int pow) {
        return pow / 5;
    }

    public static int calcSan(int pow) {
        return Math.min(99, pow * 5);
    }
}
