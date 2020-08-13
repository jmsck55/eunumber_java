
// MyEuNumber for Java

// Eunumber, advanced sequence based arithmetic with exponents

// myEunumber

// Big endian. Sequence math. With exponents.

// #Functions:
// GetVersion()

// #Big endian functions:
// MyEuNumber(long[] n1, int exp1, int targetLength, long myradix)
// MyEuNumber(long[] n1, int exp1)
// MyEuNumber(MyEuNumber a)
// MyEuNumber()
// PushFront
// PopFront
// PopBack
// Resize
// ArraysEqual
// RangeArraysEqual
// EunPrint()
// ToString()
// ToAtom() // float
// ToEun(float a, long radix, int targetLength)
//
// Borrow
// Carry
// CarryRadixOnly
// Add
// ConvertRadix
// Mult
// IsNegative
// Negate
// subtract
// TrimLeadingZeros
// TrimTrailingZeros
// SetRound
// GetRound
// AdjustRound
// MultExp
// AddExp
//
// ProtoMultInvExp
// IntegerToDigits
// IntArrayToString10
// ExpToAtom
// GetGuessExp
// SetCalcSpeed
// GetCalcSpeed
// MultInvExp
// DivideExp
// ConvertExp
//
// eun (type)
// EunMult
// EunAdd
// EunIsNegative
// EunNegate
// EunSubtract
// EunMultInv
// EunDivide
// EunConvert
//
// CompareExp
// EunCompare
// ReverseIntegerArray
// Reverse
// EunFracPart
// EunIntPart
// EunRoundSig
// EunRoundSignificantDigits
// EunRoundToInt
//
// IntPowerExp
// NthRootProtoExp
// NthRootExp
// EunNthRoot
// EunSquareRoot(n1, guess)
// EunSqrt(n1)
// MyEuNumber[] EunQuadraticEquation(a, b, c) // returns array of two (2) MyEuNumbers
//

package myeunumber;

/**
 *
 * @author jmsck
 */
public class MyEuNumber {
// Constants and variables
    // Main Variables:
    // {num, exp, targetLength, radix}
    public long[] num;
    public int exp;
    public int targetLength;
    public long radix;
    public int flags = 0; // pos_inf, neg_inf, imag, div_by_zero, nan_flag:
    // Flags and Variables (could be without "static")
    public static boolean isImaginaryFlag = false;
    public static boolean divideByZeroFlag = false;
    public static boolean nanFlag = false;
    public static long EUN_MAX_RADIX = 134217728L; // recommended maximum radix
    public static long EUN_MAX_RADIX10 = 100000000L; // pow(10, 8);
    public static int defaultTargetLength = 70;
    public static long defaultRadix = 1001L;
    public static float calculationSpeed = 1.0F; // defaultTargetLength / moreAccuracy
    // Used for debugging and optimization:
    public static int multInvIter = 1000000000;
    public static int lastIterCount = -1;
    public static int nthRootIter = 1000000000;
    public static int lastNthRootIter = -1;
    // Constants Required For Rounding:
    public static final int ROUND_INF = 1; // round towards +infinity or -infinity
    public static final int ROUND_ZERO = 2; // round towards zero
    public static final int ROUND_POS_INF = 4; // round towards positive +infinity
    public static final int ROUND_NEG_INF = 5; // round towards negative -infinity
    public static final int ROUND_EVEN = 6; // round towards negative -infinity
    public static final int ROUND_ODD = 7; // round towards negative -infinity
    public static final int ROUND_DOWN = ROUND_NEG_INF;
    public static final int ROUND_UP = ROUND_POS_INF;
    public static int roundingMethod = ROUND_EVEN; // or you could try: ROUND_INF or any other rounding method
    public static boolean roundToNearestOption = false; // Round to nearest whole number (Eun integer), true or false
    // constants required for division algorithm:
    public static int overflowBy = 0;
    public static final float LOG_ATOM_MAX = (float)Math.log((double)Float.MAX_VALUE);
    public static final float LOG_ATOM_MIN = (float)Math.log((double)Float.MIN_VALUE);
    public static final long[] TWO = { 2L };
    public static boolean realMode = false; // set to false to allow imaginary results

    // Version information:
    public static int GetVersion() {
        return 13;
    }
// Language specific functions:
    public MyEuNumber(final long[] n0, final int exp0, final int targetLength, 
                        final long myradix, final int myflags) {
        this.num = n0;
        this.exp = exp0;
        this.targetLength = targetLength;
        this.radix = myradix;
        this.flags = myflags;
    }

    public MyEuNumber(final long[] n0, final int exp0, final int targetLength, 
                        final long myradix) {
        this.num = n0;
        this.exp = exp0;
        this.targetLength = targetLength;
        this.radix = myradix;
        this.flags = 0;
    }

    public MyEuNumber(final MyEuNumber a) {
        // copy MyEuNumber
        this.num = a.num.clone();
        this.exp = a.exp;
        this.targetLength = a.targetLength;
        this.radix = a.radix;
        this.flags = a.flags;
    }

    public MyEuNumber(final long[] n1, final int exp1) {
        this.num = n1;
        this.exp = exp1;
        this.targetLength = defaultTargetLength;
        this.radix = defaultRadix;
        this.flags = 0;
    }

    public MyEuNumber() {
        this.num = new long[0];
        this.exp = 0;
        this.targetLength = defaultTargetLength;
        this.radix = defaultRadix;
        this.flags = 0;
    }

    public static long[] PushFront(final long[] num, final long c) { // s = prepend(s, i)
        final long[] a = new long[num.length + 1];
        a[0] = c;
        System.arraycopy(num, 0, a, 1, num.length);
        return a;
    }

