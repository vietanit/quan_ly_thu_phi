package models;

/**
 * Lớp để lưu thông tin session của user đang đăng nhập
 */
public class UserSession {
	private static UserSession instance;
	private String cmnd;
	private boolean isAdmin;
	
	private UserSession() {}
	
	public static UserSession getInstance() {
		if (instance == null) {
			instance = new UserSession();
		}
		return instance;
	}
	
	public void setUserInfo(String cmnd, boolean isAdmin) {
		this.cmnd = cmnd;
		this.isAdmin = isAdmin;
	}
	
	public String getCmnd() {
		return cmnd;
	}
	
	public void setCmnd(String cmnd) {
		this.cmnd = cmnd;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}
	
	public void clear() {
		cmnd = null;
		isAdmin = false;
	}
}
