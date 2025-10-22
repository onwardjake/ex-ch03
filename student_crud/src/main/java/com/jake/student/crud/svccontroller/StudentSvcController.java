package com.jake.student.crud.svccontroller;

import com.jake.student.crud.Student;
import com.jake.student.crud.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentSvcController {
    private final StudentService studentService;

    // list 화면
    @GetMapping
    public String list(Model model) {
        // "students"는 템플릿에서 사용할 이름(key)으로, 템플릿에서 ${students}로 접근할 수 있게 된다.
        model.addAttribute("students", studentService.selectAll());
        return "student/list";
    }

    // 새 학생 등록 form
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        return "student/form";
    }

    // 새 학생 등록 처리 후 학생 목록 화면으로 redirect 시킨다
    @PostMapping
    public String create(@ModelAttribute Student student) {
        studentService.create(student);

        return "redirect:/students";
    }

    // 학생 정보 수정 form
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.selectOne(id));
        return "student/form";
    }

    // 학생 정보 수정 처리
    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        studentService.update(student);
        return "redirect:/students";
    }

    // 학생 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}
