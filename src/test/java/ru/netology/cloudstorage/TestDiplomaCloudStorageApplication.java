package ru.netology.cloudstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestDiplomaCloudStorageApplication {

    public static void main(String[] args) {
        SpringApplication.from(DiplomaCloudStorageApplication::main).with(TestDiplomaCloudStorageApplication.class).run(args);
    }

}
