package com.dayu.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.dayu.**.mapper")
public class DayuManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayuManagementApplication.class, args);
    }

}
