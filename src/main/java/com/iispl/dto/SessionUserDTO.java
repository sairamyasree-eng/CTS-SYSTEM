package com.iispl.dto;
import java.io.Serializable;

public class SessionUserDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public static final String SESSION_KEY = "sessionUser";
	
	private final String userId;
	private final String username;
    private final String fullName;
    private final String branch;
    private final String roleId;
    private final String roleName;       // e.g. "MAKER_OUTWARD"
    private final String roleDisplay; 
    
    
    public SessionUserDTO(
            String userId,
            String username,
            String fullName,
            String branch,
            String roleId,
            String roleName,
            String roleDisplay
    ) {
        this.userId      = userId;
        this.username    = username;
        this.fullName    = fullName;
        this.branch      = branch;
        this.roleId      = roleId;
        this.roleName    = roleName;
        this.roleDisplay = roleDisplay;
    }
    
    public String getUserId()      { return userId; }
    public String getUsername()    { return username; }
    public String getFullName()    { return fullName; }
    public String getBranch()      { return branch; }
    public String getRoleId()      { return roleId; }
    public String getRoleName()    { return roleName; }
    public String getRoleDisplay() { return roleDisplay; }
 
    @Override
    public String toString() {
        return "SessionUserDTO{username='" + username
                + "', role='" + roleName
                + "', branch='" + branch + "'}";
    }
}
