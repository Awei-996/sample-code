package online.k12code;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Carl
 * @date 2023/8/15
 **/
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class })
public @interface EnableSwaggerDoc {

}
