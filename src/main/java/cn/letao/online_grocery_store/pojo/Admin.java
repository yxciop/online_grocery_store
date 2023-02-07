package cn.letao.online_grocery_store.pojo;

public class Admin {
    private Integer id;
    private String  username;
    private String  password;
    private String  loginStatus;
    private Integer roleId;
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

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Admin(){

    }
}
