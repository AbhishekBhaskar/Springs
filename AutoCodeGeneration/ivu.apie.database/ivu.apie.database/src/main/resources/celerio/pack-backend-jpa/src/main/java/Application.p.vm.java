$output.java($Root,"Application")##

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.test.*"})
@EntityScan("com.test.*")
@EnableJpaRepositories("com.test.*")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /*@Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new Angular2PathLocationStrategyCustomizer();
    }

    // http://stackoverflow.com/questions/36761019/how-can-i-use-angular2-pathlocationstrategy-in-a-spring-boot-application
    private static class Angular2PathLocationStrategyCustomizer implements EmbeddedServletContainerCustomizer {
        @Override
        public void customize(ConfigurableEmbeddedServletContainer container) {
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/"));
        }
    }*/
}
