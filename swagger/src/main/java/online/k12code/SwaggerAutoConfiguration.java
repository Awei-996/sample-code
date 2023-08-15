package online.k12code;

import com.github.xiaoymin.knife4j.core.util.StrUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.time.LocalTime;

/**
 * @author Carl
 * @date 2023/8/15
 **/
@Configuration
@EnableSwagger2WebMvc
public class SwaggerAutoConfiguration {

    private static final String DESC_FORMAT = "<div style='font-size:14px;color:red;'>%s RESTful APIs</div>";
    private static final String SERVICE_URL = "https://want-want.com";
    private static final String DEFAULT_APPLICATION = "default-service";

    @Bean
    public Docket groupRestApi(Environment environment) {
        String applicationName = environment.getProperty("spring.application.name");
        if(StrUtil.isBlank(applicationName)){
            applicationName = DEFAULT_APPLICATION;
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(groupApiInfo(applicationName))
                .select()
                .apis(RequestHandlerSelectors.basePackage(ClassUtils.mainClassPackage()))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(LocalTime.class,String.class);
    }

    private ApiInfo groupApiInfo(String applicationName){
        String desc = String.format(DESC_FORMAT,applicationName);
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(desc)
                .termsOfServiceUrl(SERVICE_URL)
                .contact(new Contact("","",""))
                .version("1.0")
                .build();
    }
}

