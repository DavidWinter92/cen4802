public class Fibonacci {

    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) {
        int n = 10;

        System.out.print("The fibonacci sequence is: ");
        for (int i = 0; i <= n; i++) {
            System.out.print(fibonacci(i));
            if (i < n) {
                System.out.print(", ");
            }
        }
        System.out.println();

        int result = fibonacci(n);
        System.out.println("The " + n + "th term of the fibonacci sequence is " + result + ".");
    }
}
