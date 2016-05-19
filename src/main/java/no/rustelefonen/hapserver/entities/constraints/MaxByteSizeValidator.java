package no.rustelefonen.hapserver.entities.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.Charset;

/**
 * Created by Fredrik on 13.05.2016.
 */
public class MaxByteSizeValidator implements ConstraintValidator<MaxByteSize, String> {

    private MaxByteSize byteSize;

    @Override
    public void initialize(MaxByteSize byteSize) {
        this.byteSize = byteSize;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Charset charset = Charset.forName(byteSize.charset());
        int length = s.getBytes(charset).length;

        return length <= byteSize.value();
    }
}