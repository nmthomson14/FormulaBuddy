package com.example.FormulaBuddy;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;

import java.util.HashMap;

public class FormulaEvaluator {

    public static String solveForUnknown(String equation, HashMap<String, Double> values, String unknown) {
        ExprEvaluator evaluator = new ExprEvaluator();

        try {
            // Convert '=' to '==' for Symja Solve
            String expression = equation.replace("=", "==");

            // Define known variables
            for (var entry : values.entrySet()) {
                evaluator.defineVariable(entry.getKey(), F.num(entry.getValue()));
            }

            // Solve for the unknown
            IExpr result = evaluator.eval("Solve(" + expression + ", " + unknown + ")");

            // Check for a non-empty list result
            if (result.isList() && !result.isEmpty()) {
                IExpr firstSolution = result.getAt(1); // First solution in the list

                if (firstSolution.isList()) {
                    for (int i = 1; i < firstSolution.size(); i++) {
                        IExpr rule = firstSolution.getAt(i);

                        if (rule.isRule()) {
                            IExpr lhs = rule.getAt(1); // Variable name
                            IExpr rhs = rule.getAt(2); // Solved value
                            return lhs + " = " + rhs;
                        }
                    }
                }
            }

            return "No solution found.";
        } catch (Exception e) {
            return "Error solving equation: " + e.getMessage();
        }
    }

}
