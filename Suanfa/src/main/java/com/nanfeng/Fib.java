package com.nanfeng;

import java.util.Scanner;

/**
 * Author：nanfeng
 * Created:2019/7/20
 */
public class Fib {
    //递归 --- 时间复杂度：O(2^n)
    private static int fibonacci(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    //动态递归 --- 时间复杂度：O(n)
    private static int fibonacci2(int n) {
        if (n <= 0) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }

        //申请一个数组，保存子问题的解，题目要求从0开始
        int[] arr = new int[n + 1];
        arr[0] = 0;
        arr[1] = 1;
        for (int i = 2; i <= n; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];
    }

    //优化 --- 时间复杂度：O(1)
    private static int fibonacci3(int n){
        if (n<=0){
            return 0;
        }
        if (n==1 || n==2){
            return 1;
        }
        int fn1 = 1;
        int fn2 = 1;
        int result = 0;
        for (int i=3;i<=n;i++){
            result = fn2 + fn1;
            fn1 = fn2;
            fn2 = result;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
//        int result = fibonacci(num);
//        int result = fibonacci2(num);
        int result = fibonacci3(num);
        System.out.println(result);
    }
}
