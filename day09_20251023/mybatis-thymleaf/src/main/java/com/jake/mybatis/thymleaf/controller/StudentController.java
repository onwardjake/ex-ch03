package com.jake.mybatis.thymleaf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jake.mybatis.thymleaf.domain.Student;
import com.jake.mybatis.thymleaf.service.StudentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;
	
	// 전체 조회
	@GetMapping
	public String list(Model model) {
		model.addAttribute("students", studentService.getAllStudents());
		return "student/list";
	}
	
	// 단건 조회
	@GetMapping("/{id}")
	public ResponseEntity<Student> detail(@PathVariable Long id) {
		Student student = studentService.getStudent(id);
		return ResponseEntity.ok(student);
	}
	
	// 신규 학생 추가 화면
	@GetMapping("/new")
	public String createStudent(Model model) {
		model.addAttribute("student", new Student());
		return "student/form";
	}
	
	// 신규 학생 등록 처리
	@PostMapping
	public String create(@ModelAttribute Student student) {
		studentService.create(student);
		return "redirect:/students";
	}
	
	// 학생 정보 수정 화면
	@GetMapping("/{id}/edit")
	public String updateStudent(@PathVariable Long id, Model model) {
		model.addAttribute("student", studentService.getStudent(id));
		return "student/form";
	}
	
	// 학생 정보 수정 처리
	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute Student student) {
		student.setId(id);
		studentService.update(student);
		return "redirect:/students";
	}
	
	// 학생 정보 삭제
	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		studentService.delete(id);
		return "redirect:/students";
	}
}
