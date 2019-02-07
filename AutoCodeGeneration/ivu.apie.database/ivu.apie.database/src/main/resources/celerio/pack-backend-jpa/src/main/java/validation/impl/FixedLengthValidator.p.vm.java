$output.java($ValidationImpl, "FixedLengthValidator")##

$output.requireStatic("org.apache.commons.lang.StringUtils.isEmpty")##
$output.require("javax.validation.ConstraintValidator")##
$output.require("javax.validation.ConstraintValidatorContext")##
$output.require($Validation, "FixedLength")##

public class $output.currentClass implements ConstraintValidator<FixedLength, String> {

    private FixedLength constraint;

    @Override
    public void initialize(FixedLength constraint) {
        this.constraint = constraint;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (isEmpty(value)) {
            return constraint.nullable();
        }
        return value.length() == constraint.length();
    }
}