    public static long[] PopFront(final long[] num) { // s = s[2..$]
        final int len = num.length - 1;
        final long[] a = new long[len];
        System.arraycopy(num, 1, a, 0, len);
        return a;
    }

    public static long[] PopBack(final long[] num) { // s = s[1..$-1]
        final int len = num.length - 1;
        final long[] a = new long[len];
        System.arraycopy(num, 0, a, 0, len);
        return a;
    }

    public static long[] Resize(final long[] src, int len) { // s = s[1..len]
        final long[] a = new long[len]; // zero initialized array.
        if (len > src.length) {
            len = src.length;
        }
        System.arraycopy(src, 0, a, 0, len);
        return a;
    }

    public static double RoundTowardsZero(final double x) {
        if (x < 0) {
            return Math.ceil(x);
        } else {
            return Math.floor(x);
        }
    }

    public static boolean ArraysEqual(final long[] a, final long[] b) {
        int len;
        if (a == b) {
            return true;
        }
        if (a != null) {
            if (b != null) {
                if (a.length == b.length) {
                    len = a.length;
                    len--;
                    for (int i = len; i >= 0; i--) {
                        if (a[i] != b[i]) {
                            return false;
                        }
                    }
                    return true; // arrays are equal!
                }
            }
        }
        return false;
    }

    public static boolean RangeArraysEqual(final long[] a, final long[] b, final int start, 
                                            final int stop) {
        if (a == b) {
            return true;
        }
        if (a != null) {
            if (b != null) {
                if (a.length > stop) {
                    if (b.length > stop) {
                        for (int i = stop; i >= start; i--) {
                            if (a[i] != b[i]) {
                                return false;
                            }
                        }
                        return true; // arrays are equal!
                    }
                }
            }
        }
        return false;
    }

    // Start of myeunumber functions:
    public static void Borrow(final long[] num, final long radix) {
        for (int i = num.length - 1; i >= 1; i--) {
            if (num[i] < 0) {
                num[i] += radix;
                num[i - 1]--;
            }
        }
    }

    public static long[] Carry(long[] num, final long radix) {
        long q, b;
        int i = num.length;
        while (i > 0) {
            i--;
            b = num[i];
            if (b >= radix) {
                q = b / radix; // integer division
                num[i] = b % radix; // modulus, or remainder
                if (i == 0) {
                    num = PushFront(num, q);
                    i++;
                } else {
                    i--;
                    b = num[i] + q;
                    if (b == 9223372036854775807L) {
                        throw new IllegalArgumentException("Error: In MyEuNumber, integer overflow error.");
                    }
                    num[i] = b;
                    i++;
                }
            }
        }
        return num;
    }

    public static long[] CarryRadixOnly(long[] num, final long radix) {
        long b;
        int i = num.length;
        i--;
        while (i >= 0) {
            b = num[i];
            if (b != radix) {
                break;
            }
            num[i] = 0; // modulus, or remainder
            if (i == 0) {
                num = PushFront(num, 1);
            } else {
                i--;
                num[i]++;
            }
        }
        return num;
    }

    public static long[] Add(final long[] n1, final long[] n2) {
        long[] sum;
        long[] tmp;
        int c, len;
        if (n1.length >= n2.length) {
            c = n1.length - n2.length;
            sum = n1.clone();
            tmp = n2;
        } else {
            c = n2.length - n1.length;
            sum = n2.clone();
            tmp = n1;
        }
        len = tmp.length;
        for (int i = 0; i < len; i++) {
            sum[c] += tmp[i];
            c++;
        }
        return sum;
    }

    public static long[] ConvertRadix(final long[] number, final long fromRadix, 
                                        final long toRadix) {
        // Initialize target as empty sequence:
        long[] target = {}; // same as: {0}
        long[] base = { 1L };
        long[] tmp;
        int len;
        long digit;
        boolean f;
        if (number.length > 0) {
            if (number[0] < 0) {
                Negate(number);
                f = true;
            } else {
                f = false;
            }
            for (int i = number.length - 1; i >= 0; i--) {
                tmp = base.clone();
                digit = number[i];
                len = tmp.length;
                for (int j = 0; j < len; j++) {
                    tmp[j] *= digit;
                }
                target = Add(tmp, target);
                target = Carry(target, toRadix);
                len = base.length;
                for (int j = 0; j < len; j++) {
                    base[j] *= fromRadix;
                }
                base = Carry(base, toRadix);
            }
            if (f) {
                Negate(target);
            }
        }
        return target;
    }

    public static long[] Mult(final long[] n1, final long[] n2) {
        long[] sum = {};
        long g, k;
        int myTargetLength, len1, len2, h;
        len1 = n1.length;
        len2 = n2.length;
        myTargetLength = len1 + len2;
        if (myTargetLength == 0) {
            return sum; // return empty sequence; zero (0) multiplied by zero (0).
        }
        myTargetLength--;
        sum = new long[myTargetLength];
        for (int i = 0; i < len1; i++) {
            h = i;
            g = n1[h];
            for (int j = 0; j < len2; j++) {
                k = g * n2[j] + sum[h];
                if ((k == 9223372036854775807L) || (k == -9223372036854775807L)) {
                    throw new IllegalArgumentException("Error: In MyEuNumber, integer overflow error.");
                }
                sum[h] = k;
                h++;
            }
        }
        return sum;
    }

    public static boolean IsNegative(final long[] numArray1) {
        if (numArray1.length > 0) {
            return numArray1[0] < 0;
        }
        return false;
    }

