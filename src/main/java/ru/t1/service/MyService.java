package ru.t1.service;

import org.springframework.stereotype.Service;
import ru.t1.annotation.ExecutionTime;
import ru.t1.annotation.LogException;
import ru.t1.annotation.LogExecution;
import ru.t1.annotation.ResultHandling;
import java.util.List;
import java.util.Random;
import net.datafaker.Faker;

@Service
public class MyService {


    @LogExecution
    public void greeting(String name) {
        System.out.println("Hello " + name);
    }

    @LogException
    public void division(int divisible, int divider) {
        System.out.println(divisible + " / " + divider + " = " + divisible / divider);
    }

    @LogExecution
    @ResultHandling
    public List<Integer> createListOfIntegers() {
        Random random = new Random();
        List<Integer> integers = random.ints(0, 10).limit(10).filter(n -> n >= 6).boxed().toList();
        System.out.println(integers);
        return integers;
    }

    @ExecutionTime
    public String printNames(int numberNamesToPrint) {
        Faker faker = new Faker();
        for (int i = 0; i < numberNamesToPrint; i++) {
            System.out.println(faker.name().fullName());
        }
        return "Names is printed.";
    }

    @ExecutionTime
    public String publishPermission(String permission) {
        System.out.println(permission);
        printNames(10);
        return permission;
    }
}
