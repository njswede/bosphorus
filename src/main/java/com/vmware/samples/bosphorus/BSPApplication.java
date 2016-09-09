package com.vmware.samples.bosphorus;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BSPApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BSPApplication.class, args);
    }
}