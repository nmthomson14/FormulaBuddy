package com.example.FormulaBuddy;

import java.util.List;

public class FunctionDictionary {

    public static final List<FunctionRecord> COMMON_FUNCTIONS = List.of(
            new FunctionRecord("sqrt(x)", "sqrt()"),                // square root
            new FunctionRecord("abs(x)", "abs()"),                  // absolute value
            new FunctionRecord("sin(x)", "sin()"),                  // sine
            new FunctionRecord("cos(x)", "cos()"),                  // cosine
            new FunctionRecord("tan(x)", "tan()"),                  // tangent
            new FunctionRecord("asin(x)", "asin()"),                // arcsine
            new FunctionRecord("acos(x)", "acos()"),                // arccosine
            new FunctionRecord("atan(x)", "atan()"),                // arctangent
            new FunctionRecord("log(x)", "log()"),                  // logarithm base 10
            new FunctionRecord("ln(x)", "ln()"),                    // natural logarithm
            new FunctionRecord("exp(x)", "exp()"),                  // exponential
            new FunctionRecord("pow(x,n)", "pow(,)"),               // power
            new FunctionRecord("nthRoot(x,n)", "nthRoot(,)"),       // nth root
            new FunctionRecord("floor(x)", "floor()"),              // floor
            new FunctionRecord("ceil(x)", "ceil()"),                // ceiling
            new FunctionRecord("round(x)", "round()"),              // round
            new FunctionRecord("min(a,b)", "min(,)"),               // minimum
            new FunctionRecord("max(a,b)", "max(,)"),               // maximum
            new FunctionRecord("mod(x,y)", "mod(,)"),               // modulo
            new FunctionRecord("diff(x,y)", "diff(,)"),             // derivative
            new FunctionRecord("integrate(x,dx)", "integrate(,)"),  // integral
            new FunctionRecord("sum(x,y)", "sum(,)"),               // summation
            new FunctionRecord("product(x,y)", "product(,)"),       // product
            new FunctionRecord("limit(dx,dt)", "limit(,)")          // limit
    );
}
