package com.example.FormulaBuddy;

import org.matheclipse.core.interfaces.IExpr;

import java.util.*;

public class FileHandler {
    private static final List<FormulaRecord> COMMON_FORMULAS = List.of(
            new FormulaRecord(
                    "Newton's Second Law",
                    "F = m * a",
                    Set.of("F", "m", "a")
            ),
            new FormulaRecord(
                    "Kinetic Energy",
                    "KE = (1/2) * m * v^2",
                    Set.of("KE", "m", "v")
            ),
            new FormulaRecord(
                    "Potential Energy",
                    "PE = m * g * h",
                    Set.of("PE", "m", "g", "h")
            ),
            new FormulaRecord(
                    "Ohm's Law",
                    "V = I * R",
                    Set.of("V", "I", "R")
            ),
            new FormulaRecord(
                    "Ideal Gas Law",
                    "P * V = n * R * T",
                    Set.of("P", "V", "n", "R", "T")
            ),
            new FormulaRecord(
                    "Coulomb's Law",
                    "F = k * (q1 * q2) / r^2",
                    Set.of("F", "k", "q1", "q2", "r")
            ),
            new FormulaRecord(
                    "Einstein's Mass-Energy",
                    "E = m * c^2",
                    Set.of("E", "m", "c")
            ),
            new FormulaRecord(
                    "Gravitational Force",
                    "F = G * (m1 * m2) / r^2",
                    Set.of("F", "G", "m1", "m2", "r")
            ),
            new FormulaRecord(
                    "First Kinematic Equation",
                    "v = u + a * t",
                    Set.of("v", "u", "a", "t")
            ),
            new FormulaRecord(
                    "Second Kinematic Equation",
                    "s = u * t + (1/2) * a * t^2",
                    Set.of("s", "u", "t", "a")
            ),
            new FormulaRecord(
                    "Work Done",
                    "W = F * d * cos(theta)",
                    Set.of("W", "F", "d", "theta")
            ),
            new FormulaRecord(
                    "Hooke's Law",
                    "F = -k * x",
                    Set.of("F", "k", "x")
            ),
            new FormulaRecord(
                    "Simple Harmonic Motion (Displacement)",
                    "x = A * cos(omega * t + phi)",
                    Set.of("x", "A", "omega", "t", "phi")
            ),
            new FormulaRecord(
                    "Capacitance",
                    "C = Q / V",
                    Set.of("C", "Q", "V")
            ),
            new FormulaRecord(
                    "Snell's Law",
                    "n1 * sin(theta1) = n2 * sin(theta2)",
                    Set.of("n1", "theta1", "n2", "theta2")
            ),
            new FormulaRecord(
                    "Centripetal Force",
                    "Fc = m * v^2 / r",
                    Set.of("Fc", "m", "v", "r")
            ),
            new FormulaRecord(
                    "Wave Speed",
                    "v = f * lambda",
                    Set.of("v", "f", "lambda")
            ),
            new FormulaRecord(
                    "Planck's Equation",
                    "E = h * f",
                    Set.of("E", "h", "f")
            )
    );

    private static final String FORMULA_RECORDS_NAME = "formula_records.ser";
    private static final RecordFile<FormulaRecord> formulaRecordsFile = new RecordFile<>(FORMULA_RECORDS_NAME, COMMON_FORMULAS);
    private static List<FormulaRecord> formulaRecords;

    public static void main(String[] args) {
        formulaRecords = getFormulaRecords();
        for (FormulaRecord formulaRecord : formulaRecords) {
            IExpr parsedExpression = FormulaProcessor.EVALUATOR.parse(formulaRecord.expression());
            System.out.println(FormulaProcessor.generateLatexStub(parsedExpression));
        }
    }

    public static List<FormulaRecord> getFormulaRecords() {
        if (formulaRecords == null) {
            formulaRecords = new ArrayList<>(loadRecords(formulaRecordsFile));
        }
        return formulaRecords;
    }

    public static boolean saveRecords(RecordFile recordFile) {
        return recordFile.save();
    }

    private static <T> List<T> loadRecords(RecordFile recordFile) {
        return new ArrayList<>(recordFile.getRecords());
    }
}
