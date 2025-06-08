package com.example.FormulaBuddy;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import java.io.Serializable;
import java.util.Set;

public record FormulaRecord(
        String id,
        String name,
        String expression,
        String latexExpression,
        Set<String> symbols
) implements Serializable {}
