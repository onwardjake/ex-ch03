package com.jake.mybatis.thymleaf.controller;

import com.jake.mybatis.thymleaf.domain.Student;
import com.jake.mybatis.thymleaf.service.StudentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {
	
	private final StudentService studentService;

	@GetMapping
	public List<Student> list() {
		return studentService.getAllStudents();
	}
	
}
