package com.assignment.management;

import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Student {
    DatabaseConnection db = new DatabaseConnection();
    Statement stmt= db.startingDB();
    Scanner sc = new Scanner(System.in);
    String userEmail;

    public void studentPage() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Press 1 for Login");
        System.out.println("Press 2 for Registeration");
        int stInput = sc.nextInt();
        switch (stInput) {
            case 1:
                System.out.println("Enter Your Student Credentials");
                loginPage();
                break;
            case 2:
                System.out.println("Enter Your Details for Registeration");
                registrationPage();
                studentPage();
                break;

        }

    }

    private void loginPage() throws Exception {
        System.out.println("------------------------------------------------");
        System.out.println("Login Page");
        System.out.println();
        System.out.println("Enter UserEmail or UserID:");
        userEmail = sc.nextLine();
        System.out.println("Enter Password:");
        String password = sc.nextLine();
        if (login(userEmail, password)) {
            studentDashboard();

        } else {
            System.out.println("Wrong id/password");
            loginPage();
        }
    }

    boolean login(String UserEmail, String Password) throws Exception {
        String realPass = "";
        ResultSet res = stmt.executeQuery("select password from Rohit.student_details where email='" + UserEmail + "' or user_ID='" + UserEmail + "'");
        while (res.next()) {
            realPass = res.getString(1);
            break;
        }
        return realPass.equals(Password);
    }

    private void registrationPage() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Roll No.:");
        int rollNo = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter your name:");
        String name = sc.nextLine();
        System.out.println("Enter your Email id:");
        String email = sc.nextLine();
        System.out.println("Enter your Password:");
        String password = sc.nextLine();
        if (isValidEmail(email)) {
            try{
                stmt.executeQuery("Insert into Rohit.student_details Values(" + rollNo + ",'" + name + "','" + email + "','" + generateUID(name, rollNo) + "','" + password + "')");
                System.out.println("Register Successfully");
                System.out.println("Your UserID for LOGIN : " + generateUID(name, rollNo));
            }
            catch(SQLIntegrityConstraintViolationException e){
                System.out.println("Student Already Registered");
            }
        }
        else{
            System.out.println("INValid Email Address");
        }

}

     private String generateUID(String name, int rollNo){
        String rNo= String.valueOf(rollNo);
         return name.substring(0,3).concat(rNo.substring(rNo.length()-3,rNo.length()));
     }

     void studentDashboard() throws Exception {
         Scanner sc = new Scanner(System.in);
         System.out.println("------------------------------------------");
         System.out.println("Welcome to Your Dashboard");
         System.out.println("Enter 1 for show All course");
         System.out.println("Enter 2 for Enroll in a Course");
         System.out.println("Enter 3 for leave out a Course");
         System.out.println("Enter 4 for View Marks");
         System.out.println("Enter 5 for Log Out");

         int input = sc.nextInt();
         switch (input) {
             case 1:
                 System.out.println("list of all course");
                 showAllCourse();
                 studentDashboard();
                 break;
             case 2:
                 courseRegisteration();
                 studentDashboard();
                 break;
             case 3:
                    leaveCourse();
                 studentDashboard();
                 break;
             case 4:
                 //view marks
                 viewMarks();
                 studentDashboard();
                 break;
             case 5:
                 System.out.println("Log Out Successfull");
                 break;
         }
     }

    private void leaveCourse() throws Exception {
        int rollNo= getRollNo(userEmail);
        System.out.println("Enter Course Name Which You Want to Leave:");
        showStudentCourse(rollNo);
        String courseName=sc.nextLine();
        courseName=courseName.toUpperCase();
            stmt.executeQuery("delete from rohit.student_course_details where roll_no=" + rollNo + "and discipline='" + courseName + "'");
        System.out.println("leave course successfully");
    }

    private void viewMarks() throws Exception {
        int marks = 0;
        int rollNo = getRollNo(userEmail);
        System.out.println("Course you have registered");
        showStudentCourse(rollNo);
        System.out.println("Enter course name which marks you want to see:");
        String courseName = sc.nextLine();
        courseName=courseName.toUpperCase();
        ResultSet res = stmt.executeQuery("select marks from rohit.student_course_details where roll_no=" + rollNo + " and discipline='" + courseName + "'");
        while (res.next()) {
            marks = res.getInt("marks");
        }
        if (marks == 0) {
            System.out.println("Your marks in " + courseName + " are : " + marks + " or Marks not Updated Yet!! Confirm With Your Teacher");
        } else {
            System.out.println("Your marks in " + courseName + " are : " + marks);
        }
    }

    private void showStudentCourse(int rollNo) throws  Exception{
        List<String> courses= new ArrayList<>();
        ResultSet rs=stmt.executeQuery("Select discipline from Rohit.student_course_details where roll_no="+rollNo);
        while (rs.next()){
            courses.add(rs.getString(1));
        }
        for (String cours : courses) {
            System.out.println(cours);
        }
    }

    void courseRegisteration() throws Exception{
             int rollNo=0;
             String name="";
        ResultSet res= stmt.executeQuery("select roll_no,name from rohit.student_details where user_id='"+userEmail+ "'or email='"+userEmail+"'");
            while (res.next()){
                rollNo=res.getInt("roll_no");
                name=res.getString("name");
            }
            System.out.println("list of all courses");
            showAllCourse();
             System.out.println("Enter your Course Name which you want to enroll");
             String discipline= sc.nextLine();
             if(courseCount(rollNo, discipline)) {
                 addCourse(rollNo, name, discipline, 0);
             }else{
                studentDashboard();
             }
         }

    private boolean courseCount(int rollNo,String discipline) throws Exception {
        discipline = discipline.toUpperCase();
        boolean flag = true;
        int courseLimit = 5, courseEnroll = 0, max_limit = 0, enrollNo = 0;
        ResultSet res = stmt.executeQuery("select count(roll_No)from rohit.student_course_details where roll_No =" + rollNo);
        while (res.next()) {
            courseEnroll = res.getInt("count(roll_no)");
        }
        res = stmt.executeQuery("select max_limit from rohit.course where discipline='" + discipline + "'");
        while (res.next()) {
            max_limit = res.getInt("max_limit");
        }
        res = stmt.executeQuery("select count(discipline) from rohit.student_course_details where discipline='" + discipline + "'");
        while (res.next()) {
            enrollNo = res.getInt("count(discipline)");
        }
        res = stmt.executeQuery("select roll_no from rohit.student_course_details where discipline='" + discipline + "' and roll_no=" + rollNo);
        while (res.next()) {
            flag = false;
        }

        if (!flag) {
            System.out.println("You Already Enrolled in Course");
            return false;
        } else if (max_limit == enrollNo) {
            System.out.println("Course achieved max_limit");
            return false;
        } else if (courseLimit == courseEnroll) {
            System.out.println("You Already enrolled in 5 Courses");
            return false;
        } else {
            return true;
        }
    }

    private void addCourse(int rollNo,String name,String discipline,int marks ) throws Exception {
        discipline=discipline.toUpperCase();
         String first="", second="";
         ResultSet res = stmt.executeQuery("select PRIMARY_OWNER,SECONDARY_OWNER from Rohit.course where discipline='" + discipline + "'");
         while (res.next()) {
             first=res.getString(1);
             second=res.getString(2);
             break;
         }
         stmt.executeQuery("Insert into Rohit.student_course_details Values(" + rollNo + ",'" + name + "','" + discipline + "','" + first + "','" + second + "'," + marks + ")");
         System.out.println("Registered With Course Successfully");
     }

     void showAllCourse() throws Exception{
         ResultSet rs=stmt.executeQuery("Select discipline from Rohit.course");
         while (rs.next()){
             System.out.println(rs.getString(1));
         }

     }

     boolean isValidEmail(String email){
         String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                 "[a-zA-Z0-9_+&*-]+)*@" +
                 "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                 "A-Z]{2,7}$";

         Pattern pat = Pattern.compile(emailRegex);
         if (email == null)
             return false;
         return pat.matcher(email).matches();
     }

     int getRollNo(String ID) throws Exception{
        ResultSet res= stmt.executeQuery("Select roll_no from rohit.student_details where email='"+ID+ "' or user_id='"+ID+"'");
        while(res.next()){
            return res.getInt("roll_no");
        }
        return 0;
     }
}
