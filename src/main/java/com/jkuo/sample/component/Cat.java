package com.jkuo.sample.component;

import org.springframework.stereotype.Component;

@Component("OrangeCat")
public class Cat {

    private String name;

    public Cat() {
        this.name = "Cat";
    }

    public Cat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}