package com.jkuo.sample.config;

//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ComponentScan;

//@ComponentScan
@Configuration
public class ProjectConfig {

    /**
    @Bean("BlueCat")
    Cat cat() {
        return new Cat("Orange");
    }
    **/


    /**
    direct wiring
    @Bean("BlueCat")
    Cat cat() {
        return new Cat("Orange");
    }

    @Bean("Alice")
    Person person() {
        return new Person("Alice", cat());
    }
    **/

    /**
    auto wiring

    @Bean("BlueCat")
    Cat cat() {
        return new Cat("Orange");
    }

    @Bean("Alice")
    Person person(Cat cat) {
        return new Person("Alice", cat);
    }

    /**
    qualifier

    @Bean("BlueCat")
    Cat cat1() {
        return new Cat("Orange");
    }

    Cat cat2() {
        return new Cat("Orange");
    }

    @Bean("Alice")
    Person person(@Qualifier("cat2") Cat cat) {
        return new Person("Alice", cat);
    }
    
    **/
}