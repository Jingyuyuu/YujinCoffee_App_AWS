package app.myproject.yujincoffee_app.Model.Product;

public class DrinkModel {
    int id;
    int series;
    String txtName;
    int tem;
    int txtCalorie;
    int txtPrice;
    int image;
    private final static String txt1="價錢";
    private final static String txt2="熱量";

    public DrinkModel(int id,int series,String txtName,int tem,int txtCalorie,int txtPrice,int image) {
        this.id=id;
        this.series=series;
        this.txtName = txtName;
        this.tem=tem;
        this.txtCalorie = txtCalorie;
        this.txtPrice = txtPrice;
        this.image = image;


    }
    public String getTxt1(){
        return txt1;
    }
    public String getTxt2(){
        return txt2;
    }
    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public int getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(int txtPrice) {
        this.txtPrice = txtPrice;
    }

    public int getTxtCalorie() {
        return txtCalorie;
    }

    public void setTxtCalorie(int txtCalorie) {
        this.txtCalorie = txtCalorie;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getTem() {
        return tem;
    }

    public void setTem(int tem) {
        this.tem = tem;
    }
}
