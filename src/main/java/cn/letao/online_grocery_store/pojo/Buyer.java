package cn.letao.online_grocery_store.pojo;

public class Buyer  {
	private Integer id;
	private String  username;
	private String  password;
	private String  phonenumber;
	private String  nickname;
	private String  imgPath;
	private Integer roleId;
	private String  imgPhysicalPath;
	private String  loginStatus;
	private String  status;
	private String  realname;
	private String  msg;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getImgPhysicalPath() {
		return imgPhysicalPath;
	}

	public void setImgPhysicalPath(String imgPhysicalPath) {
		this.imgPhysicalPath = imgPhysicalPath;
	}

	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Buyer{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", phonenumber='" + phonenumber + '\'' +
				", nickname='" + nickname + '\'' +
				", imgPath='" + imgPath + '\'' +
				", roleId=" + roleId +
				", imgPhysicalPath='" + imgPhysicalPath + '\'' +
				", loginStatus='" + loginStatus + '\'' +
				", status='" + status + '\'' +
				", realname='" + realname + '\'' +
				", msg='" + msg + '\'' +
				'}';
	}
}
