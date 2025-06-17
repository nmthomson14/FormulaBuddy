package com.example.FormulaBuddy;

import java.io.Serializable;
import java.util.Set;

public record FormulaRecord(
        String name,
        String expression,
        Set<String> symbols
) implements Serializable {}
