package com.passwordmanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test
    void appCanBeInstantiated() {
        App app = new App();
        assertNotNull(app, "App instance should not be null");
    }
}
