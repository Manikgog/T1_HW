package ru.t1.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(controllers = {TaskController.class})
@AutoConfigureMockMvc(addFilters = false)
public class HelloControllerTest {

    @Test
    void helloTest() {

    }

    @Test
    void hello() {
    }

    @Test
    void postHello() {
    }
}
