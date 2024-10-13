package com.example.service;

import com.example.dto.StudentDTO;
import com.example.entity.Student;
import com.example.exception.StudentNotFoundException;
import com.example.mapper.StudentMapper;
import com.example.repository.StudentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.example.constant.TestConstant.ADDRESS;
import static com.example.constant.TestConstant.AGE;
import static com.example.constant.TestConstant.ID;
import static com.example.constant.TestConstant.NAME;
import static com.example.constant.TestConstant.SURNAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private static Student student;
    private static StudentDTO studentDTO;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeAll
    public static void setUp() {
        student = new Student(ID, NAME, SURNAME, AGE, ADDRESS);
        studentDTO = new StudentDTO(NAME, SURNAME);
    }

    @Test
    void findById_Success() {
        //given
        var expected = Optional.of(student);

        //when
        when(studentRepository.findById(ID)).thenReturn(expected);

        //then
        var actual = studentService.findById(ID);

        assertEquals(expected, actual);
        verify(studentRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    @Disabled("Expecting findById() to throw StudentNotFoundException but never achieved")
    void findById_WhenStudentNotFound_ShouldThrowStudentNotFoundException() {
        //when
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> studentService.findById(ID))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Student with id: " + ID + " is not found!");

        verify(studentRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void create_Success() {
        //given
        var expected = student;

        //when
        when(studentMapper.toEntity(studentDTO)).thenReturn(expected);
        when(studentRepository.save(expected)).thenReturn(expected);

        //then
        var actual = studentService.create(studentDTO);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(studentMapper, times(1)).toEntity(studentDTO);
        verify(studentRepository, times(1)).save(expected);
        verifyNoMoreInteractions(studentMapper, studentRepository);
    }

    @Test
    void create_WhenStudentNameOrSurnameInvalid_ShouldThrowIllegalArgumentException() {
        //given
        studentDTO.setName(null);
        studentDTO.setSurname(null);

        //then
        assertThrows(IllegalArgumentException.class, () -> studentService.create(studentDTO));
        verifyNoInteractions(studentRepository);
    }

    @Test
    void create_ThrowsException_WhenDTOIsNull() {
        //then
        assertThrows(IllegalArgumentException.class, () -> studentService.create(null));
        verifyNoInteractions(studentMapper, studentRepository);
    }

    @Test
    void getAll_Success() {
        //given
        List<Student> studentList = Collections.singletonList(student);

        //when
        when(studentRepository.findAll()).thenReturn(studentList);

        //then
        var result = studentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Kamil", result.get(0).getName());
        verify(studentRepository, times(1)).findAll();
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void update_WhenStudentNotFound_ThrowsStudentNotFoundException() {
        //when
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        assertThrows(StudentNotFoundException.class, () -> studentService.update(ID, studentDTO));
        verify(studentRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void patch_Success() {
        //given
        var expected = student;
        var updates = new HashMap<String, Object>();
        updates.put("name", "Kamil");
        updates.put("surname", "Heyderli");

        //when
        when(studentRepository.findById(ID)).thenReturn(Optional.of(expected));
        when(studentRepository.save(expected)).thenReturn(expected);

        //then
        var actual = studentService.patch(1L, updates);

        assertNotNull(actual);
        assertEquals(ResponseEntity.ok(expected), actual);
        verify(studentRepository, times(1)).findById(ID);
        verify(studentRepository, times(1)).save(expected);
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void patch_WhenStudentNotFound_Then404() {
        //given
        var expected = ResponseEntity.notFound().build();

        //when
        when(studentRepository.findById(ID)).thenReturn(Optional.empty());

        //then
        var actual = studentService.patch(ID, new HashMap<>());

        assertEquals(expected, actual);
        verify(studentRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }
}