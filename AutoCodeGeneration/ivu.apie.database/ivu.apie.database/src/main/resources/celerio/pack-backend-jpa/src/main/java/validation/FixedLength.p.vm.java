$output.java($Validation, "FixedLength")##

$output.requireStatic("java.lang.annotation.ElementType.ANNOTATION_TYPE")##
$output.requireStatic("java.lang.annotation.ElementType.FIELD")##
$output.requireStatic("java.lang.annotation.ElementType.METHOD")##
$output.requireStatic("java.lang.annotation.RetentionPolicy.RUNTIME")##
$output.require("java.lang.annotation.Documented")##
$output.require("java.lang.annotation.Retention")##
$output.require("java.lang.annotation.Target")##
$output.require("javax.validation.Constraint")##
$output.require("javax.validation.Payload")##
$output.require($ValidationImpl, "FixedLengthValidator")##

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = FixedLengthValidator.class)
@Documented
public @interface $output.currentClass {
    int length() default 0;

    boolean nullable() default true;

    String message() default "{${Validation.packageName}.FixedLength.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
