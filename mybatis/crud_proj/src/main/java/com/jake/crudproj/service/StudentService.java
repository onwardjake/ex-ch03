package com.jake.crudproj.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jake.crudproj.domain.Student;
import com.jake.crudproj.mapper.StudentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

	private final StudentMapper studentMapper;
	
	@Transactional
	public void deleteStudent(Long id) {
		studentMapper.delete(id);
	}
	
	@Transactional
	public void updateStudent(Student student) {
		studentMapper.update(student);
	}
	
	@Transactional
	public void createStudent(Student student) {
		studentMapper.insert(student);
	}
	
	public Student getStudent(Long id) {
		return studentMapper.findById(id);
	}
	
	public List<Student> getAllStudents() {
		return studentMapper.findAll();
		/*
		List<Student> students = new ArrayList<>();
		
		students.add(Student.builder()
				.id(1L)
				.name("홍길동")
				.build()
		);
		
		students.add(new Student() {
			{
				setId(2L);
				setName("김철수");
			}
		});
		students.add(new Student() {
			{
				setId(3L);
				setName("이영희");
			}
		});
		
		return students;
		 */
	}
}
