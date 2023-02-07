package cn.letao.online_grocery_store.pojo;

public class BuyerVo {
    private String  username;
    private String  password;
    private String  phonenumber;

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

    public BuyerVo(String username, String password, String phonenumber) {
        this.username = username;
        this.password = password;
        this.phonenumber = phonenumber;
    }
}
