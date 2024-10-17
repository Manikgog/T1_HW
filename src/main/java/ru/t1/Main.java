package ru.t1;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.t1.service.MyService;

@ComponentScan
@EnableAspectJAutoProxy
public class Main {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        MyService myService = context.getBean(MyService.class);

        myService.greeting("Maxim");

        Thread.sleep(1000);

        myService.createListOfIntegers();

        Thread.sleep(1000);

        myService.printNames(10);

        Thread.sleep(1000);

        myService.publishPermission("access denied");

        Thread.sleep(1000);

        myService.division(1, 0);

    }
}