import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class createTable {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        Statement stmt = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url, "system", "1234");
            stmt = con.createStatement();
            stmt.executeQuery("create table rohit.teacher(teacher_id number(6) primary key , name varchar2(30) not null, password varchar2(30) not null, phone_no number(10) unique not null)");
            stmt.executeQuery("create table rohit.course(discipline varchar2(30) primary key not null, primary_owner number(10), secondary_owner number(10), max_limit number(3))");
            stmt.executeQuery("create table rohit.student_details(Roll_no number(10),name varchar2(20), email varchar2(20),phone_no number (10),User_ID varchar2(6),password varchar2(20))");
            stmt.executeQuery("create table rohit.student_course_details(Roll_no number(10),name varchar2(20), discipline varchar2(20),primary_owner number(5),secondary_owner number (5),Marks number(4))");
            System.out.println("Tables Created Successfully");
        } catch (SQLException e) {
            e.getMessage();
            System.out.println("Connection Not Established");
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
