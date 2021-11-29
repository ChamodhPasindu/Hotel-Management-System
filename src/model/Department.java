package model;

public class Department {
    private String departmentId;
    private String departmentName;

    public Department() {
    }

    public Department(String departmentId, String departmentName) {
        this.setDepartmentId(departmentId);
        this.setDepartmentName(departmentName);
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId='" + departmentId + '\'' +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
