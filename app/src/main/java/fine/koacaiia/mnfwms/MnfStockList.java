package fine.koacaiia.mnfwms;

public class MnfStockList {
    String totalDate;
    String totalPlt;
    String totalCbm;

    public MnfStockList(){

    }
    public MnfStockList(String totalDate,String totalPlt,String totalCbm){
        this.totalDate=totalDate;
        this.totalPlt=totalPlt;
        this.totalCbm=totalCbm;
    }

    public String getTotalDate() {
        return totalDate;
    }

    public void setTotalDate(String totalDate) {
        this.totalDate = totalDate;
    }

    public String getTotalPlt() {
        return totalPlt;
    }

    public void setTotalPlt(String totalPlt) {
        this.totalPlt = totalPlt;
    }

    public String getTotalCbm() {
        return totalCbm;
    }

    public void setTotalCbm(String totalCbm) {
        this.totalCbm = totalCbm;
    }
}
