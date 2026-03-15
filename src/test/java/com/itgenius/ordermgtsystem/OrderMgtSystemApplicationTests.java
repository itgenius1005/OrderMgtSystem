package com.itgenius.ordermgtsystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:16:///testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OrderMgtSystemApplicationTests {

    @Test
    void contextLoads() {
    }
}
