package no.rustelefonen.hapserver.entities.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Fredrik on 13.05.2016.
 */
@Constraint(validatedBy = MaxByteSizeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxByteSize {
    int value() default Integer.MAX_VALUE;
    String charset() default "UTF-8";
    String message() default "{no.rustelefonen.hapserver.MaxByteSize}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}