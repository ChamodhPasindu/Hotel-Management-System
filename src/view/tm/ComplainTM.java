package view.tm;

public class ComplainTM {
    private String complainId;
    private String cusId;
    private String description;
    private String time;
    private String date;
    private String status;



    public ComplainTM(String cusId, String description, String date, String time) {
        this.cusId = cusId;
        this.description = description;
        this.time = time;
        this.date = date;
    }

    public ComplainTM(String complainId, String cusId , String time,String date, String description) {
        this.complainId = complainId;
        this.cusId = cusId;
        this.description = description;
        this.time = time;
        this.date = date;
    }


    public ComplainTM(String complainId, String cusId, String description, String time, String date, String status) {
        this.setComplainId(complainId);
        this.setCusId(cusId);
        this.setDescription(description);
        this.setTime(time);
        this.setDate(date);
        this.setStatus(status);
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

}
