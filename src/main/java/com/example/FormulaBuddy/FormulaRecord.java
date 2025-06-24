package com.example.FormulaBuddy;

import java.io.Serializable;
import java.util.List;

public record FormulaRecord(
        String name,
        String expression,
        List<String> symbols,
        List<String> tags // describes the formula for easier search
) implements Serializable {}
