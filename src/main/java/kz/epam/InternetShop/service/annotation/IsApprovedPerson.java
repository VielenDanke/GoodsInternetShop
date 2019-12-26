package kz.epam.InternetShop.service.annotation;

import javax.annotation.security.RolesAllowed;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public @interface IsApprovedPerson {
}
