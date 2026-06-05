package tn.esprit.studentmanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.studentmanagement.dto.DepartmentDTO;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

import java.util.List;

@RestController
@RequestMapping("/Department")
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class DepartmentController {

    private IDepartmentService departmentService;

    @GetMapping("/getAllDepartment")
    public List<DepartmentDTO> getAllDepartment() {
        return departmentService.getAllDepartments()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @GetMapping("/getDepartment/{id}")
    public DepartmentDTO getDepartment(@PathVariable Long id) {
        Department department = departmentService.getDepartmentById(id);
        return mapToDTO(department);
    }

    @PostMapping("/createDepartment")
    public DepartmentDTO createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = mapToEntity(departmentDTO);
        Department savedDepartment = departmentService.saveDepartment(department);
        return mapToDTO(savedDepartment);
    }

    @PutMapping("/updateDepartment")
    public DepartmentDTO updateDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = mapToEntity(departmentDTO);
        Department updatedDepartment = departmentService.saveDepartment(department);
        return mapToDTO(updatedDepartment);
    }

    @DeleteMapping("/deleteDepartment/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

    private DepartmentDTO mapToDTO(Department department) {
        if (department == null) {
            return null;
        }

        return new DepartmentDTO(
                department.getIdDepartment(),
                department.getName(),
                department.getLocation(),
                department.getPhone(),
                department.getHead()
        );
    }

    private Department mapToEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) {
            return null;
        }

        Department department = new Department();
        department.setIdDepartment(departmentDTO.getIdDepartment());
        department.setName(departmentDTO.getName());
        department.setLocation(departmentDTO.getLocation());
        department.setPhone(departmentDTO.getPhone());
        department.setHead(departmentDTO.getHead());

        return department;
    }
}