    public static void Negate(final long[] sum) {
        for (int i = 0; i < sum.length; i++) {
            sum[i] = -sum[i];
        }
    }

    public static void AbsoluteValue(final long[] sum) {
        if (sum.length > 0) {
            if (sum[0] < 0) {
                Negate(sum);
            }
        }
    }

    public static long[] Subtract(long[] sum, final long radix) {
        boolean needsNegate;
        if (sum.length > 0) {
            needsNegate = (sum[0] < 0);
            if (needsNegate) {
                Negate(sum); // uses reference
            }
            sum = Carry(sum, radix);
            Borrow(sum, radix); // uses reference
            if (needsNegate) {
                Negate(sum); // uses reference
            }
        }
        return sum;
    }

    public static long[] TrimLeadingZeros(final long[] num) {
        long[] a = {};
        int len = num.length;
        for (int i = 0; i < len; i++) {
            if (num[i] != 0) {
                if (i == 0) {
                    return num;
                } else {
                    len -= i;
                    a = new long[len];
                    System.arraycopy(num, i, a, 0, len);
                    return a;
                }
            }
        }
        return a;
    }

    public static long[] TrimTrailingZeros(final long[] num) {
        long[] a = {};
        int len = num.length;
        for (int i = len - 1; i >= 0; i--) {
            if (num[i] != 0) {
                if (i == len - 1) {
                    return num;
                } else {
                    len = i;
                    len++;
                    a = new long[len];
                    System.arraycopy(num, 0, a, 0, len);
                    return a;
                }
            }
        }
        return a;
    }

    public void SetRound(final int i) {
        roundingMethod = i;
    }

    public int GetRound() {
        return roundingMethod;
    }

    public void AdjustRound() {
        long halfRadix, f;
        int oldlen, roundTargetLength;
        oldlen = num.length;
        num = TrimLeadingZeros(num);
        exp += num.length - oldlen;
        // adjust_exponent();
        oldlen = num.length;
        // in subtract, the first element of sum cannot be a zero.
        num = Subtract(num, radix);
        // use subtract() when there are both negative and positive numbers.
        // otherwise, you can use Carry(). (for all positive numbers)
        // sum = Carry(sum, radix);
        num = TrimLeadingZeros(num);
        if (num.length == 0) {
            exp = 0;
            return;
        }
        exp += num.length - oldlen;
        // round2();
        halfRadix = radix >> 1; // floor(radix / 2)
        roundTargetLength = ((roundToNearestOption) && (targetLength > exp + 1)) ? exp + 1 : targetLength;
        if (roundTargetLength <= 1) {
            roundTargetLength = 1;
            if (exp <= -1) {
                if (exp == -1) {
                    num = PushFront(num, 0);
                } else {
                    num = new long[2];
                    num[0] = 0;
                    num[1] = 0;
                }
                exp = 0;
            }
        }
        oldlen = num.length;
        if (oldlen > roundTargetLength) {
            f = num[roundTargetLength];
            if ((radix & 1) == 1) { // if (IsIntegerOdd(radix))
                // feature: support for odd radixes
                // normally, if radix is 5, then 0,1 rounds down, 2,3,4 rounds up
                // actually, 1,2 round down, 3,4 round up
                // because, 0.2, 0.4 round down and 0.6, 0.8 round up
                //halfRadix++;
                // This seems to work better:
                for (int i = roundTargetLength + 1; i < oldlen; i++) {
                    if ((f != halfRadix) && (f != -halfRadix)) {
                        break;
                    }
                    f = num[i];
                }
            }
            if ((f == halfRadix) || (f == -halfRadix)) {
                switch (roundingMethod) {
                    case ROUND_EVEN:
                        halfRadix -= (num[roundTargetLength - 1] & 1); // IsIntegerOdd
                        break;
                    case ROUND_ODD:
                        halfRadix -= (num[roundTargetLength - 1] & 0); // IsIntegerEven
                        break;
                    case ROUND_ZERO:
                        f = 0;
                        break;
                    default:
                        break;
                }
            }
            else {
                switch (roundingMethod) {
                    case ROUND_INF:
                        halfRadix--;
                        break;
                    case ROUND_POS_INF:
                        f++;
                        break;
                    case ROUND_NEG_INF:
                        f--;
                        break;
                    default:
                        break;
                }
            }
            num = Resize(num, roundTargetLength);
            if (halfRadix < f) {
                num[num.length - 1]++;
                num = CarryRadixOnly(num, radix);
                exp += num.length - roundTargetLength;
            } else if (f < -halfRadix) {
                num[num.length - 1]--;
                Negate(num);
                num = CarryRadixOnly(num, radix);
                Negate(num);
                exp += num.length - roundTargetLength;
            }
        }
        num = TrimTrailingZeros(num);
        if (num.length == 0) {
            exp = 0;
        }
    }

    public static MyEuNumber MultExp(final long[] n1, final int exp1, final long[] n2, 
                                final int exp2, final int targetLength, final long radix) {
        MyEuNumber ret;
        long[] n0;
        n0 = Mult(n1, n2);
        ret = new MyEuNumber(n0, exp1 + exp2, targetLength, radix);
        ret.AdjustRound();
        return ret;
    }

