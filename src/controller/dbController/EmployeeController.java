package controller.dbController;

import db.DbConnection;
import model.Employee;
import model.Room;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeController {
    public String createEmployeeId() throws SQLException {
        ResultSet rst = DbConnection.getInstance()
                .getConnection().prepareStatement(
                        "SELECT employeeId FROM Employee ORDER BY employeeId DESC LIMIT 1"
                ).executeQuery();
        if (rst.next()){

            int tempId = Integer.
                    parseInt(rst.getString(1).split("-")[1]);
            tempId=tempId+1;
            if (tempId<=9){
                return "E-00"+tempId;
            }else if(tempId<=99){
                return "E-0"+tempId;
            }else{
                return "E-"+tempId;
            }

        }else{
            return "E-001";
        }
    }

    public ArrayList<String> getDepartmentName() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Department").executeQuery();

        ArrayList<String> departmentName=new ArrayList();
        while(resultSet.next()) {
            departmentName.add(
                    resultSet.getString(2)
            );
        }
        return departmentName;

    }

    public String getDepartmentId(String departmentName) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT departmentId FROM Department WHERE DepartmentName=?");
        stm.setObject(1,departmentName);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return resultSet.getString(1);
        }else{
            return null;
        }
    }

    public boolean saveEmployeeDetails(Employee employee) throws SQLException {

        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("INSERT INTO Employee VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        stm.setObject(1,employee.getEmployeeId());
        stm.setObject(2,employee.getDepartmentId());
        stm.setObject(3,employee.getEmployeeName());
        stm.setObject(4,employee.getDob());
        stm.setObject(5,employee.getNic());
        stm.setObject(6,employee.getAddress());
        stm.setObject(7,employee.getNumber());
        stm.setObject(8,employee.getEmail());
        stm.setObject(9,employee.getSalary());
        stm.setObject(10,employee.getJoinDate());
        stm.setObject(11,employee.getGender());

        return stm.executeUpdate()>0;
    }

    public ArrayList<Employee> getEmployeeDetails() throws SQLException {
        ResultSet resultSet = DbConnection.getInstance().getConnection().prepareStatement("SELECT Employee.employeeId, Department.departmentName,Employee.employeeName FROM Employee LEFT JOIN Department ON Employee.departmentId = Department.departmentId ORDER BY Employee.employeeId;").executeQuery();

        ArrayList<Employee> employeeList = new ArrayList();
        while(resultSet.next()){
            employeeList.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return employeeList;
    }

    public boolean deleteEmployeeDetails(String employeeId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("DELETE FROM Employee WHERE EmployeeId=?");
        stm.setObject(1,employeeId);
        return stm.executeUpdate()>0;
    }

    public boolean updateEmployeeDetails(Employee employee) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("UPDATE Employee SET departmentId=?,employeeName=?," +
                "dob=?,nic=?,address=?,number=?,email=?,salary=?,joinDate=?,gender=? WHERE employeeID=?");
        stm.setObject(1,employee.getDepartmentId());
        stm.setObject(2,employee.getEmployeeName());
        stm.setObject(3,employee.getDob());
        stm.setObject(4,employee.getNic());
        stm.setObject(5,employee.getAddress());
        stm.setObject(6,employee.getNumber());
        stm.setObject(7,employee.getEmail());
        stm.setObject(8,employee.getSalary());
        stm.setObject(9,employee.getJoinDate());
        stm.setObject(10,employee.getGender());
        stm.setObject(11,employee.getEmployeeId());

        return stm.executeUpdate()>0;
    }

    public Employee getEmployeeRecord(String employeeId) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("SELECT * FROM Employee WHERE employeeId=?");
        stm.setObject(1,employeeId);
        ResultSet resultSet = stm.executeQuery();
        if(resultSet.next()){
            return new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getBigDecimal(9),
                    resultSet.getString(10),
                    resultSet.getString(11)
            );
        }else{
            return null;
        }
    }

    public ArrayList<Employee> searchEmployeeDetails(String employeeName) throws SQLException {
        PreparedStatement stm = DbConnection.getInstance().getConnection().prepareStatement("\n" +
                "SELECT Employee.employeeId, Department.departmentName,Employee.employeeName\n" +
                " FROM Employee\n" +
                " JOIN Department ON Employee.departmentId = Department.departmentId AND employeeName LIKE '"+employeeName+"%' ORDER BY Employee.employeeId;");
        ResultSet resultSet = stm.executeQuery();

        ArrayList<Employee>employeeList = new ArrayList();
        while(resultSet.next()){
            employeeList.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            ));
        }
        return employeeList;
    }



}
