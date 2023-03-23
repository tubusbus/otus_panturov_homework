package org.example.componentClasses;

import java.util.Set;

public class Courses {

  static Set<Course> courses;

  public static Set<Course> getCourse(){
    return courses;
  }

  public static void setCourse(Set<Course> newCourses){
    courses = newCourses;
  }
}