package com.example.restservice.bean.stream;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @className: LambdaUtils
 * @author: geeker
 * @date: 11/13/25 3:44 PM
 * @Version: 1.0
 * @description:
 */

public class LambdaAppTest {

    @Data
    @Accessors(chain = true)
    class Person {
        private String id;
        private String name;
    }

    private List<Person> persons;

    @Before
    public void before() {
        persons = Arrays.asList(
                new Person().setId("1").setName("ddd"),
                new Person().setId("2").setName("ddd"),
                new Person().setId("3").setName("ddd"),
                new Person().setId("4").setName("ddd")
        );
    }


    @Test
    public void test(){
        Map<String, Person> map = persons.stream().collect(
                Collectors.toMap(Person::getId, Function.identity()));
        map = persons.stream().collect(
                Collectors.toMap(Person::getId, Function.identity(),(a,b) -> a)
        );

        Set<Person> set = persons.stream().collect(Collectors.toSet());

        List<Person> personList = set.stream()
                .sorted(Comparator.comparing(Person::getId))
                .collect(Collectors.toList());

        personList = set.stream().sorted(
                Comparator
                        .comparing(Person::getId, Comparator.reverseOrder())
                        .thenComparing(Person::getName, Comparator.reverseOrder())
                ).collect(Collectors.toList());


    }

}
