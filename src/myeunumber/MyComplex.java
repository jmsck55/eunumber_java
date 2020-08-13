
// MyEuNumber for Java

// Complex numbers

// Untested, but should still work or be able to be fixed.

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myeunumber;


/**
 *
 * @author jmsck
 */
public class MyComplex {
    MyEuNumber real;
    MyEuNumber imag;
    
    public MyComplex(MyEuNumber real0, MyEuNumber imag0) {
        this.real = real0;
        this.imag = imag0;
    }
    public MyComplex(MyComplex a) {
        // Copy MyComplex
        this.real = new MyEuNumber(a.real);
        this.imag = new MyEuNumber(a.imag);
    }
    public MyComplex() {
        this.real = new MyEuNumber();
        this.imag = new MyEuNumber();
    }
    public void NegateImagPart() {
        // Negate the imaginary part of the number, giving: -imag
        MyEuNumber.Negate(this.imag.num);
    }
    public static MyComplex ComplexAdd(MyComplex a, MyComplex b) {
        MyComplex ret = new MyComplex(
                MyEuNumber.EunAdd(a.real, b.real),
                MyEuNumber.EunAdd(a.imag, b.imag)
        );
        return ret;
    }
    public static MyComplex ComplexNegate(MyComplex b) {
        MyComplex ret = new MyComplex(
                MyEuNumber.EunNegate(b.real),
                MyEuNumber.EunNegate(b.imag)
        );
        return ret;
    }
    public static MyComplex ComplexSubtract(MyComplex a, MyComplex b) {
        MyComplex ret = new MyComplex(
                MyEuNumber.EunSubtract(a.real, b.real),
                MyEuNumber.EunSubtract(a.imag, b.imag)
        );
        return ret;
    }
    public static MyComplex ComplexMult(MyComplex n1, MyComplex n2) {
        MyEuNumber real0 = MyEuNumber.EunSubtract(
                MyEuNumber.EunMult(n1.real, n2.real),
                MyEuNumber.EunMult(n1.imag, n2.imag)
        );
        MyEuNumber imag0 = MyEuNumber.EunAdd(
                MyEuNumber.EunMult(n1.real, n2.imag),
                MyEuNumber.EunMult(n1.imag, n2.real)
        );
        MyComplex ret = new MyComplex(real0, imag0);
        return ret;
    }
    public static MyComplex ComplexMultInv(MyComplex n2) {
        // Complex Multiplicative Inverse, f(x) = 1/x
        //  eun a, b, c
        //  (a+bi)(a-bi) <=> a*a + b*b
        //  n2 = (a+bi)
        //  a = n2[1]
        //  b = n2[2]
        //  c = (a*a + b*b)
        //  1 / n2 <=> (a-bi) / (a*a + b*b)
        //  <=> (a / (a*a + b*b)) - (b / (a*a + b*b))i
        //  <=> (a / c) - (b / c)i
        MyEuNumber a, b, c, real0, imag0;
        MyComplex ret;
        a = n2.real;
        b = n2.imag;
        c = MyEuNumber.EunMultInv(
                MyEuNumber.EunAdd(
                    MyEuNumber.EunMult(a, a),
                    MyEuNumber.EunMult(b, b)
                )
        );
        real0 = MyEuNumber.EunMult(a, c);
        imag0 = MyEuNumber.EunNegate(MyEuNumber.EunMult(b, c));
        ret = new MyComplex(real0, imag0);
        return ret;
    }
    public static MyComplex ComplexDivide(MyComplex n1, MyComplex n2) {
        MyComplex ret = ComplexMult(n1, ComplexMultInv(n2));
        return ret;
    }
    public static MyComplex[] ComplexSqrt(MyComplex a) {
        // sqrt(x + iy) <=> (1/2) * sqrt(2) * [ sqrt( sqrt(x*x + y*y) + x )  +  i*sign(y) * sqrt( sqrt(x*x + y*y) - x ) ]
        // NOTE: results are both positive and negative. Remember i (the imaginary part) is always both positive and negative in mathematics.
        // NOTE: So, you will need to keep in mind that information in your equations.
        MyEuNumber x, y, n1, n2, tmp, tmptwo;
        MyComplex c1, c2;
        MyComplex[] ret;
        MyEuNumber[] s;
        int moreAccuracy;
        x = new MyEuNumber(a.real);
        y = new MyEuNumber(a.imag);
        moreAccuracy = (int) Math.ceil(x.targetLength / MyEuNumber.calculationSpeed);
        moreAccuracy++;
        x.targetLength += moreAccuracy;
        y.targetLength += moreAccuracy;
        n1 = MyEuNumber.EunMult(x, x);
        n2 = MyEuNumber.EunMult(y, y);
        tmp = MyEuNumber.EunAdd(n1, n2);
        s = MyEuNumber.EunSqrt(tmp); // should not return an imaginary number
        tmp = s[0]; // get the postive answer
        n1 = MyEuNumber.EunAdd(tmp, x);
        n2 = MyEuNumber.EunSubtract(tmp, x);
        //n1
        s = MyEuNumber.EunSqrt(n1);
        if (MyEuNumber.isImaginaryFlag) {
            throw new IllegalArgumentException("Error: In MyEuNumber, unexpected imaginary number.");
        }
        n1 = s[0]; // get the postive answer
        //n2
        s = MyEuNumber.EunSqrt(n2);
        if (MyEuNumber.isImaginaryFlag) {
            throw new IllegalArgumentException("Error: In MyEuNumber, unexpected imaginary number.");
        }
        n2 = s[0]; // get the postive answer
        if (MyEuNumber.EunIsNegative(y)) { // sign of y, if negative then negate()
            MyEuNumber.Negate(n2.num);
        }
        //tmp and tmptwo
        tmptwo = new MyEuNumber(MyEuNumber.TWO, 0, a.real.targetLength, a.real.radix);
        s = MyEuNumber.EunSqrt(tmptwo); // tmptwo, should not return an imaginary number
        tmp = s[0]; // get the postive answer
        tmp = MyEuNumber.EunDivide(tmp, tmptwo);
        n1 = MyEuNumber.EunMult(n1, tmp);
        n2 = MyEuNumber.EunMult(n2, tmp);
        n1.targetLength -= moreAccuracy;
        n2.targetLength -= moreAccuracy;
        n1.AdjustRound();
        n2.AdjustRound();
        c1 = new MyComplex(n1, n2);
        c2 = ComplexNegate(c1);
        ret = new MyComplex[2];
        ret[0] = c1;
        ret[1] = c2;
        return ret;
    }
    public static MyComplex[] complex_quadratic_equation(MyComplex a, MyComplex b, MyComplex c) {
        //  About the Quadratic Equation :
        //  
        //  The quadratic equation produces two answers (the two answers may be the same)
        //  ax ^ 2 + bx + c
        //   f(a, b, c) = (-b + -sqrt(b * b - 4 * a * c)) / (2 * a)
        //  answer[0] = ((-b + sqrt(b * b - 4 * a * c)) / (2 * a))
        //  answer[1] = ((-b - sqrt(b * b - 4 * a * c)) / (2 * a))
        //  
        //  The "Complex" quadratic equation produces about 2 results
        //  
        MyComplex ans, tmp;
        MyComplex[] s;
        long[] f;
        ans = ComplexMult(a, c);
        f = new long[1];
        f[0] = 4;
        tmp = new MyComplex(new MyEuNumber(f, 0, a.real.targetLength, a.real.radix),
                            new MyEuNumber(new long[0], 0, a.imag.targetLength, a.imag.radix));
        ans = ComplexMult(tmp, ans);
        tmp = ComplexMult(b, b);
        ans = ComplexSubtract(tmp, ans);
        s = ComplexSqrt(ans); // returns two values
        tmp = ComplexNegate(b);
        s[0] = ComplexAdd(s[0], tmp);
        s[1] = ComplexAdd(s[1], tmp); // s[1] is already negative
        ans = ComplexAdd(a, a); // 2 * a
        ans = ComplexMultInv(ans);
        s[0] = ComplexMult(s[0], ans);
        s[1] = ComplexMult(s[1], ans);
        return s;
    }
}
