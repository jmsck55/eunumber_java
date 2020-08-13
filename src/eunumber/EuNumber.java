/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eunumber;

//import java.util.*;
import myeunumber.MyEuNumber;

/**
 *
 * @author jmsck
 */
public class EuNumber {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MyEuNumber aa, bb, cc, dd;
        MyEuNumber[] ptr;
        MyEuNumber[] s;
        long[] n1 = {-2,-3};
        long[] n2 = {4};
        
        aa = new MyEuNumber(n1, 1);
        bb = new MyEuNumber(n2, 0);
        cc = MyEuNumber.EunDivide(aa,bb);
        cc.EunPrint();
        s = MyEuNumber.EunSqrt(aa);
        cc = s[0];
        System.out.println("Answer is:");
        if (MyEuNumber.isImaginaryFlag) {
            System.out.println("Number is imaginary:");
        }
        cc.EunPrint();
        // do unit-testing with all variables and functions.
        dd = new MyEuNumber(n2, -1, 100, 16);
        dd.EunPrint();
        cc = MyEuNumber.EunConvert(dd, 10, 100);
        cc.EunPrint();
        
        aa = MyEuNumber.ToEun(2,0,0);
        bb = MyEuNumber.ToEun(3,0,0);
        cc = MyEuNumber.ToEun(1,0,0);
        ptr = MyEuNumber.EunQuadraticEquation(aa, bb, cc);
        System.out.println("Answers for quadratic_equation():");
        if (ptr == null) {
            System.out.println("Something went wrong. Try using 'complex_quadratic_equation()' instead.");
        }
        else {
            System.out.println(ptr[0].ToString());
            System.out.println(ptr[1].ToString());

            ptr[0].EunPrint();
            ptr[1].EunPrint();
        }
        
        
        
        System.out.println("Hi, this works!");
        
    }
    
}
