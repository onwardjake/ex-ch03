package com.jake.mybatis.thymleaf.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jake.mybatis.thymleaf.domain.Student;
import com.jake.mybatis.thymleaf.mapper.StudentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
	
	private final StudentMapper studentMapper;
	
	public List<Student> getAllStudents(){
		return studentMapper.findAll();
	}

	public Student getStudent(Long id) {
		return studentMapper.findById(id);
	}

	@Transactional
	public void create(Student student) {
		studentMapper.create(student);		
	}

	public void update(Student student) {
		studentMapper.update(student);
	}

	public void delete(Long id) {
		studentMapper.delete(id);
	}
}
