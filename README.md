# eunumber_java
Eunumber for fast, accurate calculations of Big Numbers (translated into Java)

Change your units so that they are close to one (1). If you have 1000 grams, use 1 kilogram for instance.
This would be represented as the sequence: {{1} , 0 , maxlength , radix} of type "eun";

Example: 1000 grams would be: {{1} , 3 , maxlength , 10}; or {{1} , 0 , maxlength/3 , 1000};

Zero is represented as: {{} , 0 , maxlength , radix}; notice the empty sequence as the first argument.

2.17 is: {{2 , 1 , 7} , 0 , maxlength , 10};

Note:
Maxlength is the maximum number of significant digits.  A good maxlength is about 200 or less on modern computers.
Always carry out your numbers to a few more decimal places of accuracy,
to make sure you have enough accuracy for your number of significant digits.

In other words:
Always increase maxlength to a few more integers than you need.
This is good programming practise when doing mathematics on a computer.

When doing division, you must observe the "more_accuracy" variable.
All higher math functions rely on division (or "mult_inv(x) = 1/x").

With larger maxlengths, you will need to increase the more accuracy
variable.  Increase the "more_accuracy" variable to get more accuracy.
Decrease it to get less accuracy, with smaller maxlengths.

"more_accuracy" variable should be about one seventh (1/7) to one sixth (1/6) of maxlength, (1/6 being larger than 1/7).

You can use radix of 2 to 1000 on 32-bit, up to 100,000,000 safely on 64-bit. (It can actually be a little larger than this).
I favor preserving accuracy over speed or size, in programming.

Keep in mind: Square roots (even roots) are always plus (+) and minus (-),
So you have to account for both plus and minus values, by splitting them up
and running both of them through each function or operation that they need to go through.
(Complex numbers' imaginary part is a square root, and it, the square root, is always plus and minus.)

Get latested updates from:
https://github.com/jmsck55/eunumber_java
