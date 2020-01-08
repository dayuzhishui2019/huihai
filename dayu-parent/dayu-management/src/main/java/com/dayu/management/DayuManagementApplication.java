package com.dayu.management;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients
@EnableAsync
@SpringBootApplication(scanBasePackages = "com")
@MapperScan(basePackages = "com.dayu.**.mapper")
public class DayuManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayuManagementApplication.class, args);
    }


    @Bean
    public DockerClient create(@Value("${docker.runtime}") String url) {
        return DockerClientBuilder.getInstance(url).build();
    }

}
