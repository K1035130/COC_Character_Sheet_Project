package com.coc.sheet.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DerivedStatCalculatorTest {

    @Test
    void calcHpTruncatesFractionalPart() {
        assertEquals(9, DerivedStatCalculator.calcHp(43, 50));
        assertEquals(0, DerivedStatCalculator.calcHp(0, 0));
    }

    @Test
    void calcMpTruncatesFractionalPart() {
        assertEquals(10, DerivedStatCalculator.calcMp(50));
        assertEquals(0, DerivedStatCalculator.calcMp(4));
    }

    @Test
    void calcSanCapsAt99() {
        assertEquals(99, DerivedStatCalculator.calcSan(50));
        assertEquals(85, DerivedStatCalculator.calcSan(17));
        assertEquals(0, DerivedStatCalculator.calcSan(0));
    }
}
