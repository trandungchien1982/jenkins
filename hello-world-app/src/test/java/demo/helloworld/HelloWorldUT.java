package demo.helloworld;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class HelloWorldUT {

	Logger log = LoggerFactory.getLogger(getClass());

	@Test
	void test01() {
		log.info(">> Start Unit Test `test01()`");
		log.warn(".. End of Unit Test `test01()`");
	}

	@Test
	void test02() {
		log.info(">> Start Unit Test `test02()`");
		log.error(".. End of Unit Test `test02()`");
	}

}
