package com.example.FormulaBuddy;

import java.util.List;

public class FunctionDictionary {

    public static final List<FunctionRecord> COMMON_FUNCTIONS = List.of(
            new FunctionRecord("sqrt(x)"),          // square root
            new FunctionRecord("abs(x)"),           // absolute value
            new FunctionRecord("sin(x)"),           // sine
            new FunctionRecord("cos(x)"),           // cosine
            new FunctionRecord("tan(x)"),           // tangent
            new FunctionRecord("asin(x)"),          // arcsine
            new FunctionRecord("acos(x)"),          // arccosine
            new FunctionRecord("atan(x)"),          // arctangent
            new FunctionRecord("log(x)"),           // logarithm base 10
            new FunctionRecord("ln(x)"),            // natural logarithm
            new FunctionRecord("exp(x)"),           // exponential
            new FunctionRecord("pow(x,n)"),         // power
            new FunctionRecord("nthRoot(x,n)"),     // nth root
            new FunctionRecord("floor(x)"),         // floor
            new FunctionRecord("ceil(x)"),          // ceiling
            new FunctionRecord("round(x)"),         // round
            new FunctionRecord("min(a,b)"),         // minimum
            new FunctionRecord("max(a,b)"),         // maximum
            new FunctionRecord("mod(x,y)"),         // modulo
            new FunctionRecord("diff(x,y)"),        // derivative
            new FunctionRecord("integrate(x,dx)"),  // integral
            new FunctionRecord("sum(x,y)"),         // summation
            new FunctionRecord("product(x,y)"),     // product
            new FunctionRecord("limit(dx,dt)")      // limit
    );


}
