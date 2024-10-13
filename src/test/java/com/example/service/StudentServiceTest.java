package com.example.service;

import com.example.dto.StudentDTO;
import com.example.entity.Student;
import com.example.mapper.StudentMapper;
import com.example.repository.StudentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.constant.TestConstant.ADDRESS;
import static com.example.constant.TestConstant.AGE;
import static com.example.constant.TestConstant.ID;
import static com.example.constant.TestConstant.NAME;
import static com.example.constant.TestConstant.SURNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
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
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

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
        when(studentService.findById(ID)).thenReturn(expected);

        //then
        var actual = studentService.findById(ID);

        assertEquals(expected, actual);
        verify(studentRepository, times(1)).findById(ID);
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void create_Success() {
        //when
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);

        //then
        var actual = studentService.create(studentDTO);

        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        assertEquals(NAME, actual.getName());
        assertEquals(SURNAME, actual.getSurname());
        assertEquals(AGE, actual.getAge());
        assertEquals(ADDRESS, actual.getAddress());

        verify(studentMapper, times(1)).toEntity(studentDTO);
        verify(studentRepository, times(1)).save(student);
        verifyNoMoreInteractions(studentMapper, studentRepository);
    }

    @Test
    void create_WhenStudentNameOrSurnameInvalid_ShouldThrowIllegalArgumentException() {
        //given
        studentDTO.setName(null);
        studentDTO.setSurname(null);

        //then
        var exception = assertThrows(IllegalArgumentException.class, () -> studentService.create(studentDTO));

        assertEquals(exception.getClass().getName(), "java.lang.IllegalArgumentException");
        verifyNoInteractions(studentRepository);
    }

    @Test
    void testSave_ThrowsException_WhenDTOIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentService.create(null));
        verify(studentMapper, never()).toEntity(any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testFindAll() {
        List<Student> studentList = Collections.singletonList(student);
        when(studentRepository.findAll()).thenReturn(studentList);
        List<Student> result = studentService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jasur", result.get(0).getName());
        verify(studentRepository, times(1)).findAll();
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void testUpdate_ThrowsException_WhenStudentNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.update(1L, studentDTO));
        assertEquals("Student with id: 1 is not found!", exception.getMessage());
        verify(studentRepository, times(1)).findById(anyLong());
        verify(studentMapper, never()).toEntity(any(Student.class), any(StudentDTO.class));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void testPatch() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Updated Name");
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        ResponseEntity<Student> response = studentService.patch(1L, updates);
        assertNotNull(response);
        assertEquals(ResponseEntity.ok(student), response);
        verify(studentRepository, times(1)).findById(anyLong());
        verify(studentRepository, times(1)).save(any(Student.class));
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void testPatch_StudentNotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Student> response = studentService.patch(1L, new HashMap<>());
        assertEquals(ResponseEntity.notFound().build(), response);
        verify(studentRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(studentRepository);
        verifyNoInteractions(studentMapper);
    }
}