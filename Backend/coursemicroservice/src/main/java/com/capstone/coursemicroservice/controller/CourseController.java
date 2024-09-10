package com.capstone.coursemicroservice.controller;


import com.capstone.coursemicroservice.entity.Course;
import com.capstone.coursemicroservice.service.CourseService;
import com.projectums.entity.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    
    @GetMapping("/getAll")
    public List<Course> getAllCourses() {

        return courseService.getAllCourses();
    }


    @PostMapping("/addCourse")
    public Course addCourse(@RequestBody Course course) {

        return courseService.saveCourse(course);
    }

    @PutMapping("/updateCourse")
    public Course updateCourse(@RequestBody Course course) {

        return courseService.updateCourse(course);
    }

    @GetMapping("/student/{studentId}")
    public List getAllCoursesByStudentId(@PathVariable Integer studentId) {
        return courseService.getAllCoursesByStudentId(studentId);
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/allStudentAssignment/{studentId}")
    public List<Object> getAllAssignmentsForStudent(@PathVariable Integer studentId) {
        // First, call the User Management Service to get all courses the student is enrolled in
        String userManagementUrl = "http://USER-MANAGEMENT-SERVICE/students/" + studentId + "/courses";
        
        // Get the list of course IDs the student is enrolled in
        List<Integer> courseIds = restTemplate.getForObject(userManagementUrl, List.class);

        // Use the Course Service to get the assignments for each course
        return courseService.getAllAssignmentsOfStudent(courseIds);
    }


    @GetMapping("/professor/{professorId}")
    public List<Course> getAllCoursesByProfessorId(String token,@PathVariable Integer professorId) {

        return courseService.getAllCoursesByProfessorId(professorId);
    }

    @GetMapping("/showResource/{courseId}")
    public ResponseEntity<ByteArrayResource> showResource(@PathVariable Integer courseId) {
        byte[] resourceData = courseService.getResourceByCourseId(courseId);
        if (resourceData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ByteArrayResource resource = new ByteArrayResource(resourceData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"course_" + courseId + ".pdf\"")
                .contentLength(resourceData.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
    
//    @GetMapping("/{courseId}/students")
//    public ResponseEntity<List<Student>> getStudentsByCourseId(@PathVariable Integer courseId) {
//        List<Student> students = courseService.getStudentsByCourseId(courseId);
//        return ResponseEntity.ok(students);
//    }



    }

//    @DeleteMapping("/{courseId}")
//    public void deleteCourse(@PathVariable Integer courseId) {
//        courseService.deleteCourse(courseId);
//    }

//    @GetMapping("/{courseId}/professor")
//    public String getProfessorName(@PathVariable Integer courseId) {
//        Course course = courseService.getCourseById(courseId);
//        return courseService.getProfessorNameById(course.getProfessorId());
//    }

//    public List<Course> getAssignmentsByCourseId(@PathVariable Integer courseId) {
//        return courseService.getAssignmentsByCourseId();
//    }
//    @GetMapping("/getOverallScore")
//    public int getOverallScore(@RequestParam Integer courseId, @RequestParam List<Long> assignmentIds) {
//        return courseService.getOverallScore(courseId);
//    }


