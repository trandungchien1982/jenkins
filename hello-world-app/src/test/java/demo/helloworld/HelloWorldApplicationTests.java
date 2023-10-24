package demo.helloworld;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelloWorldApplicationTests {

	Logger log = LoggerFactory.getLogger(getClass());

	@Test
	void contextLoads() {
		log.info("... Context loads of Integration Tests...");
	}

	@Test
	void executeITNumber01() {
		log.info("... Execute Integration Tests, number 01 ...");
	}

	@Test
	void executeITNumber02() {
		log.info("... Execute Integration Tests, number 02 ...");
	}

}