    public static MyEuNumber AddExp(final long[] n1, final int exp1, final long[] n2, 
                                final int exp2, final int targetLength, final long radix) {
        MyEuNumber ret;
        long[] local, n0, p1, p2;
        int size = (n1.length - exp1) - (n2.length - exp2);
        if (size < 0) {
            size = n1.length - size;
            local = new long[size];
            System.arraycopy(n1, 0, local, 0, n1.length);
            p1 = local;
            p2 = n2;
        } else if (0 < size) {
            size += n2.length;
            local = new long[size];
            System.arraycopy(n2, 0, local, 0, n2.length);
            p1 = n1;
            p2 = local;
        } else {
            p1 = n1;
            p2 = n2;
        }
        n0 = Add(p1, p2);
        ret = new MyEuNumber(n0, (exp1 > exp2) ? exp1 : exp2, targetLength, radix);
        ret.AdjustRound();
        return ret;
    }

    // Division and Mult Inverse:
    public static MyEuNumber ProtoMultInvExp(final long[] n0, final int exp0, 
            final long[] n1, final int exp1, final int targetLength, final long radix) {
        // a = in0
        // n1 = in1
        // f(a) = a * (2 - (n1 * a))
        MyEuNumber tmp;
        long[] sum;
        int exp2;
        tmp = MultExp(n0, exp0, n1, exp1, targetLength, radix); // n1 * a
        sum = tmp.num;
        exp2 = tmp.exp;
        Negate(sum);
        tmp = AddExp(TWO, 0, sum, exp2, targetLength, radix); // 2 - tmp
        sum = tmp.num;
        exp2 = tmp.exp;
        tmp = MultExp(n0, exp0, sum, exp2, targetLength, radix); // a * tmp
        return tmp;
    }

    public static long[] IntegerToDigits(long x, final long radix) {
        long[] sum = new long[0];
        long a;
        while (x != 0) {
            a = x % radix; // remainder(x, radix)
            sum = PushFront(sum, a);
            x = (long) RoundTowardsZero(x / radix); // integer division
        }
        return sum;
    }

    public static String IntArrayToString10(final long[] p) {
        String st = new String();
        for (final long e : p) {
            st = st + e;
        }
        return st;
    }

    public static double ExpToAtom(final long[] num, int exp, final int targetLength, 
                                final long radix) {
        // this function returns two values: overflowBy, and ans
        double p, ans, lookat;
        int len;
        long ele;
        if (num.length == 0) {
            throw new IllegalArgumentException("Error: In MyEuNumber, tried to divide by zero."); // error
        }
        // what if exp is too large?
        p = Math.log(radix);
        overflowBy = exp - (int) (LOG_ATOM_MAX / p) + 2; // (+2 may need to be bigger)
        if (overflowBy > 0) {
            // overflow warning in "power()" function
            // reduce size:
            exp -= overflowBy;
        } else {
            // what if exp is too small?
            overflowBy = exp - (int) (LOG_ATOM_MIN / p) - 2; // (-2 may need to be bigger)
            if (overflowBy < 0) {
                exp -= overflowBy;
            } else {
                overflowBy = 0;
            }
        }
        exp -= targetLength;
        len = num.length;
        p = Math.pow(radix, exp);
        ans = num[0] * p;
        for (int i = 1; i < len; i++) {
            p = p / radix;
            ele = num[i];
            if (ele != 0) {
                lookat = ans;
                ans += ele * p;
                if (ans == lookat) {
                    break;
                }
            }
        }
        // if overflowBy is positive, then there was an overflow
        // overflowBy is an offset of that overflow in the given radix
        return ans;
    }

    public static MyEuNumber GetGuessExp(final long[] den, final int exp1, 
                                    final int protoTargetLength, final long radix) {
        MyEuNumber tmp;
        long[] guess;
        double denom, one;
        int len, sigDigits;
        long ans;
        sigDigits = (int) Math.ceil(15 / Math.log10(radix));
        if (protoTargetLength < sigDigits) {
            sigDigits = protoTargetLength;
        }
        len = den.length;
        denom = ExpToAtom(den, len - 1, sigDigits, radix);
        one = Math.pow(radix, len - 1 - overflowBy);
        ans = (long) RoundTowardsZero(one / denom);
        guess = IntegerToDigits(ans, radix); // works on negative numbers
        tmp = new MyEuNumber(guess, exp1, sigDigits - 1, radix);
        tmp.AdjustRound();
        tmp.targetLength = protoTargetLength;
        return tmp;
    }

    public void SetCalcSpeed(final float a) {
        calculationSpeed = a;
    }

    public float GetCalcSpeed() {
        return calculationSpeed;
    }

    public static MyEuNumber MultInvExp(final long[] den1, final int exp1, 
                                    int targetLength, final long radix) {
        MyEuNumber tmp, lookat;
        long[] guess;
        int exp0, protoTargetLength, len, lastLen, moreAccuracy;
        if (den1.length == 0) {
            lastIterCount = 1;
            tmp = new MyEuNumber(new long[0], 0, targetLength, radix, 1);
            divideByZeroFlag = true;
            throw new IllegalArgumentException("Error: In MyEuNumber, tried to divide by zero."); // error
        }
        if (den1.length == 1) {
            if ((den1[0] == 1) || (den1[0] == -1)) {
                lastIterCount = 1;
                tmp = new MyEuNumber(den1.clone(), -exp1, targetLength, radix);
                return tmp;
            }
        }
        moreAccuracy = (int) Math.ceil(targetLength / calculationSpeed);
        moreAccuracy++;
        protoTargetLength = targetLength + (moreAccuracy * 2);
        targetLength += moreAccuracy;
        exp0 = -exp1;
        exp0--;
        tmp = GetGuessExp(den1, exp0, protoTargetLength, radix);
        guess = tmp.num;
        lastLen = guess.length;
        lastIterCount = multInvIter;
        for (int i = 1; i <= multInvIter; i++) {
            lookat = tmp; // .clone(); // should there be more clones in this file?
            tmp = ProtoMultInvExp(guess, exp0, den1, exp1, protoTargetLength, radix);
            guess = tmp.num;
            exp0 = tmp.exp;
            len = guess.length;
            if (len > targetLength) {
                len = targetLength;
            }
            if (len == lastLen) {
                if (exp0 == lookat.exp) {
                    if (RangeArraysEqual(guess, lookat.num, 0, len - 1)) {
                        if (lastIterCount == i - 1) {
                            break;
                        }
                        lastIterCount = i;
                    }
                }
            }
            lastLen = len;
        }
        targetLength -= moreAccuracy;
        tmp.targetLength = targetLength;
        tmp.AdjustRound();
        return tmp;
    }

