package com.example.FormulaBuddy;

import java.io.Serializable;

public record FormulaRecord(
        String name,
        String expression,
        String[] symbols,
        String[] tags // describes the formula for easier search
) implements Serializable {}
