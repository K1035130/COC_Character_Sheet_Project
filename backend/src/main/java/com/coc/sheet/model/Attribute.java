package com.coc.sheet.model;

import java.util.EnumSet;
import java.util.Set;

public enum Attribute {
    STR, DEX, CON, APP, POW, SIZ, INT, EDU, LUC,
    HP, MP, SAN;

    private static final Set<Attribute> DERIVED = EnumSet.of(HP, MP, SAN);

    public boolean isDerived() {
        return DERIVED.contains(this);
    }
}
