package view.tm;

import java.math.BigDecimal;

public class EmployeeTM {
    private String employeeId;
    private String departmentId;
    private String employeeName;
    private String dob;
    private String nic;
    private String address;
    private String number;
    private String email;
    private BigDecimal salary;
    private String joinDate;
    private String gender;


    public EmployeeTM(String employeeId, String departmentId, String employeeName) {
        this.employeeId = employeeId;
        this.departmentId = departmentId;
        this.employeeName = employeeName;
    }

    public EmployeeTM(String employeeId, String departmentId, String employeeName, String dob, String nic, String address, String number, String email, BigDecimal salary, String joinDate, String gender) {
        this.employeeId = employeeId;
        this.departmentId = departmentId;
        this.employeeName = employeeName;
        this.dob = dob;
        this.nic = nic;
        this.address = address;
        this.number = number;
        this.email = email;
        this.salary = salary;
        this.joinDate = joinDate;
        this.gender = gender;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
