package com.jake.student.crud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {
    private final StudentMapper studentMapper;

    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    public Student selectOne(Long id) {
        return studentMapper.selectById(id);
    }

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
