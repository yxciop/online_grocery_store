package cn.letao.online_grocery_store.pojo;

import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable {
    private Integer id;
    private String  title;
    private String  imgPath;
    private String  imgPhysicalPath;
    private Integer type_1;
    private Integer type_2;
    private Integer type_3;
    private Double  price;
    private Integer stock;
    private Date    createdTime;
    private Date    modifyTime;
    private Integer sellerId;
    private String  description;
    private String  status;
    private String  msg;
    private Integer soldCount;
    private String  dimension;

    private String  sort;
    private String  condition;
    private String  store;
    private String  address;
    private String  phonenumber;
    private String  sellerImg;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getType_1() {
        return type_1;
    }

    public void setType_1(Integer type_1) {
        this.type_1 = type_1;
    }

    public Integer getType_2() {
        return type_2;
    }

    public void setType_2(Integer type_2) {
        this.type_2 = type_2;
    }

    public Integer getType_3() {
        return type_3;
    }

    public void setType_3(Integer type_3) {
        this.type_3 = type_3;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSellerImg() {
        return sellerImg;
    }

    public void setSellerImg(String sellerImg) {
        this.sellerImg = sellerImg;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", imgPhysicalPath='" + imgPhysicalPath + '\'' +
                ", type_1=" + type_1 +
                ", type_2=" + type_2 +
                ", type_3=" + type_3 +
                ", price=" + price +
                ", stock=" + stock +
                ", createdTime=" + createdTime +
                ", modifyTime=" + modifyTime +
                ", sellerId=" + sellerId +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", soldCount=" + soldCount +
                ", dimension='" + dimension + '\'' +
                ", sort='" + sort + '\'' +
                ", condition='" + condition + '\'' +
                ", store='" + store + '\'' +
                ", address='" + address + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", sellerImg='" + sellerImg + '\'' +
                '}';
    }
}
