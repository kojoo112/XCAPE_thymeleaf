package com.samsan.xcape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class XcapeApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcapeApplication.class, args);
    }

}
