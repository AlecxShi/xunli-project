import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Betty on 2017/7/15.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.xunli.manager")
@EntityScan(basePackages = "com.xunli.manager")
@ComponentScan(basePackages = "com.xunli.manager")
public class ManagerApplication extends SpringBootServletInitializer {
    public static void main(String[] args)
    {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
