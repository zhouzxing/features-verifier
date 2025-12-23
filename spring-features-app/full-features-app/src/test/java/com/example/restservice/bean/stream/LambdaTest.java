package com.example.restservice.bean.stream;

import lombok.Data;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @className: LambdaTest
 * @author: geeker
 * @date: 11/17/25 7:39 PM
 * @Version: 1.0
 * @description:
 */

public class LambdaTest {

    @Test
    public void test(){
        List<Employer> employers = new ArrayList<>();
        List<Employer> employers1 = employers.stream().sorted((o1, o2) -> {
            if (o1.getDepartment().equals(o2.getDepartment())) {
                return o2.salary.compareTo(o1.salary);
            }
            return o1.department.compareTo(o2.department);
        }).collect(Collectors.toList());


        employers.stream().sorted(
                Comparator.comparing(Employer::getDepartment)
                        .thenComparing(Employer::getSalary).reversed())
                .collect(Collectors.toList());

        employers.stream().sorted(Comparator.comparing(Employer::getDepartment)
                .thenComparing(Comparator.comparing(Employer::getSalary).reversed()))
                .collect(Collectors.toList());

        // null 处理

        /*employers.stream().sorted(Comparator.comparing(
                employer -> employer.getDepartment() == null ? "" : employer.getDepartment(),
                Comparator.nullsFirst(Comparator.naturalOrder()))
                .thenComparing(Comparator.comparing(employer -> employer.getSalary))
        );*/

        employers.stream().sorted(Comparator.comparing(Employer::getDepartment));

    }


    @Test
    public void testNull(){
        /*List<String> strings = Arrays.asList("aaa", "bbb", "ccc",null);
        strings.sort(Comparator.naturalOrder());
        System.out.println(strings);*/

        String s = null;
        System.out.println( s.compareTo("11"));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        list.sort(Comparator.reverseOrder());


        File file = new File("./src/main/resources/employees.txt");
    }

    @Test
    public void testList(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        list.clear();
        list.add(1);
        list.add(1);
        System.out.println(list.size());
    }

    @Test
    public void testFloat() {
        float f1 = 0.1f;
        float f2 = 0.1f;

        Float f3 = new Float(0.1f);
        Float f4 = new Float(0.1f);

        System.out.println(f1 == f2);
        System.out.println(f1 == f3);
        System.out.println(f3 == f4);


        int i1 = 1;
        int i2 = 1;
        Integer i3 = new Integer(1);
        Integer i4 = new Integer(1);
        System.out.println(i1 == i2);
        System.out.println(i1 == i3);
        System.out.println(i3 == i4);


        Integer i = Integer.valueOf(128);
        Integer ii = Integer.valueOf(128);
        Integer j = Integer.valueOf(127);
        Integer j1 = Integer.valueOf(127);

        System.out.println(i == ii);
        System.out.println(j == j1);
    }



    @Test
    public void threads() {

        // 改进： 原子递增
        final int[] j = {0};

        //ThreadPoolExecutor.
        ExecutorService addPool = Executors.newFixedThreadPool(2);
        ExecutorService subPool = Executors.newFixedThreadPool(2);

        addPool.execute(() -> {
            try {
                while (true) {
                    synchronized (j) {
                        j[0]++;
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        subPool.execute(() -> {
            try {
                while (true) {
                    synchronized (j) {
                        j[0]--;
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    public void testThread() throws InterruptedException {
        AtomicInteger j = new AtomicInteger();

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);

        List<Future> list = new ArrayList();

        list.add(scheduledThreadPool.scheduleAtFixedRate(()->{j.incrementAndGet();},1,1,TimeUnit.MINUTES));
        list.add(scheduledThreadPool.scheduleAtFixedRate(()->{j.decrementAndGet();},1,1,TimeUnit.MINUTES));

        Thread.sleep(1000);
        list.forEach(future -> {future.cancel(true);});
        scheduledThreadPool.shutdown();
    }


    @Data
    class Employer {
        private String name;
        private String department;
        private Double salary;
    }

}
