package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.StudentDTO;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class StudentController {

    private IStudentService studentService;

    @GetMapping("/getAllStudents")
    public List<StudentDTO> getAllStudents() {
        return studentService.getAllStudents()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @GetMapping("/getStudent/{id}")
    public StudentDTO getStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return mapToDTO(student);
    }

    @PostMapping("/createStudent")
    public StudentDTO createStudent(@RequestBody StudentDTO studentDTO) {
        Student student = mapToEntity(studentDTO);
        Student savedStudent = studentService.saveStudent(student);
        return mapToDTO(savedStudent);
    }

    @PutMapping("/updateStudent")
    public StudentDTO updateStudent(@RequestBody StudentDTO studentDTO) {
        Student student = mapToEntity(studentDTO);
        Student updatedStudent = studentService.saveStudent(student);
        return mapToDTO(updatedStudent);
    }

    @DeleteMapping("/deleteStudent/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    private StudentDTO mapToDTO(Student student) {
        if (student == null) {
            return null;
        }

        Long departmentId = student.getDepartment() != null
                ? student.getDepartment().getIdDepartment()
                : null;

        return new StudentDTO(
                student.getIdStudent(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getPhone(),
                student.getDateOfBirth(),
                student.getAddress(),
                departmentId
        );
    }

    private Student mapToEntity(StudentDTO studentDTO) {
        if (studentDTO == null) {
            return null;
        }

        Student student = new Student();
        student.setIdStudent(studentDTO.getIdStudent());
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setDateOfBirth(studentDTO.getDateOfBirth());
        student.setAddress(studentDTO.getAddress());

        if (studentDTO.getDepartmentId() != null) {
            Department department = new Department();
            department.setIdDepartment(studentDTO.getDepartmentId());
            student.setDepartment(department);
        }

        return student;
    }
}