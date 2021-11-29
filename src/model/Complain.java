package model;


public class Complain {
    private String complainId;
    private String cusId;
    private String description;
    private String time;
    private String date;
    private String status;

    public Complain() {
    }

    public Complain(String complainId, String cusId,String date , String time, String description) {
        this.complainId = complainId;
        this.cusId = cusId;
        this.description = description;
        this.time = time;
        this.date = date;
    }

    public Complain(String complainId, String cusId, String description, String time, String date, String status) {
        this.complainId = complainId;
        this.cusId = cusId;
        this.description = description;
        this.time = time;
        this.date = date;
        this.status = status;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Complain{" +
                "complainId='" + complainId + '\'' +
                ", custId='" + cusId + '\'' +
                ", description='" + description + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
