package com.iispl.service;

import java.time.LocalDate;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import com.iispl.dao.UserDao;
import com.iispl.dto.SessionUserDTO;
import com.iispl.entity.User;
import com.iispl.exceptions.AuthException;
public class LoginService {
	private UserDao userDao;
	
	public LoginService() {
		this.userDao = new UserDao();
	}
	
	public SessionUserDTO authenticate(String username, String plainPassword, String selectedRole) {
		if(isBlank(username) || isBlank(plainPassword) || isBlank(selectedRole)) {
			throw new AuthException("All fields are mandatory");
		}
		
		Optional<User> opt = userDao.findActiveByUsername(username.trim());
		
		if(opt.isEmpty()) {
			throw new AuthException("Invalid username or password");
		}else {
			User user= opt.get();
			boolean passwordMatches = BCrypt.checkpw(plainPassword, user.getPasswordHash());
			
			if(!passwordMatches) {
				throw new AuthException("Invalid username or password");	
			}
			
			String dbRoleName = (user.getRole() != null) ? user.getRole().getRoleName() : "";
			
			if(!dbRoleName.equalsIgnoreCase(selectedRole.trim())) {
				throw new AuthException(
	                    "Selected role does not match your assigned role.");
			}
			
			user.setLastLoginAt(LocalDate.now());
			
			return new SessionUserDTO(
	                user.getId(),
	                user.getUsername(),
	                user.getFullName(),
	                user.getBranch(),
	                user.getRole().getId(),
	                user.getRole().getRoleName(),
	                toDisplayName(
	                        user.getRole().getRoleName())
	        );
		}
		
		
	}
	 private boolean isBlank(String value) {

	        return value == null
	                || value.trim().isEmpty();
	    }
	 
	 private String toDisplayName(String roleName) {

	        if (roleName == null) {
	            return "";
	        }

	        switch (roleName.toUpperCase()) {

	            case "ADMIN":
	                return "Admin";

	            case "MAKER_OUTWARD":
	                return "Maker Outward";

	            case "CHECKER_OUTWARD":
	                return "Checker Outward";

	            case "MAKER_INWARD":
	                return "Maker Inward";

	            case "CHECKER_INWARD":
	                return "Checker Inward";

	            default:
	                return roleName;
	        }
	    }
}
