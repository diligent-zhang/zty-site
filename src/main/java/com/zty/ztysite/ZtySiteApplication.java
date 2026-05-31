package com.zty.ztysite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zty.ztysite.mapper")
public class ZtySiteApplication {

   public static void main(String[] args) {
        SpringApplication.run(ZtySiteApplication.class, args);
    }


}
