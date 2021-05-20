package fine.koacaiia.mnfwms;

public class MnfRemarkList {

    String date;
    String bl;
    String des;
    String count;
    String remark;

    public MnfRemarkList(){
            }

    public MnfRemarkList(String date, String bl, String des, String count, String remark) {
        this.date = date;
        this.bl = bl;
        this.des = des;
        this.count = count;
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBl() {
        return bl;
    }

    public void setBl(String bl) {
        this.bl = bl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
