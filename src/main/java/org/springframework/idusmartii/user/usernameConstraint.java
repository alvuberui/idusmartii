//package org.springframework.idusmartii.user;
//
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//import javax.validation.Constraint;
//import javax.validation.Payload;
//
//
//@Documented
//@Constraint(validatedBy = usernameValidator.class)
//@Target( {ElementType.METHOD,ElementType.FIELD })
//@Retention(RetentionPolicy.RUNTIME)
//public @interface usernameConstraint {
//	String message() default "This username is taken";
//	Class<?>[] groups() default {};
//	Class<? extends Payload>[] payload() default {};
//	
//	
//	
//}
