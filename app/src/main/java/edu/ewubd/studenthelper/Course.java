package edu.ewubd.studenthelper;

public class Course {
//    String key = "";
    String semester = "";
    String courseCode = "";
    String section = "";
    String credit = "";
    String room = "";
    String timeSlot = "";
//    String labRoom = "";
//    String labTimeSlot = "";
    String faculty = "";


    public Course(String semester, String courseCode, String section, String credit, String room, String timeSlot, String faculty) {
//        this.key = key;
        this.semester = semester;
        this.courseCode = courseCode;
        this.section = section;
        this.credit = credit;
        this.room = room;
        this.timeSlot = timeSlot;
//        this.labRoom = labRoom;
//        this.labTimeSlot = labTimeSlot;
        this.faculty = faculty;
    }
}

