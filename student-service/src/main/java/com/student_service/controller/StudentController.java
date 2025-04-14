package com.student_service.controller;

import com.student_service.DTO.StudentCourseResponse;
import com.student_service.model.Student;
import com.student_service.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.student_service.repository.StudentRepository;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository repository;

    @Autowired
    private StudentService studentService;

    @PutMapping("/{studentId}/assign-course/{courseId}")
    public Student assignCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        return studentService.assignCourseToStudent(studentId, courseId);
    }
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return repository.save(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
    @GetMapping("/{id}/with-course")
    public ResponseEntity<StudentCourseResponse> getStudentWithCourse(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentWithCourse(id));
    }

}
