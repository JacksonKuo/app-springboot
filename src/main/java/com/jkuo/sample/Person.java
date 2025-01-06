package com.jkuo.sample;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component("Alice")
public class Person {

    private String name;
    //@Autowired
    private final Cat cat; 

    /** 
    public Person() {
        this.name = "Bob";
    }

    public Person(String name) {
        this.name = name;
    }
    **/

    public Person(String name, Cat cat) {
        this.name = name;
        this.cat = cat;
    }

    @Autowired
    public Person(Cat cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cat getCat() {
        return cat;
    }

}