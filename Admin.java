package com.assignment.management;

import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Admin  {
    Scanner sc= new Scanner(System.in);
    DatabaseConnection db = new DatabaseConnection();

    Student student=new Student();
    Statement stmt= db.startingDB();
    private String userName = "Rohit";
    private String userPassword ="123456";

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    boolean login(String Username, String  Password) {
        return Username.equals(userName) && userPassword.equals(Password);
    }

    void addCourse(String discipline, int primaryOwner, int secondaryOwner, int maxLimit) throws Exception {
        try {
            stmt.executeQuery("Insert into Rohit.COURSE Values('" + discipline + "'," + primaryOwner + "," + secondaryOwner + "," + maxLimit + ")");
            System.out.println("data added successfully");
        }
        catch(SQLIntegrityConstraintViolationException e){
            System.out.println("Course Already present");
        }
    }

    public void loginPage() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------------------------------------");
        System.out.println("Login Page");
        System.out.println();
        System.out.println("Enter Username:");
        String userName = sc.nextLine();
        System.out.println("Enter Password:");
        String password = sc.nextLine();
        boolean result = login(userName, password);
        if (!result) {
            System.out.println("Wrong UserName or Password");
            loginPage();
        } else {
            System.out.println("Login Successfull!!");
            adminDashboard();
        }
    }
    void adminDashboard() throws Exception {

        System.out.println("------------------------------------------------");
        System.out.println("Your Dashboard");
        System.out.println();
        System.out.println("Choose Option");
        System.out.println("Enter 1 for Add Course");
        System.out.println("Enter 2 for view Highest and Lowest Marks of a Course");
        System.out.println("Enter 3 for Create/Update/Delete a Teacher");
        System.out.println("Enter 4 for Change Password");
        System.out.println("Enter 5 for Log out");
        System.out.println("------------------------------------------------");
        int input = sc.nextInt();
        sc.nextLine();

        switch (input) {
            case 1:
//                    / add a course function
                    addCourse();
                    adminDashboard();
                    break;
            case 2:
                // show high and low marks
                    showMarks();
                    adminDashboard();
                    break;
            case 3:
                    System.out.println("Enter 1 for Create a Teacher Profile");
                    System.out.println("Enter 2 for Update a Teacher Profile");
                    System.out.println("Enter 3 for Delete a Teacher Profile");
                    int teachInput = sc.nextInt();
                    switch (teachInput) {
                            case 1:
                                //create a teacher profile
                                createTeacher();
                                adminDashboard();
                                break;
                            case 2:
                                //update  a teacher profile
                                updateTeacher();
                                adminDashboard();
                                break;
                            case 3:
                                //delete  a teacher profile
                                deleteTeacher();
                                adminDashboard();
                                break;
                            }
                            break;
            case 4:
                    //change password
                    changePassword();
                    loginPage();
                    break;
            case 5:
                System.out.println("Log Out Successfully");
                break;
            default:
                    System.out.println("!!press Valid input");
                    loginPage();
        }
    }

    private void addCourse() throws Exception{
        System.out.println("Enter Discipline/Course Name");
        String discipline = sc.nextLine();
        System.out.println("Enter Primary Owner ID");
        int primaryOwner = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Secondary Owner ID");
        int secondaryOwner = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Max Limit of Course");
        int maxLimit = sc.nextInt();
        sc.nextLine();
        String course=discipline.toUpperCase();
       //validate p or s owner is present in teacher table or not
            if(isTeacherPresent(primaryOwner, secondaryOwner)) {
                addCourse(course, primaryOwner, secondaryOwner, maxLimit);
            }else{
                System.out.println("Teacher is not present! , First Register Teacher");
            }
    }

    private boolean isTeacherPresent(int pOwner,int sOwner) throws Exception{
        boolean flag1=false;
        boolean flag2=false;
        ResultSet res=stmt.executeQuery("select teacher_id from rohit.teacher where teacher_id="+pOwner);
        while(res.next()){
            flag1= true;
        }
        res=stmt.executeQuery("select teacher_id from rohit.teacher where teacher_id="+sOwner);
        while(res.next()){
            flag2= true;
        }
        return flag1 && flag2;


    }

    private void showMarks() throws Exception{
        System.out.println("Enter Course name Which Highest and Lowest marks,You want to See");
        System.out.println("List of All Course");
        student.showAllCourse();
        String course= sc.nextLine();
        course= course.toUpperCase();
        int high=0,low=0;
        ResultSet res= stmt.executeQuery("select MAX(marks) from rohit.student_course_details where discipline ='"+course+"'");
        while (res.next()){
            high= res.getInt("max(marks)");
        }
        res= stmt.executeQuery("select min(marks) from rohit.student_course_details where discipline ='"+course+"'");
        while(res.next()){
            low= res.getInt("min(marks)");
        }
        System.out.println("Highest marks of the course :"+high);
        System.out.println("Lowest marks of the course :"+low);

    }

    private void createTeacher() throws Exception {
       Scanner sc = new Scanner(System.in);
        int id = 1001;
        System.out.println("Enter Teacher Details");
        System.out.println("Enter Teacher name ");
        String name = sc.nextLine();
        System.out.println("Enter teacher password");
        String password =sc.nextLine();
        System.out.println("Enter Phone Number");
        long phone_no= sc.nextLong();
        sc.nextLine();
        ResultSet res = stmt.executeQuery("Select MAX(teacher_id) from rohit.teacher");
        while (res.next()) {
            id = res.getInt("MAX(teacher_id)") + 1;
            if (id == 1) id = 1001;
        }

        if (isValidPhone(phone_no)) {
            try {
                stmt.executeQuery("insert into rohit.teacher values(" + id + ",'" + name + "','" + password + "'," + phone_no + ")");
                System.out.println("Teacher Added Successfully");
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("This Teacher is already present or phone no is already associated with another teacher");
            }
        } else {
            System.out.println("Not a Valid Phone Number");
            createTeacher();
        }

    }

    private void updateTeacher() throws Exception {
        Scanner sc= new Scanner(System.in);
            System.out.println("Enter your teacher id: ");
            int id= sc.nextInt();
            sc.nextLine();
            boolean flag=checkId(id);
            if(flag){
            System.out.println("Enter field You want to Change");
            System.out.println("Enter 1 for Teacher Name, 2 for Password, 3 for Phone Number");
            int input= sc.nextInt();
            sc.nextLine();
            System.out.println("Enter your Update Data:");
            String data= sc.nextLine();
            if(input==1){
                stmt.executeQuery("update rohit.teacher set teacher_name='"+data+"' where teacher_id="+id);
                System.out.println("Data updated Successfully");
            } else if (input==2) {
                stmt.executeQuery("update rohit.teacher set password='"+data+"' where teacher_id="+id);
                System.out.println("Data updated Successfully");
            }else if (input==3) {
                stmt.executeQuery("update rohit.teacher set phone_no="+data+" where teacher_id="+id);
                System.out.println("Data updated Successfully");
            }else{
                System.out.println("Enter Valid ID");
            }
        }else{
            System.out.println("No teacher Present with this id, Enter valid id");
            updateTeacher();
        }
    }

    private void deleteTeacher() throws Exception{
        Scanner sc= new Scanner(System.in);
            System.out.println("Enter Teacher ID to Delete");
            int input = sc.nextInt();
            boolean flag=checkId(input);
            if (flag) {
                stmt.executeQuery("update rohit.course set primary_owner= null where primary_owner=" + input);
                stmt.executeQuery("update rohit.course set secondary_owner = null where secondary_owner=" + input);
                stmt.executeQuery("delete from rohit.teacher where teacher_id=" + input);
                System.out.println("deleted Successfully");
            }
            else{
                System.out.println("No Teacher ID Present,Enter Valid ID");
                deleteTeacher();
            }
    }
    boolean checkId(int id) throws Exception {
        int realPass = 0;
        ResultSet res = stmt.executeQuery("select teacher_id from Rohit.teacher where teacher_id=" + id);
        while (res.next()) {
            realPass = res.getInt("teacher_id");
            break;
        }
        return realPass == id;
    }

    void changePassword(){
        System.out.println("Enter old Password:");
        String oldPass= sc.nextLine();
        System.out.println("Enter New Password:");
        String newPass= sc.nextLine();
        if(oldPass.equals(getUserPassword())){
            setUserPassword(newPass);
            System.out.println("Password Changed");
        }else{
            System.out.println("Old Password is Wrong");
        }
    }

    boolean isValidPhone(long phoneNo){
        String number= Long.toString(phoneNo);
        Pattern p = Pattern.compile("(0|91)?[7-9][0-9]{9}");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression
        Matcher m = p.matcher(number);
        return (m.find() && m.group().equals(number));
    }

}