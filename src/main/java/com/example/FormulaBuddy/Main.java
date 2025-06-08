package com.example.FormulaBuddy;

import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.TeXUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.ISymbol;

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        GUIBuilder gui = new GUIBuilder();

        SystemMessageHandler.initializeInstance(
                new MessageReceiver[] { gui }
        );
    }

//    public static void main(String[] args) throws ParseException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter formula name: ");
//        String name = scanner.nextLine();
//        System.out.print("Enter formula: ");
//        String raw = scanner.nextLine();
//
//        FormulaRecord record = FormulaProcessor.processFormula(name, raw);
//        Map<String, Double> variables = new HashMap<String, Double>();
//        for (String variable : record.variables()) {
//            System.out.println("What is the value of  " + variable + ":");
//            variables.put(variable, scanner.nextDouble());
//        }
//
//        FormulaEvaluator evaluator = new FormulaEvaluator();
//        System.out.println(evaluator.evaluate(record.latexExpression(), variables));
//    }

//    public static void main(String[] args) {
//        // 1. Create evaluator and define variables
//        ExprEvaluator evaluator = new ExprEvaluator();
//
//        ISymbol x = F.$s("x");
//        ISymbol y = F.$s("y");
//
//        evaluator.defineVariable(x, F.num(2));
//        evaluator.defineVariable(y, F.num(1));
//
//        // 2. Parse and evaluate expression
//        String expression = "14 * x + sqrt(sin(y^2))";
//        IExpr parsed = evaluator.parse(expression);
//        IExpr result = evaluator.eval(parsed);
//
//
//
//        // 3. Convert to LaTeX
//        StringWriter writer = new StringWriter();
//        TeXUtilities texUtil = new TeXUtilities(evaluator.getEvalEngine(), false);
//        boolean success = texUtil.toTeX(parsed, writer);
//
//        Set<ISymbol> symbols = new HashSet<>();
//        extractSymbols(parsed, symbols);
//
//        for (ISymbol symbol : symbols) {
//            System.out.println(symbol);
//        }
//
//        if (success) {
//            String latex = writer.toString();
//            System.out.println("LaTeX output: " + latex);
//            System.out.println("Evaluated result: " + result);
//
//            // 4. Show popup with LaTeX
//            GUIBuilder.showLatexPopup(latex);
//        } else {
//            System.out.println("Failed to convert to LaTeX.");
//        }
//    }

//    public static void main(String[] args) {
//
//        ExprEvaluator evaluator = new ExprEvaluator();
//
//        ISymbol x = F.$s("x");
//        ISymbol y = F.$s("y");
//
//        evaluator.defineVariable(x, F.num(2));
//        evaluator.defineVariable(y, F.num(1));
//
//        String name = "random expression";
//        String expression = "14 * x + sqrt(y^2)";
//        Integer[] values = {1, 2};
//        FormulaRecord record;
//
//        try {
//            record = FormulaProcessor.processFormula(name, expression);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        IExpr expr = evaluator.parse(record.expression());
//        Set<ISymbol> symbols = record.symbols().stream()
//                .map(F::$s)
//                .collect(Collectors.toSet());
//
//        int count = 0;
//        for (ISymbol element : record.symbols()) {
//            evaluator.defineVariable(element, F.num(values[count++]));
//        }
//
//        IExpr result = evaluator.eval(record.expression());
//        System.out.println(result);
//        GUIBuilder.showLatexPopup(record.latexExpression());
//    }

//    private static void extractSymbols(IExpr expression, Set<ISymbol> symbols) {
//
//        // Check if the expression (1st pass) or subexpression is a Symbol
//        if (expression instanceof ISymbol symbol) {
//            if (!symbol.isBuiltInSymbol()) {
//                symbols.add(symbol);
//            }
//
//        // If it's not a symbol, see if it is an abstract syntax tree and recursively look through each expression
//        } else if (expression.isAST()) {
//            IAST ast = (IAST) expression;
//            for (int i = 1; i < ast.size(); i++) { // skip index 0 since that is the head a.k.a the operator
//                extractSymbols(ast.get(i), symbols);
//            }
//        }
//    }
}
