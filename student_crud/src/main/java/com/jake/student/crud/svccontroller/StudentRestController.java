package com.jake.student.crud.svccontroller;

import com.jake.student.crud.Student;
import com.jake.student.crud.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {
    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.selectAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> selectOne(@PathVariable long id) {
        Student student = studentService.selectOne(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping("/create")
    public ResponseEntity<Student> create(@RequestBody Student student) {
        studentService.create(student);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Student> update(@PathVariable long id, @RequestBody Student student) {
        student.setId(id);
        studentService.update(student);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Student> delete(@PathVariable long id) {
        Student student = studentService.selectOne(id);
        studentService.delete(id);
        return ResponseEntity.ok(student);
    }
}
