package ru.yumeno.nir;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yumeno.nir.service.AddressService;

@SpringBootTest
class NirApplicationTests {
    @Autowired
    private AddressService addressService;

    @Test
    void contextLoads() {
    }
}
