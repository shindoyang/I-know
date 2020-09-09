import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description:
 * @Author: 杨耿
 * @Date: Create in 2020/9/9
 */
public class MyTest {
	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Object person = context.getBean("person");
		System.out.println(person);

	}
}
