package app.myproject.yujincoffee_app.Model.Product;

public class ProductModel {
    private  int _id;
    private String name;
    private String sugar;
    private String ice;
    private int amount;
    private int dollar;
    private int tem;

    public ProductModel(int _id,String name,int tem ,String sugar, String ice, int amount, int dollar) {
        this._id=_id;
        this.name = name;
        this.tem=tem;
        this.sugar = sugar;
        this.ice = ice;
        this.amount = amount;
        this.dollar = dollar;
    }

    public  ProductModel(int _id,String name,int tem,int amount,int dollar){
        this._id=_id;
        this.name=name;
        this.tem=tem;
        this.amount=amount;
        this.dollar=dollar;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getTem() {
        return tem;
    }

    public void setTem(int tem) {
        this.tem = tem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDollar() {
        return dollar;
    }

    public void setDollar(int dollar) {
        this.dollar = dollar;
    }
}
