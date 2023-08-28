package com.assignment.management;


import java.util.Scanner;

public class homepage {

    public static void main(String[] args) throws Exception {
        Admin admin = new Admin();
        Teacher teacher= new Teacher();
        Student student= new Student();
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------------------------------------");
        System.out.println("Welcome to Student & Course Management System");
        System.out.println("Select Type Of User");
        System.out.println("Enter 1 for Admin");
        System.out.println("Enter 2 for Teacher");
        System.out.println("Enter 3 for Student");
        System.out.println("Enter 4 for Exit");
        System.out.println("------------------------------------------------");
        int input = sc.nextInt();
        boolean correctOperation =true;
        do{
            correctOperation = true;
        switch (input) {
            case 1:
                System.out.println("Enter Your Admin Credentials");
                admin.loginPage();
                break;
            case 2:
                System.out.println("Enter Your Teacher Credentials");
                teacher.loginPage();
                break;
            case 3:
                System.out.println("Select Option for Student");
                student.studentPage();
                break;
            case 4:
                System.out.println("App Closed Successfully");
                break;
            default:
                System.out.println("Please!! Enter Valid Input");
                correctOperation=false;
                input=sc.nextInt();
                sc.nextLine();
        }
        }
        while (!correctOperation);
    }
}

