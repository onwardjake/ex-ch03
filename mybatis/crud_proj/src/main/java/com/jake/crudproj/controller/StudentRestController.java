package com.jake.crudproj.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jake.crudproj.domain.Student;
import com.jake.crudproj.service.StudentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentRestController {
	private final StudentService studentService;
	
	// 전체 학생 목록
	@GetMapping
	public String list(Model model) {
		model.addAttribute("students", studentService.getAllStudents());
		return "student/list";
	}
	
	// 새 학생 등록 form
	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("student", new Student());
		return "student/form";
	}
	
	// 등록처리: 학생정보 기록 후 저장버튼 클릭하면, 학생 정보를 insert
	@PostMapping
	public String create(@ModelAttribute Student student) {
		studentService.createStudent(student);
		
		// /students로 리다이렉트 시켜서 list.html 페이지로 이동하도록 한다
		return "redirect:/students";
	}
	
	// 새 학생 등록 form
	@GetMapping("/{id}/edit")
	public String updateForm(@PathVariable Long id, Model model) {
		model.addAttribute("student", studentService.getStudent(id));
		return "student/form";
	}
	
	// 수정처리
	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute Student student) {
		student.setId(id);
		studentService.updateStudent(student);
		return "redirect:/students";
	}
	
	// 삭제 처리
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return "redirect:/students";
	}
	
	
	
	/*
	
	// 전체 학생 조회
	// http://localhost:8080/api/students
	@GetMapping
	public List<Student> list() {
		return studentService.getAllStudents();
	}
	
	// 학생 id로 조회 (단건)
	// http://localhost:8080/api/students/1
	@GetMapping("/{id}")
	public ResponseEntity<Student> detail(@PathVariable Long id){
		Student student = studentService.getStudent(id);
		if(student == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(student);
	}
	
	// 등록 : post
	@PostMapping("/create")
	public ResponseEntity<Student> create(@RequestBody Student student){
		studentService.createStudent(student);
		return ResponseEntity.ok(student);
	}
	
	
	// 수정 : put
	@PutMapping("/update/{id}")
	public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
		student.setId(id);
		studentService.updateStudent(student);
		
		return ResponseEntity.ok(student);
	}
	
	
	// 삭제 : delete
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Student> delete(@PathVariable Long id){
		Student student = studentService.getStudent(id);
		studentService.deleteStudent(id);
		return ResponseEntity.ok(student);
	}
	*/
	
}
