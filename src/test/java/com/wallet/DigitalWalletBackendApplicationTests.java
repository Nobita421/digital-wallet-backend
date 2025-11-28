package com.wallet;

import com.wallet.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class DigitalWalletBackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
