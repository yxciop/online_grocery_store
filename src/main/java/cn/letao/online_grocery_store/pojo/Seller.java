package cn.letao.online_grocery_store.pojo;

public class Seller {
	private Integer id;
	private String  username;
	private String  password;
	private String  phonenumber;
	private String  address;
	private String  realname;
	private String  imgPath;
	private String  imgPhysicalPath;
	private String  store_name;
    private String  loginStatus;
    private String  status;
    private Integer roleId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgPhysicalPath() {
		return imgPhysicalPath;
	}

	public void setImgPhysicalPath(String imgPhysicalPath) {
		this.imgPhysicalPath = imgPhysicalPath;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Seller(String username, String phonenumber, String store_name) {
		this.username = username;
		this.phonenumber = phonenumber;
		this.store_name = store_name;
	}
	public Seller(){

	}
}
