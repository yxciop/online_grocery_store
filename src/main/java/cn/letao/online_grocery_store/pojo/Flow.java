package cn.letao.online_grocery_store.pojo;

import java.util.Date;

public class Flow {
    private String id;
    private String  trade_no;
    private String  out_trade_no;
    private Double  paid_amount;
    private Date created_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Double getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(Double paid_amount) {
        this.paid_amount = paid_amount;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }

    @Override
    public String toString() {
        return "Flow{" +
                "id='" + id + '\'' +
                ", trade_no='" + trade_no + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", paid_amount=" + paid_amount +
                ", created_time=" + created_time +
                '}';
    }
}
