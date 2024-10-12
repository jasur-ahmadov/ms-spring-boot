package com.example.service;

import com.example.dto.StudentDTO;
import com.example.entity.Student;
import com.example.exception.StudentNotFoundException;
import com.example.mapper.StudentMapper;
import com.example.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public Student create(StudentDTO dto) {
        return Optional.ofNullable(dto)
                .map(studentMapper::toEntity)
                .map(studentRepository::save)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(id)
                .map(studentRepository::findById)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public void deleteById(Long id) {
        if (id != null && studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new StudentNotFoundException(id);
        }
    }

    public void update(Long id, StudentDTO dto) {
        Optional.ofNullable(id)
                .flatMap(studentRepository::findById)
                .map(s -> studentMapper.toEntity(s, dto))
                .map(studentRepository::save)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public ResponseEntity<Student> patch(Long id, Map<String, Object> updates) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Student student = optionalStudent.get();
        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Student.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, student, value);
            }
        });
        return ResponseEntity.ok().body(studentRepository.save(student));
    }
}