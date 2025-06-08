package com.example.FormulaBuddy;

import org.matheclipse.core.interfaces.IExpr;

import java.util.Map;

public class FormulaEvaluator {

    //private final ExprEvaluator exprEvaluator;
    private String lastParsedExpression;
    private IExpr parsedExpression;

    public FormulaEvaluator() {
        // Initialize Symja evaluator with built-in functions/constants
        //exprEvaluator = new ExprEvaluator(false, 100);
    }

    public void evaluate(FormulaRecord formulaRecord) {

    }

    public void parseFormula(String expression) {
        lastParsedExpression = expression;
        //parsedExpression = exprEvaluator.parse(expression);
    }

//    public double evaluate(String expression, Map<String, Double> variableValues) {
//        // Parse if needed
//        if (parsedExpression == null || !expression.equals(lastParsedExpression)) {
//            parseFormula(expression);
//        }
//
//        // Set variables as rules
//        for (Map.Entry<String, Double> entry : variableValues.entrySet()) {
//            exprEvaluator.defineVariable(entry.getKey(), F.num(entry.getValue()));
//        }
//
//        // Evaluate expression
//        IExpr result = exprEvaluator.evaluate(parsedExpression);
//
//        if (result.isReal() || result.isInteger() || result.isNumeric()) {
//            return result.evalDouble();
//        } else {
//            throw new IllegalStateException("Evaluation did not return a numeric result");
//        }
//    }

//    public String toLatex(String expression) {
//        IExpr expr = exprEvaluator.parse(expression);
//        return toLatex();
//    }
}
