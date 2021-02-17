import java.util.*;
import java.io.*;

class Factorial {

    public static int calculateFactorial(int number) {
        int factorial = number;
        for (int i = 1; i < number; i++) {
            System.out.println("factorial: " + factorial);
            System.out.println("number: " + (number - i));
            factorial = factorial * (number - i);
        }
        return factorial;
    }

    public static int calculateFactorial(int number) {
        return (number == 1 ? 1 : number * calculateFactorial(number - 1));
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
