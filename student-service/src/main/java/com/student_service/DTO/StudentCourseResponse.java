package com.student_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseResponse {
    private Long studentId;
    private String studentName;
    private String studentEmail;

    private Long courseId;
    private String courseTitle;
    private String courseDescription;

}
