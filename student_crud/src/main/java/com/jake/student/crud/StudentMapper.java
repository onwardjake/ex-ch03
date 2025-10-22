package com.jake.student.crud;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    List<Student> selectAll();
    Student selectById(long id);
    void create(Student student);
    void update(Student student);
    void delete(long id);
}
