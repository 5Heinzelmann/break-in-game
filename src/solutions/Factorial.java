import java.util.*;
import java.io.*;

class Factorial {

    public static long calculateFactorial(long inputNumber) {
        long factorial = inputNumber;
        for (int i = 1; i < inputNumber; i++) {
            System.out.println("factorial: " + factorial);
            System.out.println("inputNumber: " + (inputNumber - i));
            factorial = factorial * (inputNumber - i);
        }
        return factorial;
    }

    public static long calculateFactorial(long inputNumber) {
        return (inputNumber == 1 ? 1 : inputNumber * calculateFactorial(inputNumber - 1));
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
