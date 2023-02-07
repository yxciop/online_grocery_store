package cn.letao.online_grocery_store.pojo;

import java.util.Date;

public class Order {
    private Integer id;
    private String  orderId;
    private Double  price;
    private String  productname;
    private Integer buyerId;
    private Integer sellerId;
    private String  phonenumber;
    private String  address;
    private String  status;
    private Date    createdTime;
    private Date    modifyTime;
    private String  recipient;
    private String  specification;
    private Integer amount;
    private String  msg;
    private Integer product_id;
    private Double  total_price;

    private String  imgPath;
    private String  store_name;
    private String  newOrderId;
    private String  buyerAccount;
    private String  sellerAccount;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getNewOrderId() {
        return newOrderId;
    }

    public void setNewOrderId(String newOrderId) {
        this.newOrderId = newOrderId;
    }

    public String getBuyerAccount() {
        return buyerAccount;
    }

    public void setBuyerAccount(String buyerAccount) {
        this.buyerAccount = buyerAccount;
    }

    public String getSellerAccount() {
        return sellerAccount;
    }

    public void setSellerAccount(String sellerAccount) {
        this.sellerAccount = sellerAccount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", price=" + price +
                ", productname='" + productname + '\'' +
                ", buyerId=" + buyerId +
                ", sellerId=" + sellerId +
                ", phonenumber='" + phonenumber + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", createdTime=" + createdTime +
                ", modifyTime=" + modifyTime +
                ", recipient='" + recipient + '\'' +
                ", specification='" + specification + '\'' +
                ", amount=" + amount +
                ", msg='" + msg + '\'' +
                ", product_id=" + product_id +
                ", total_price=" + total_price +
                ", imgPath='" + imgPath + '\'' +
                ", store_name='" + store_name + '\'' +
                ", newOrderId='" + newOrderId + '\'' +
                ", buyerAccount='" + buyerAccount + '\'' +
                ", sellerAccount='" + sellerAccount + '\'' +
                '}';
    }
}
