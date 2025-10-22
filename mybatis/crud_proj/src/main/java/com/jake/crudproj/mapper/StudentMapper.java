package com.jake.crudproj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jake.crudproj.domain.Student;

@Mapper
public interface StudentMapper {

	List<Student> findAll();
	Student findById(Long id);
	void insert(Student student);
	void update(Student student);
	void delete(Long id);
}