    public static MyEuNumber DivideExp(final long[] n1, final int exp1, 
            final long[] n2, final int exp2, final int targetLength, final long radix) {
        MyEuNumber tmp, ret;
        tmp = MultInvExp(n2, exp2, targetLength, radix);
        if (divideByZeroFlag) {
            if (n1.length == 0) {
                nanFlag = true;
            } else {
                tmp.flags = -1;
            }
            return tmp;
        } else {
            ret = MultExp(n1, exp1, tmp.num, tmp.exp, targetLength, radix);
            return ret;
        }
    }

    public static MyEuNumber ConvertExp(long[] n1, final int exp1, 
            final int targetLength, final long fromRadix, final long toRadix) {
        long[] n2, n3, local, p1;
        MyEuNumber result;
        int exp2, exp3;
        n1 = TrimTrailingZeros(n1);
        if (n1.length == 0) { // return value of zero:
            result = new MyEuNumber(new long[0], 0, targetLength, toRadix, 0);
            return result;
        }
        if (n1.length <= exp1) {
            p1 = new long[exp1 + 1];
            System.arraycopy(n1, 0, p1, 0, n1.length);
        } else {
            p1 = n1;
        }
        n2 = ConvertRadix(p1, fromRadix, toRadix);
        exp2 = n2.length;
        exp2--;
        // local = null;
        local = new long[p1.length - exp1];
        local[0] = 1;
        n3 = ConvertRadix(local, fromRadix, toRadix);
        exp3 = n3.length;
        exp3--;
        result = DivideExp(n2, exp2, n3, exp3, targetLength, toRadix);
        return result;
    }

    // Eun Functions:

    public void EunPrint() {
        final String indent = "  ";
        System.out.println("{");
        System.out.print(indent + "{");
        if (num.length != 0) {
            System.out.print(num[0]);
            for (int i = 1; i < num.length; i++) {
                System.out.print(",");
                System.out.print(num[i]);
            }
        }
        System.out.println("},");
        System.out.print(indent);
        System.out.print(exp);
        System.out.println(",");
        System.out.print(indent);
        System.out.print(targetLength);
        System.out.println(",");
        System.out.print(indent);
        System.out.println(radix);
        System.out.println("}");
    }

    public static MyEuNumber EunMult(final MyEuNumber n1, final MyEuNumber n2) {
        int targetLength;
        if (n1.radix != n2.radix) {
            return null;
        }
        if (n1.targetLength > n2.targetLength) {
            targetLength = n1.targetLength;
        } else {
            targetLength = n2.targetLength;
        }
        return MultExp(n1.num, n1.exp, n2.num, n2.exp, targetLength, n1.radix);
    }

    public static MyEuNumber EunAdd(final MyEuNumber n1, final MyEuNumber n2) {
        int targetLength;
        if (n1.radix != n2.radix) {
            return null;
        }
        if (n1.targetLength > n2.targetLength) {
            targetLength = n1.targetLength;
        } else {
            targetLength = n2.targetLength;
        }
        return AddExp(n1.num, n1.exp, n2.num, n2.exp, targetLength, n1.radix);
    }

    public static boolean EunIsNegative(final MyEuNumber n1) {
        return IsNegative(n1.num);
    }

    public static MyEuNumber EunNegate(final MyEuNumber n1) {
        final MyEuNumber ret = new MyEuNumber(n1);
        Negate(ret.num);
        return ret;
    }

    public static MyEuNumber EunSubtract(final MyEuNumber n1, final MyEuNumber n2) {
        MyEuNumber ret = EunNegate(n2);
        ret = EunAdd(n1, ret);
        return ret;
    }

    public static MyEuNumber EunMultInv(final MyEuNumber n1) {
        return MultInvExp(n1.num, n1.exp, n1.targetLength, n1.radix);
    }

    public static MyEuNumber EunDivide(final MyEuNumber n1, final MyEuNumber n2) {
        int targetLength;
        if (n1.radix != n2.radix) {
            return null;
        }
        if (n1.targetLength > n2.targetLength) {
            targetLength = n1.targetLength;
        } else {
            targetLength = n2.targetLength;
        }
        return DivideExp(n1.num, n1.exp, n2.num, n2.exp, targetLength, n1.radix);
    }

    public static MyEuNumber EunConvert(final MyEuNumber n1, final long toRadix, final int targetLength) {
        return ConvertExp(n1.num, n1.exp, targetLength, n1.radix, toRadix);
    }

