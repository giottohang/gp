package com.it;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan//扫描@WebFilter、@WebServlet等组件
public class CaseJopApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaseJopApplication.class, args);
    }
}
