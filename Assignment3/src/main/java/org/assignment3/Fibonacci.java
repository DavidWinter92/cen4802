package org.assignment3;

/**
 * This class is used to work with the Fibonacci sequence.
 * The Fibonacci sequence is a series of numbers where each number
 * is the sum of the two numbers before it. The sequence starts with 0 and 1,
 * so the beginning looks like: 0, 1, 1, 2, 3, 5, 8, and so on.
 * <p>
 * The class includes a method to find the value of the nth term
 * in the sequence and a main method that prints out the sequence
 * up to a certain term and shows the value of that term.
 */
public class Fibonacci {
    /**
     * A simple modification to the int variable to test JUnit 5.
     */
    public static final int base_int = 8;

    /**
     * Finds the nth term of the Fibonacci sequence using recursion.
     * If n is 0, the method returns 0. If n is 1, it returns 1.
     * For any other number, it adds the two previous terms together.
     *
     * @param n the position in the sequence (starting from 0)
     * @return the value of the nth Fibonacci number
     */
    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * The main method of the program.
     * It prints the Fibonacci sequence up to the nth term
     * and then shows the value of that term.
     *
     * @param args command-line arguments (not used here)
     */
    public static void main(String[] args) {
        int n = base_int;

        // Print the sequence up to n
        System.out.print("The Fibonacci sequence is: ");
        for (int i = 0; i <= n; i++) {
            System.out.print(fibonacci(i));
            if (i < n) {
                System.out.print(", ");
            }
        }
        System.out.println();

        // Show the value of the nth term
        int result = fibonacci(n);
        System.out.println("The " + n + "th term of the Fibonacci sequence is " + result + ".");
    }
}
