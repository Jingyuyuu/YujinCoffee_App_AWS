package app.myproject.yujincoffee_app.Model.Product;

public class StoreListModel {

    private String storename;
    private String address;
    private String tel;
    private String businesshour;

    public StoreListModel(String storename, String address, String tel, String businesshour) {
        this.storename = storename;
        this.address = address;
        this.tel = tel;
        this.businesshour = businesshour;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getBusinesshour() {
        return businesshour;
    }

    public void setBusinesshour(String businesshour) {
        this.businesshour = businesshour;
    }
}
