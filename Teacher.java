package com.assignment.management;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Teacher {
    Scanner sc= new Scanner(System.in);
    DatabaseConnection db = new DatabaseConnection();
    Statement stmt= db.startingDB();
    Student stud= new Student();

    public void loginPage() throws Exception {
//        Scanner sc = new Scanner(System.in);
        System.out.println("------------------------------------------------");
        System.out.println("Login Page");
        System.out.println();
        System.out.println("Enter Your ID:");
        int userId = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Password:");
        String password = sc.nextLine();
        boolean result = login(userId, password);
        if (!result) {
            System.out.println("Wrong UserID/Password");
            loginPage();
        } else {
            System.out.println("Login Successfull");
            teacherDashboard(userId);
        }
    }

    private void teacherDashboard(int userId) throws Exception {
        boolean correctOperation;
        do {
            correctOperation = true;
            System.out.println("Enter 1 to See Highest and Lowest Scorer in Course");
            System.out.println("Enter 2 for Enter Marks in Course");
            System.out.println("Enter 3 for Log Out");
            int input = sc.nextInt();
            sc.nextLine();
            switch (input) {
                case 1:
                    showScorer(userId);
                    teacherDashboard(userId);
                    break;
                case 2:
                    entryMarks(userId);
                    teacherDashboard(userId);
                    break;
                case 3:
                    System.out.println("Log Out Successfully");
                    break;
                default:
                    System.out.println("Please!! Enter Valid Input");
                    correctOperation = false;
                    input = sc.nextInt();
                    sc.nextLine();
            }

        } while (!correctOperation);

    }
    private void entryMarks(int userId) throws Exception {
        System.out.println("List of all courses:");
        stud.showAllCourse();
        System.out.println();
        System.out.println("Enter Your Course");
        String course = sc.nextLine();
        if(checkCourseValiation(course, userId)) {
            System.out.println("Enter Student Roll no.:");
            int roll = sc.nextInt();
            sc.nextLine();
            if (isEnroll(roll, course)) {
                course=course.toUpperCase();
                System.out.println("Enter Marks");
                int marks = sc.nextInt();
                sc.nextLine();
                stmt.executeQuery("update rohit.student_course_details set marks=" + marks + " where roll_no=" + roll + " and discipline='" + course + "'");
                System.out.println("marks added/updated successfully");
            } else {
                System.out.println("Student is not Enrolled in Your Course");
            }
        }
        else{
            System.out.println("You are not the Owner of the Course, Please put Valid Course Name");
            entryMarks(userId);
        }


    }

    private boolean isEnroll(int roll,String course) throws Exception{
        course=course.toUpperCase();
        ResultSet res=stmt.executeQuery("Select roll_no from Rohit.student_course_details where discipline='"+course+"' and roll_no="+roll);
        while(res.next()){
           if(roll==res.getInt("roll_no"))
               return true;
        }
        return  false;
    }

    private void showScorer(int userId) throws Exception {
        System.out.println("List of All Courses:");
        stud.showAllCourse();
            System.out.println();
            System.out.println("Enter Course Name:- ");
            String course = sc.nextLine();
            course = course.toUpperCase();
            if (checkCourseValiation(course, userId)) {
                scoreName(course);
            } else {
                System.out.println("You are not the Owner of the Course");
                showScorer(userId);
        }
    }

    private void scoreName(String course) throws Exception {
        String highScorer="";
        String lowScorer="";
        course=course.toUpperCase();
        ResultSet res= stmt.executeQuery("select name from rohit.student_course_details where marks=(select max(marks)from rohit.student_course_details where discipline='"+course+"')");
        while( res.next()){
            highScorer=res.getString("name");
            System.out.println("Highest scorer in your Course: "+highScorer);
        }
        res=stmt.executeQuery("select name from rohit.student_course_details where marks=(select min(marks)from rohit.student_course_details where discipline='"+course+"')");
        while( res.next()){
            lowScorer=res.getString("name");
            System.out.println("Lowest scorer in your Course: "+lowScorer);
        }
        if(highScorer.equals("") && lowScorer.equals("")){
            highScorer="Not Assigned";
            lowScorer="Not Assignned";
            System.out.println("Highest scorer in your Course: "+highScorer);
            System.out.println("Lowest scorer in your Course: "+lowScorer);
        }
    }

    private boolean checkCourseValiation(String course,int userId) throws Exception {
        course=course.toUpperCase();
        ResultSet res=stmt.executeQuery("select discipline from rohit.course where primary_owner="+userId+"or secondary_owner="+userId );
        while (res.next()){
            if(course.equals(res.getString("discipline")))return true;
        }
        return false;
    }

    boolean login(int UserID,String Password) throws Exception {
        String realPass="";
        ResultSet res = stmt.executeQuery("select password from Rohit.teacher where teacher_id=" + UserID );
        while (res.next()) {
            realPass=res.getString(1);
            break;
        }
        return realPass.equals(Password);
    }
}
