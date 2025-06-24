package com.example.FormulaBuddy;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.HashMap;

public class FormulaEvaluator {

    public static String solveForUnknown(String equation, HashMap<String, Double> values, String unknown) {
        ExprEvaluator evaluator = new ExprEvaluator();

        try {
            // Convert = to == for Symja Solve
            String expression = equation.replace("=", "==");

            // Define known variables
            for (var entry : values.entrySet()) {
                evaluator.defineVariable(entry.getKey(), F.num(entry.getValue()));
            }

            // Solve for the unknown
            IExpr result = evaluator.eval("Solve(" + expression + ", " + unknown + ")");

            // Expecting result like: {{y -> 76.8}}
            if (result.isList() && !result.isEmpty() && result.getAt(1).isList()) {
                IExpr rule = result.getAt(1).getAt(1); // Extract y -> 76.8

                if (rule.isAST() && rule.size() == 3 && rule.getAt(0).toString().equals("Rule")) {
                    IExpr value = rule.getAt(2); // Get the value (RHS)
                    return unknown + " = " + value.toString();
                }
            }

            return "No solution found.";
        } catch (Exception e) {
            return "Error solving equation: " + e.getMessage();
        }
    }

    public static IExpr evaluate(FormulaRecord formulaRecord, HashMap<String, String> variables) {
        ExprEvaluator evaluator = new ExprEvaluator();
        IExpr parsedExpression = evaluator.parse(formulaRecord.expression());
        for (var entry : variables.entrySet()) {
            if (formulaRecord.symbols().contains(entry.getKey())) {
                IExpr varValue = evaluator.parse(entry.getValue());
                evaluator.defineVariable(entry.getKey(), varValue);
            }
        }
        return evaluator.eval(parsedExpression);
    }

    // TODO: CREATE A METHOD THAT EVALUATES EACH STEP OF A FORMULA, RETURNING AN IEXPRESSION THAT CAN BE RENDERED AS LATEX
    // TODO: COULD ALSO RETURN STRINGS IF THATS EASIER
    public static IExpr[] evaluateSteps() {
        return null;
    }
}
