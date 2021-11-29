package model;

public class Customer {
    private String cusId;
    private String cusTittle;
    private String cusName;
    private String nic;
    private String cusNumber;
    private String cusEmail;
    private String cusAddress;
    private String cusProvince;

    public Customer(String cusId, String cusTittle, String cusName, String nic, String cusNumber, String cusEmail, String cusAddress, String cusProvince) {
        this.cusId = cusId;
        this.cusTittle = cusTittle;
        this.cusName = cusName;
        this.nic = nic;
        this.cusNumber = cusNumber;
        this.cusEmail = cusEmail;
        this.cusAddress = cusAddress;
        this.cusProvince = cusProvince;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusTittle() {
        return cusTittle;
    }

    public void setCusTittle(String cusTittle) {
        this.cusTittle = cusTittle;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getCusNumber() {
        return cusNumber;
    }

    public void setCusNumber(String cusNumber) {
        this.cusNumber = cusNumber;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }

    public String getCusProvince() {
        return cusProvince;
    }

    public void setCusProvince(String cusProvince) {
        this.cusProvince = cusProvince;
    }
}
