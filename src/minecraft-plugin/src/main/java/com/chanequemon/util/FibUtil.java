package com.chanequemon.util;

public class FibUtil {

    public static int fib(int n) {
        if (n <= 0) return 0;
        if (n == 1 || n == 2) return 1;
        int a = 1, b = 1;
        for (int i = 3; i <= n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }

    public static int fibLog2(int n) {
        if (n <= 0) return 0;
        return (int) (Math.log(fib(n)) / Math.log(2));
    }
}
