package com.jake.mybatis.thymleaf.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jake.mybatis.thymleaf.domain.Student;

@Mapper
public interface StudentMapper {
	public List<Student> findAll();

	public Student findById(Long id); 
	public void create(Student student);
	public void update(Student student);
	public void delete(Long id);
}