    public static int CompareExp(long[] n1, int exp1, long[] n2, int exp2) {
        // It doesn't look at targetLength or radix, so they should be the same.
        final long[] local = { 0 };
        long[] a, b;
        int flag, len;
        if (n1.length == 0) {
            n1 = local;
            exp1 = exp2;
        }
        if (n2.length == 0) {
            n2 = local;
            exp2 = exp1;
        }
        if (exp1 > exp2) {
            return 1;
        } else if (exp1 < exp2) {
            return -1;
        }
        if (n1.length >= n2.length) {
            b = n1;
            a = n2;
            flag = 1;
        } else {
            a = n1;
            b = n2;
            flag = -1;
        }
        len = a.length;
        for (int i = 0; i < len; i++) {
            if (a[i] != b[i]) {
                if (a[i] > b[i]) {
                    return -flag;
                } else {
                    return flag;
                }
            }
        }
        if (a.length == b.length) {
            return 0;
        }
        if (b[0] < 0) {
            return -flag;
        } else {
            return flag;
        }
    }

    public static int EunCompare(final MyEuNumber n1, final MyEuNumber n2) {
        if (n1.radix != n2.radix) {
            throw new IllegalArgumentException("Error: In MyEuNumber, radixes are not the same."); // error
        }
        return CompareExp(n1.num, n1.exp, n2.num, n2.exp);
    }

    public static long[] ReverseIntegerArray(final long[] s) {
        int lower, n, n2;
        long[] t;
        n = s.length;
        n2 = (int) (n / 2);
        t = new long[n];
        lower = 0;
        for (int upper = n - 1; upper >= n2; upper--) {
            t[upper] = s[lower];
            t[lower] = s[upper];
            lower++;
        }
        return t;
    }

    public void EunReverse() {
        num = ReverseIntegerArray(num);
    }

    public static MyEuNumber EunFracPart(final MyEuNumber n1) {
        long[] a = {};
        int len, start;
        MyEuNumber ret;
        if (n1.exp < 0) {
            ret = new MyEuNumber(n1);
        } else {
            ret = new MyEuNumber(a, 0, n1.targetLength, n1.radix);
            if (n1.exp + 1 < n1.num.length) {
                start = n1.exp + 1;
                len = n1.num.length - start;
                a = new long[len];
                System.arraycopy(n1.num, start, a, 0, len);
                ret.num = a;
                ret.exp = -1;
            }
        }
        return ret;
    }

    public static MyEuNumber EunIntPart(final MyEuNumber n1) {
        long[] a = {};
        int len;
        MyEuNumber ret;
        len = n1.exp + 1;
        if (len >= n1.num.length) {
            ret = new MyEuNumber(n1);
        } else {
            ret = new MyEuNumber(a, 0, n1.targetLength, n1.radix);
            if (n1.exp >= 0) {
                a = new long[len];
                System.arraycopy(n1.num, 0, a, 0, len);
                ret.num = a;
                ret.exp = n1.exp;
            }
        }
        return ret;
    }

    public static MyEuNumber EunRoundSig(final MyEuNumber n1, final int sigDigits) {
        MyEuNumber ret = new MyEuNumber(n1);
        ret.targetLength = sigDigits;
        ret.AdjustRound();
        ret.targetLength = n1.targetLength;
        return ret;
    }

    public static MyEuNumber EunRoundSignificantDigits(final MyEuNumber n1, final int sigDigits) {
        return EunRoundSig(n1, sigDigits);
    }

//test this function:
    public static MyEuNumber EunRoundToInt(final MyEuNumber n1) {
        final long[] a = {};
        MyEuNumber ret;
        if (n1.exp < -1) {
            ret = new MyEuNumber(a, 0, n1.targetLength, n1.radix);
        } else {
            ret = EunRoundSig(n1, n1.exp + 1);
        }
        return ret;
    }

    // input, and output functions

    public String ToString() {
        MyEuNumber n1;
        String ret;
        long[] a;
        int len, exp1;
        boolean f;
        if (flags > 0) {
            return "inf";
        }
        if (flags < 0) {
            return "-inf";
        }
        if (radix != 10) {
            n1 = ConvertExp(num, exp, (int) Math.ceil(Math.log10(radix) * targetLength), radix, 10);
            a = n1.num;
            exp1 = n1.exp;
        } else {
            a = num;
            exp1 = exp;
        }
        if (num.length == 0) {
            ret = "0";
            return ret;
        }
        len = a.length;
        if (a[0] < 0) {
            ret = "-" + (char) (48 - a[0]);
            f = true;
        } else {
            ret = "" + (char) (a[0] + 48);
            f = false;
        }
        if (len > 1) {
            ret += ".";
            if (f) {
                for (int i = 1; i < len; i++) {
                    ret += (char) (48 - a[i]);
                }
            } else {
                for (int i = 1; i < len; i++) {
                    ret += (char) (a[i] + 48);
                }
            }
        }
        ret += "e" + Integer.toString(exp1);
        return ret;
    }

    public float ToAtom() {
        final String st = ToString();
        float ret;
        ret = Float.parseFloat(st);
        return ret;
    }

