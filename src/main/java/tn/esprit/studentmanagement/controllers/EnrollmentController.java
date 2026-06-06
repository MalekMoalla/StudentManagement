package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.EnrollmentDTO;
import tn.esprit.studentmanagement.entities.Course;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IEnrollment;

import java.util.List;

@RestController
@RequestMapping("/Enrollment")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class EnrollmentController {

    private IEnrollment enrollmentService;

    @GetMapping("/getAllEnrollment")
    public List<EnrollmentDTO> getAllEnrollment() {
        return enrollmentService.getAllEnrollments()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @GetMapping("/getEnrollment/{id}")
    public EnrollmentDTO getEnrollment(@PathVariable Long id) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        return mapToDTO(enrollment);
    }

    @PostMapping("/createEnrollment")
    public EnrollmentDTO createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = mapToEntity(enrollmentDTO);
        Enrollment savedEnrollment = enrollmentService.saveEnrollment(enrollment);
        return mapToDTO(savedEnrollment);
    }

    @PutMapping("/updateEnrollment")
    public EnrollmentDTO updateEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        Enrollment enrollment = mapToEntity(enrollmentDTO);
        Enrollment updatedEnrollment = enrollmentService.saveEnrollment(enrollment);
        return mapToDTO(updatedEnrollment);
    }

    @DeleteMapping("/deleteEnrollment/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }

    private EnrollmentDTO mapToDTO(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        Long studentId = enrollment.getStudent() != null
                ? enrollment.getStudent().getIdStudent()
                : null;

        Long courseId = enrollment.getCourse() != null
                ? enrollment.getCourse().getIdCourse()
                : null;

        return new EnrollmentDTO(
                enrollment.getIdEnrollment(),
                enrollment.getEnrollmentDate(),
                enrollment.getGrade(),
                enrollment.getStatus(),
                studentId,
                courseId
        );
    }

    private Enrollment mapToEntity(EnrollmentDTO enrollmentDTO) {
        if (enrollmentDTO == null) {
            return null;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setIdEnrollment(enrollmentDTO.getIdEnrollment());
        enrollment.setEnrollmentDate(enrollmentDTO.getEnrollmentDate());
        enrollment.setGrade(enrollmentDTO.getGrade());
        enrollment.setStatus(enrollmentDTO.getStatus());

        if (enrollmentDTO.getStudentId() != null) {
            Student student = new Student();
            student.setIdStudent(enrollmentDTO.getStudentId());
            enrollment.setStudent(student);
        }

        if (enrollmentDTO.getCourseId() != null) {
            Course course = new Course();
            course.setIdCourse(enrollmentDTO.getCourseId());
            enrollment.setCourse(course);
        }

        return enrollment;
    }
}