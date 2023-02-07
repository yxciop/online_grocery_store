package cn.letao.online_grocery_store.pojo;

public class Role {
    private Integer id;
    private String  identify;
    private String  access_to_the_address;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getAccess_to_the_address() {
        return access_to_the_address;
    }

    public void setAccess_to_the_address(String access_to_the_address) {
        this.access_to_the_address = access_to_the_address;
    }
}
