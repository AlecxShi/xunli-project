import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Betty on 2017/7/15.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.xunli.manager")
public class ManagerApplication extends SpringBootServletInitializer {
    public static void main(String[] args)
    {
        SpringApplication.run(ManagerApplication.class,args);
    }
}
