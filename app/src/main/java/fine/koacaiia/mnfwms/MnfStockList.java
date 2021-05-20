package fine.koacaiia.mnfwms;

public class MnfStockList {
    String totalDate;
    String totalPlt;
    String totalCbm;
    int untilDate;

    public MnfStockList(){

    }
    public MnfStockList(String totalDate,String totalPlt,String totalCbm,int untilDate){
        this.totalDate=totalDate;
        this.totalPlt=totalPlt;
        this.totalCbm=totalCbm;
        this.untilDate=untilDate;
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

    public int getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(int untilDate) {
        this.untilDate = untilDate;
    }
}
