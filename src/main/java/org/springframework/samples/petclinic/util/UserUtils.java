
package org.springframework.samples.petclinic.util;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class UserUtils {

	public static final String getCurrentUsername() {
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();
		if (principal instanceof User) {
			return ((User) context.getAuthentication().getPrincipal()).getUsername();
		}
		return null;
	}
}
