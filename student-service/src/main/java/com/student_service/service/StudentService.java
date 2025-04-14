package com.student_service.service;

import com.student_service.DTO.CourseDto;
import com.student_service.DTO.StudentCourseResponse;
import com.student_service.feign.CourseClient;
import com.student_service.model.Student;
import com.student_service.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseClient courseClient;

    public Student assignCourseToStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CourseDto course = courseClient.getCourseById(courseId);
        System.out.println("Course Title: " + course.getTitle());
        System.out.println("Course Id: " + course.getCourseId());
        // Save only courseId (or some fields) — do not persist entire course object
        student.setAssignedCourseId(course.getCourseId());
        return studentRepository.save(student);
    }
    public StudentCourseResponse getStudentWithCourse(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentCourseResponse response = new StudentCourseResponse();
        response.setStudentId(student.getStudentId());
        response.setStudentName(student.getName());
        response.setStudentEmail(student.getEmail());

        if (student.getAssignedCourseId() != null) {
            CourseDto course = courseClient.getCourseById(student.getAssignedCourseId());
            response.setCourseId(course.getCourseId());
            response.setCourseTitle(course.getTitle());
            response.setCourseDescription(course.getDescription());
        }

        return response;
    }

}