    public static MyEuNumber ToEun(final float a, long radix, int targetLength) {
        MyEuNumber ret;
        boolean negFlag;
        int exp1, f, len;
        long[] num1;
        char ch;
        String st = Float.toString(a);
        len = st.length();
        if (len == 0) {
            return null;
        }
        negFlag = (st.charAt(0) == '-');
        if ((negFlag) || (st.charAt(0) == '+')) {
            st = st.substring(1);
        }
        if (st.equals("inf")) {
            ret = new MyEuNumber(null, 0, defaultTargetLength, defaultRadix);
            if (negFlag) {
                ret.flags = -1;
            } else {
                ret.flags = 1;
            }
            return ret;
        }
        f = st.indexOf('e');
        if (f == -1) {
            f = st.indexOf('E');
        }
        if (f != -1) {
            exp1 = Integer.parseInt(st.substring(f + 1));
            st = st.substring(0, f);
        } else {
            exp1 = 0;
        }
        while ((st.length() != 0) && (st.charAt(0) == '0')) {
            st = st.substring(1);
        }
        f = st.indexOf('.');
        if (f != -1) {
            st = st.substring(0, f) + st.substring(f + 1);
            exp1 += f;
        } else {
            exp1 += st.length();
        }
        exp1--; // ok
        while ((st.length() != 0) && (st.charAt(0) == '0')) {
            exp1--;
            st = st.substring(1);
        }
        len = st.length();
        if (len == 0) {
            exp1 = 0;
        }
        num1 = new long[len];
        for (int i = 0; i < len; i++) {
            ch = st.charAt(i);
            if ((ch > '9') || (ch < '0')) {
                return null;
            }
            num1[i] = ((int) ch) - 48; // char '0' is int 48
        }
        if (negFlag == true) {
            Negate(num1);
        }
        if (radix == 0) {
            radix = defaultRadix;
        }
        if (targetLength == 0) {
            targetLength = defaultTargetLength;
        }
        ret = new MyEuNumber(num1, exp1, (int) (Math.ceil(Math.log10(radix) * targetLength)), 10);
        if (radix != 10) {
            ret = EunConvert(ret, radix, targetLength);
        }
        return ret;
    }

    // added functions:
    public static MyEuNumber IntPowerExp(final int toPower, final long[] n1, 
            final int exp1, final int max, final long radix) {
        MyEuNumber ret;
        long[] a;
        if (toPower == 0) {
            a = new long[1];
            a[0] = 1;
            ret = new MyEuNumber(a, 0, max, radix);
            return ret;
        }
        a = n1.clone();
        ret = new MyEuNumber(a, exp1, max, radix);
        for (int i = 2; i <= toPower; i++) {
            ret = MultExp(ret.num, ret.exp, n1, exp1, max, radix);
        }
        return ret;
    }

    public static MyEuNumber NthRootProtoExp(int n, final long[] x1, 
                    final int x1Exp, final long[] guess, final int guessExp, 
                    final int targetLength, final long radix) {
        // function nth_root(object p, object guess, object n)
        // object quotient, average
        // quotient = p / power(guess, n-1)
        // average = (quotient + ((n-1)* guess)) / n
        // return average
        // end function
        MyEuNumber p, quot, average;
        final long[] a = new long[1];
        n--;
        p = IntPowerExp(n, guess, guessExp, targetLength, radix);
        quot = DivideExp(x1, x1Exp, p.num, p.exp, targetLength, radix);
        a[0] = n;
        p = MultExp(a, 0, guess, guessExp, targetLength, radix);
        p = AddExp(p.num, p.exp, quot.num, quot.exp, targetLength, radix);
        n++;
        a[0] = n;
        average = DivideExp(p.num, p.exp, a, 0, targetLength, radix);
        return average;
    }

    public static MyEuNumber NthRootExp(final int n, final long[] x1, final int x1Exp, 
                    long[] guess, int guessExp, int targetLength, final long radix) {
        MyEuNumber tmp, lookat;
        int len, lastLen, protoTargetLength, moreAccuracy;
        if (x1.length == 0) {
            lastNthRootIter = 1;
            tmp = new MyEuNumber(x1.clone(), 0, targetLength, radix);
            return tmp;
        }
        if (x1.length == 1) {
            if ((x1[0] == 1) || (x1[0] == -1)) {
                lastNthRootIter = 1;
                tmp = new MyEuNumber(x1.clone(), 0, targetLength, radix);
                return tmp;
            }
        }
        moreAccuracy = (int) Math.ceil(targetLength / calculationSpeed);
        moreAccuracy++;
        protoTargetLength = targetLength + (moreAccuracy * 2);
        targetLength += moreAccuracy;
        tmp = new MyEuNumber(guess.clone(), guessExp, protoTargetLength, radix);
        lastLen = guess.length;
        lastNthRootIter = nthRootIter;
        for (int i = 1; i <= nthRootIter; i++) {
            lookat = tmp;
            tmp = NthRootProtoExp(n, x1, x1Exp, guess, guessExp, protoTargetLength, radix);
            guess = tmp.num;
            guessExp = tmp.exp;
            len = guess.length;
            if (len > targetLength) {
                len = targetLength;
            }
            if (len == lastLen) {
                if (guessExp == lookat.exp) {
                    if (RangeArraysEqual(guess, lookat.num, 0, len - 1)) {
                        if (lastNthRootIter == i - 1) {
                            break;
                        }
                        lastNthRootIter = i;
                    }
                }
            }
            lastLen = len;
        }
        targetLength -= moreAccuracy;
        tmp.targetLength = targetLength;
        tmp.AdjustRound();
        return tmp;
    }

    public static boolean IsEunNegative(final MyEuNumber n1) {
        if (n1.num.length > 0) {
            if (n1.num[0] < 0) {
                return true;
            }
        }
        return false;
    }

