package com.capstone.assignmentmicroservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//import com.capstone.assignmentmicroservice.entity.Assignment;
//import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.assignmentmicroservice.entity.StudentAssignment;
import com.capstone.assignmentmicroservice.service.AssignmentService;


@RestController
@RequestMapping("/assignments")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;



    
    

    @GetMapping("/showAll")
    public List<Map<String, Object>> getAllAssignments() {

        return assignmentService.getAllAssignments();
    }

    @GetMapping("/byCourseId/{courseId}")
    public List<Map<String, Object>> getAssignmentsByCourseId(@PathVariable Integer courseId) {
        return  assignmentService.getAssignmentsByCourseId(courseId);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadAssignment(@RequestParam("file") MultipartFile file,
                                                                 @RequestParam("title") String title,
                                                                 @RequestParam("id") Integer id,
                                                                 @RequestParam("courseId") Integer courseId,
                                                                 @RequestParam("deadline") String deadline) {
        System.out.println("File: " + file.getOriginalFilename());
        System.out.println("Title: " + title);
        System.out.println("ID: " + id);
        System.out.println("Course ID: " + courseId);
        System.out.println("Deadline: " + deadline);
        try {
            assignmentService.saveAssignment(file, title, id, courseId, deadline);
            return ResponseEntity.ok(Map.of("message", "Assignment uploaded successfully."));
        } catch (IOException e) {
            return ResponseEntity.ok(Map.of("message", "Failed to upload assignment: " + e.getMessage()));
        }
    }



    @GetMapping("/showAssignmentFileById/{id}")
    public ResponseEntity<Resource> showAssignmentFileById(@PathVariable Integer id) {
        byte[] fileData = assignmentService.showAssignmentFileById(id);
        if (fileData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ByteArrayResource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"assignment_" + id + ".pdf\"")
                .contentLength(fileData.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer id) {
        byte[] fileData = assignmentService.showAssignmentFileById(id);
        if (fileData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ByteArrayResource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"assignment_" + id + ".pdf\"")
                .contentLength(fileData.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
    
    
    

    // Endpoint to allot marks to a student for a course
    @PostMapping("/allot-marks")
    public ResponseEntity<StudentAssignment> allotMarks(
            @RequestParam Integer studentId,
            @RequestParam Integer courseId,
            @RequestParam List<Integer> marks) {
        StudentAssignment updatedAssignment = assignmentService.allotMarks(studentId, courseId, marks);
        if (updatedAssignment != null) {
            return ResponseEntity.ok(updatedAssignment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint to get the marks by studentId and courseId
    @GetMapping("/marks/{studentId}/{courseId}")
    public ResponseEntity<List<Integer>> getMarksByStudentIdAndCourseId(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId) {
        List<Integer> marks = assignmentService.getMarksByStudentIdAndCourseId(studentId, courseId);
        if (marks != null) {
            return ResponseEntity.ok(marks);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    
    @PostMapping("/saveStudentAssignment")
    public ResponseEntity<StudentAssignment> saveStudentAssignment(
            @RequestParam Integer studentId,
            @RequestParam Integer courseId) {
        
        // Call the service method to save the student assignment
        StudentAssignment savedStudentAssignment = assignmentService.saveStudentAssignment(studentId, courseId);
        
        return ResponseEntity.ok(savedStudentAssignment);
    }
}

