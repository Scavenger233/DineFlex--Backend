package com.dineflex.test;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class LombokTest {
    private String name;
    private int age;

    public static void main(String[] args) {
        LombokTest test = LombokTest.builder()
                .name("Alice")
                .age(25)
                .build();

        System.out.println(test);
    }
}