    public static MyEuNumber[] EunNthRootGuessExp(final int n, final MyEuNumber in1, 
                                        final MyEuNumber guess, int exp1) {
        // guess has to be positive on even roots
        MyEuNumber n1, ret;
        MyEuNumber[] retValues;
        int targetLen;
        n1 = in1;
        if (n1.radix != guess.radix) {
            return null;
        }
        targetLen = (n1.targetLength >= guess.targetLength) ? n1.targetLength : guess.targetLength;
        isImaginaryFlag = false;
        if ((n & 1) == 0) { // IsIntegerEven(n);
            if (IsEunNegative(n1)) {
                if (realMode) {
                    throw new IllegalArgumentException("Error: In MyEuNumber, even root of -1, i.e. sqrt(-1).");
                }
                // factor out sqrt(-1)
                isImaginaryFlag = true;
                n1 = EunNegate(n1);
            }
        }
        ret = NthRootExp(n, n1.num, n1.exp, guess.num, guess.exp, targetLen, n1.radix);
        exp1 = (int) (exp1 / n);
        ret.exp += exp1;
        if ((n & 1) == 0) { // IsIntegerEven(n);
            retValues = new MyEuNumber[2];
            retValues[0] = ret;
            retValues[1] = EunNegate(ret);
        } else {
            retValues = new MyEuNumber[1];
            retValues[0] = ret;
        }
        return retValues;
    }

    public static MyEuNumber[] EunNthRootGuess(final int n, final MyEuNumber in1, final MyEuNumber guess) {
        return EunNthRootGuessExp(n, in1, guess, 0);
    }

    public static MyEuNumber[] EunNthRoot(final int n, final MyEuNumber in1) {
        // Latest code:
        MyEuNumber n1 = new MyEuNumber(in1);
        int exp1 = n1.exp;
        final int f = exp1 % n; // remainder
        float a;
        boolean isOdd;
        if (f > 0) {
            exp1 -= f;
            if (exp1 <= 0) {
                exp1 += n;
            }
        }
        n1.exp -= exp1;
        a = n1.ToAtom();
        isOdd = false;
        if (a < 0) {
            // factor out sqrt(-1), an imaginary number, on even roots
            a *= -1;
            isOdd = (n & 1) == 1;
        }
        a = (float) Math.pow(a, 1.0 / n);
        if (isOdd) {
            a *= -1;
        }
        n1 = ToEun(a, n1.radix, n1.targetLength);
        return EunNthRootGuessExp(n, in1, n1, exp1);
    }

    // NOTE: There are other ways of doing square root,
    //       including: x(k) = (1/2) ( x(k-1) + (n / ( x(k-1) )) )
    //       which converges quadratically.
    //       Maybe for a future version of MyEuNumber.
    public static MyEuNumber[] EunSquareRoot(final MyEuNumber n1) {
        return EunNthRoot(2, n1);
    }

    public static MyEuNumber[] EunCubeRoot(final MyEuNumber n1) {
        return EunNthRoot(3, n1);
    }

    public static MyEuNumber[] EunSqrt(final MyEuNumber n1) {
        MyEuNumber[] retValues;
        MyEuNumber guess, local;
        float a;
        int exp1 = n1.exp;
        // factor out a perfect square, of a power of radix, an even number (used later)
        if ((exp1 & 1) == 1) { // if (IsIntegerOdd(exp1))
            if (exp1 > 0) {
                exp1--;
            } else {
                exp1++;
            }
        }
        local = new MyEuNumber(n1); // clone "n1"
        local.exp -= exp1;
        a = local.ToAtom(); // ExpToAtom(), similar function
        if (a < 0) {
            // factor out square root of negative one (sqrt(-1))
            a *= -1;
        }
        a = (float) Math.sqrt(a);
        guess = ToEun(a, local.radix, local.targetLength);
        retValues = EunNthRootGuess(2, local, guess);
        exp1 = (int) (exp1 / 2);
        retValues[0].exp += exp1;
        retValues[1].exp += exp1;
        return retValues;
    }

    public static MyEuNumber[] EunQuadraticEquation(final MyEuNumber a, 
                                    final MyEuNumber b, final MyEuNumber c) {
        // Quadratic Equation yields two results
        // ax^2 + bx + c
        // f(a,b,c) = (-b +-sqrt(b*b - 4*a*c)) / (2*a)
        // answer[0] = ((-b + sqrt(b*b - 4*a*c)) / (2*a))
        // answer[1] = ((-b - sqrt(b*b - 4*a*c)) / (2*a))
        //
        MyEuNumber n0, n1, ans, tmp;
        MyEuNumber[] retValues;
        long[] f;
        if ((a.radix != b.radix) || (a.radix != c.radix)) {
                return null;
        }
        if ((a.targetLength != b.targetLength) || (a.targetLength != c.targetLength)) {
                return null;
        }
        ans = EunMult(a, c); // a * c
        f = new long[1];
        f[0] = 4;
        tmp = new MyEuNumber(f, 0, a.targetLength, a.radix);
        ans = EunMult(tmp, ans); // 4 * a * c
        Negate(ans.num); // - 4 * a * c
        tmp = EunMult(b, b); // b * b
        ans = EunAdd(tmp, ans); // ans = b * b - 4 * a * c
        if ((ans.num.length != 0) && (ans.num[0] < 0)) {
            return null;
        }
        // +-sqrt(ans):
        retValues = EunSqrt(ans); // two (2) answers returned.
        tmp = EunNegate(b); // -b value
        if (isImaginaryFlag) {
            // Complex
            return null;
        }
        else {
            n0 = EunAdd(tmp, retValues[0]); // -b + sqrt(ans)
            n1 = EunAdd(tmp, retValues[1]); // -b - sqrt(ans)
            tmp = EunAdd(a, a); // 2a
            ans = EunMultInv(tmp); // 1/(2a)
            retValues = new MyEuNumber[2];
            retValues[0] = EunMult(n0, ans); // (-b + sqrt(ans)) / (2a)
            retValues[1] = EunMult(n1, ans); // (-b - sqrt(ans)) / (2a)
            return retValues;
        }
    }
    // here, Add more functions.
}
