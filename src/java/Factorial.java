import java.util.*;
import java.io.*;

class Factorial {

    public static int FirstFactorial(int num) {
        int factorial = num;
        for (int i = 1; i < num; i++) {
            //System.out.println("fac " + factorial);
            //System.out.println("num " + (num - i));
            factorial = factorial * (num - i);
        }
        return factorial;
    }

    public static int SolutionFirstFactorialRecursiv(int num) {
        return (num == 1 ? 1 : num * FirstFactorial(num - 1));
    }

    public static void main(String[] args) {
        int input = 999999999;

        long startTime = System.nanoTime();
        System.out.println(FirstFactorial(input));
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("1. Total execution time in millis: "
                + elapsedTime / 1000000);

        startTime = System.nanoTime();
        System.out.println(SolutionFirstFactorialRecursiv(input));
        elapsedTime = System.nanoTime() - startTime;
        System.out.println("2. Total execution time in millis: "
                + elapsedTime / 1000000);
    }
}

//CODERBYTE CODE

//import java.util.*;
//import java.io.*;
//
//class Main {
//
//    public static int FirstFactorial(int num) {
//        // code goes here
//        int factorial = num;
//        for (int i = 1; i < num; i++) {
//            //System.out.println("fac " + factorial);
//            //System.out.println("num " + (num - i));
//            factorial = factorial * (num-i);
//        }
//        return factorial;
//    }
//
//    public static void main (String[] args) {
//        // keep this function call here
//        Scanner s = new Scanner(System.in);
//        System.out.print(FirstFactorial(s.nextLine()));
//    }
//}
