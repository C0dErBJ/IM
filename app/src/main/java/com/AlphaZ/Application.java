package com.AlphaZ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.UnknownHostException;

/**
 * ProjectName: AlphaZ
 * PackageName: com.AlphaZ
 * User: C0dEr
 * Date: 2017/4/20
 * Time: 下午4:28
 * Description:This is a class of com.AlphaZ
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableAsync
public class Application {
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication.run(Application.class, args);
    }
}
