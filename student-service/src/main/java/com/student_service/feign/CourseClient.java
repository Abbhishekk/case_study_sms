package com.student_service.feign;

import com.student_service.DTO.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "api-gateway")
public interface CourseClient {

    @GetMapping("/courses/{id}")
    CourseDto getCourseById(@PathVariable("id") Long id);
}