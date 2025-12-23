package com.example.restservice.bean.algo;

/**
 * @className: Main
 * @author: geeker
 * @date: 11/17/25 8:59 PM
 * @Version: 1.0
 * @description:
 */
import java.util.Scanner;
import java.util.*;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class SimpleSort {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        int n = in.nextInt();
        int b = in.nextInt();
        List<Integer> list = new ArrayList();

        for(int i = 0; i < n; i++){
            list.add(in.nextInt());
        }

        list.sort(Comparator.reverseOrder());
        int res = 0, total = 0;
        while(total < b) {
            total += list.get(res);
            res++;
        }
        System.out.println(res);

    }
}
