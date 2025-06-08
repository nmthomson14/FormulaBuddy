package com.example.FormulaBuddy;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FormulaProcessor {

    public static final ExprEvaluator EVALUATOR = new ExprEvaluator();

//    private static final Set<String> INVALID_VARIABLES = Set.of(
//            "e", "pi", // Constants
//            "sin", "cos", "tan", "asin", "acos", "atan", // Trig functions
//            "log", "log10", "log2", "ln", "exp", // Logarithms and exponentials
//            "sqrt", "cbrt", // Roots
//            "abs", "ceil", "floor", "round", "signum", // Misc math functions
//            "sinh", "cosh", "tanh", "asinh", "acosh", "atanh", // Hyperbolic functions
//            "min", "max", "pow", "hypot", "random" // Other useful functions
//    );

    // Public methods
    public static FormulaRecord processFormula(String name, String formula) throws IllegalArgumentException {
        String expression = fixShorthandFormatting(formula);
        String latexExpression;
        Set<String> symbols = new HashSet<>();

        // Throw an IllegalArgumentException so calling class can handle it. Essentially validates and assigns in one step
        try {
            IExpr parsedExpression = EVALUATOR.parse(expression);
            extractSymbols(parsedExpression, symbols);
            latexExpression = generateLatexStub(parsedExpression);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }

        return new FormulaRecord(
                UUID.randomUUID().toString(),
                name,
                expression,
                latexExpression,
                symbols
        );
    }

    // Helpers
    private static String fixShorthandFormatting(String formula) {
        // Add multiplication between numbers and variables (e.g., "30y" -> "30*y")
        return formula.replaceAll("(\\d)([a-zA-Z])", "$1 * $2");
    }

    private static void extractSymbols(IExpr expression, Set<String> symbols) {
        // Check if the expression (1st pass) or subexpression is a Symbol
        if (expression instanceof ISymbol symbol) {
            if (!symbol.isBuiltInSymbol()) {
                symbols.add(symbol.toString());
            }
        // If it's not a symbol, see if it is an abstract syntax tree and recursively look through each expression
        } else if (expression.isAST()) {
            IAST ast = (IAST) expression;
            for (int i = 1; i < ast.size(); i++) { // skip index 0 since that is the head a.k.a the operator
                extractSymbols(ast.get(i), symbols);
            }
        }
    }

//    private static Set<String> extractVariables(String formula) {
//        Set<String> variables = new HashSet<>();
//        Pattern pattern = Pattern.compile("\\b[a-zA-Z]\\w*\\b"); // finds all single letter variables
//        Matcher matcher = pattern.matcher(formula);
//
//        while (matcher.find()) {
//            String var = matcher.group();
//            if (!INVALID_VARIABLES.contains(var)) {
//                variables.add(var);
//            }
//        }
//        return variables;
//    }

    public static String generateLatexStub(IExpr parsedExpression) throws IllegalArgumentException {

        StringWriter writer = new StringWriter();
        TeXUtilities texUtil = new TeXUtilities(EVALUATOR.getEvalEngine(), false);
        boolean success = texUtil.toTeX(parsedExpression, writer);

        if (success) {
            return writer.toString();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
