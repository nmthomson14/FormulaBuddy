package com.example.FormulaBuddy;

import java.util.*;

public class FileHandler {
    private static final FormulaRecord[] COMMON_FORMULAS = new FormulaRecord[]{
            new FormulaRecord(
                    "Newton's Second Law",
                    "F = m * a",
                    new String[]{"F", "m", "a"},
                    new String[]{"physics", "mechanics", "force", "acceleration", "dynamics"}
            ),
            new FormulaRecord(
                    "Kinetic Energy",
                    "KE = (1/2) * m * v^2",
                    new String[]{"KE", "m", "v"},
                    new String[]{"physics", "energy", "motion", "mechanics", "kinetic"}
            ),
            new FormulaRecord(
                    "Potential Energy",
                    "PE = m * g * h",
                    new String[]{"PE", "m", "g", "h"},
                    new String[]{"physics", "energy", "gravity", "mechanics", "potential"}
            ),
            new FormulaRecord(
                    "Ohm's Law",
                    "V = I * R",
                    new String[]{"V", "I", "R"},
                    new String[]{"physics", "circuits", "voltage", "current", "resistance"}
            ),
            new FormulaRecord(
                    "Ideal Gas Law",
                    "P * V = n * R * T",
                    new String[]{"P", "V", "n", "R", "T"},
                    new String[]{"chemistry", "thermodynamics", "pressure", "volume", "gas", "temperature"}
            ),
            new FormulaRecord(
                    "Coulomb's Law",
                    "F = k * (q1 * q2) / r^2",
                    new String[]{"F", "k", "q1", "q2", "r"},
                    new String[]{"physics", "electricity", "electrostatics", "force", "charge"}
            ),
            new FormulaRecord(
                    "Einstein's Mass-Energy",
                    "energy = m * c^2",
                    new String[]{"energy", "m", "c"},
                    new String[]{"physics", "energy", "relativity", "mass", "conversion"}
            ),
            new FormulaRecord(
                    "Gravitational Force",
                    "F = G * (m1 * m2) / r^2",
                    new String[]{"F", "G", "m1", "m2", "r"},
                    new String[]{"physics", "gravity", "mechanics", "force", "mass"}
            ),
            new FormulaRecord(
                    "First Kinematic Equation",
                    "v = u + a * t",
                    new String[]{"v", "u", "a", "t"},
                    new String[]{"physics", "kinematics", "motion", "velocity", "acceleration"}
            ),
            new FormulaRecord(
                    "Second Kinematic Equation",
                    "s = u * t + (1/2) * a * t^2",
                    new String[]{"s", "u", "t", "a"},
                    new String[]{"physics", "kinematics", "displacement", "acceleration", "time"}
            ),
            new FormulaRecord(
                    "Work Done",
                    "W = F * d * cos(theta)",
                    new String[]{"W", "F", "d", "theta"},
                    new String[]{"physics", "work", "mechanics", "angle", "force"}
            ),
            new FormulaRecord(
                    "Hooke's Law",
                    "F = -k * x",
                    new String[]{"F", "k", "x"},
                    new String[]{"physics", "mechanics", "spring", "force", "elasticity"}
            ),
            new FormulaRecord(
                    "Simple Harmonic Motion (Displacement)",
                    "x = A * cos(omega * t + phi)",
                    new String[]{"x", "A", "omega", "t", "phi"},
                    new String[]{"physics", "vibrations", "waves", "oscillation", "displacement"}
            ),
            new FormulaRecord(
                    "Capacitance",
                    "C = Q / V",
                    new String[]{"C", "Q", "V"},
                    new String[]{"physics", "circuits", "capacitance", "charge", "voltage"}
            ),
            new FormulaRecord(
                    "Snell's Law",
                    "n1 * sin(theta1) = n2 * sin(theta2)",
                    new String[]{"n1", "theta1", "n2", "theta2"},
                    new String[]{"physics", "optics", "refraction", "waves", "angles"}
            ),
            new FormulaRecord(
                    "Centripetal Force",
                    "Fc = m * v^2 / r",
                    new String[]{"Fc", "m", "v", "r"},
                    new String[]{"physics", "circular motion", "force", "velocity", "radius"}
            ),
            new FormulaRecord(
                    "Wave Speed",
                    "v = f * lambda",
                    new String[]{"v", "f", "lambda"},
                    new String[]{"physics", "waves", "speed", "frequency", "wavelength"}
            ),
            new FormulaRecord(
                    "Planck's Equation",
                    "Ephoton = h * f",
                    new String[]{"Ephoton", "h", "f"},
                    new String[]{"physics", "quantum", "energy", "frequency", "photons"}
            )};


    private static final String FORMULA_RECORDS_NAME = "formula_records.ser";
    private static final RecordFile<FormulaRecord> formulaRecordsFile = new RecordFile<>(FORMULA_RECORDS_NAME, COMMON_FORMULAS);

    // Formula-specific methods
    public static List<FormulaRecord> getFormulaRecords() {
        return formulaRecordsFile.getRecords();
    }

    public static boolean resetFormulaRecords() {
        return formulaRecordsFile.resetRecords();
    }

    public static boolean deleteFormulaRecord(FormulaRecord recordToDelete) {
        return formulaRecordsFile.deleteRecord(recordToDelete);
    }

    public static boolean addFormulaRecord(FormulaRecord newRecord) {
        return formulaRecordsFile.addRecord(newRecord);
    }

    public static boolean replaceFormulaRecord(FormulaRecord oldRecord, FormulaRecord newRecord) {
        return formulaRecordsFile.replaceRecord(oldRecord, newRecord);
    }
}
