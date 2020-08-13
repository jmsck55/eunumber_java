cls
md mlib
javac myeunumber\MyEuNumber.java myeunumber\MyComplex.java
jar -c -f mlib\MyEuNumber.jar myeunumber\MyEuNumber.class myeunumber\MyComplex.class
javac -p mlib eunumber\EuNumber.java
jar cfm mlib\EuNumber.jar eunumber\Manifest.txt eunumber\EuNumber.class
pause
