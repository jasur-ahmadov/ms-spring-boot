package com.example.controller;

import com.example.dto.StudentDTO;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.example.constant.TestConstant.ID;
import static com.example.constant.TestConstant.NAME;
import static com.example.constant.TestConstant.STUDENT_PATH;
import static com.example.constant.TestConstant.SURNAME;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    private static StudentDTO studentDTO;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentDTO = new StudentDTO();
    }

    @Test
    void findById_Success() throws Exception {
        //given
        var student = Student.builder()
                .id(ID)
                .name(NAME)
                .surname(SURNAME)
                .build();

        //when
        when(studentService.findById(ID)).thenReturn(Optional.of(student));

        //then
        mockMvc.perform(get(STUDENT_PATH + "/id/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId()))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.surname").value(student.getSurname()));

        verify(studentService, times(1)).findById(ID);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void create_Success() throws Exception {
        //given
        studentDTO.setName(NAME);
        studentDTO.setSurname(SURNAME);
        var expected = new Student();
        expected.setId(ID);
        expected.setName(NAME);
        expected.setSurname(SURNAME);

        //when
        when(studentService.create(studentDTO)).thenReturn(expected);

        //then
        mockMvc.perform(post(STUDENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(NAME)))
                .andExpect(jsonPath("$.surname", is(SURNAME)));

        verify(studentService, times(1)).create(studentDTO);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void create_WhenStudentNameNull_BadRequest() throws Exception {
        //given
        studentDTO.setSurname(SURNAME);

        //then
        mockMvc.perform(post(STUDENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist());

        verifyNoInteractions(studentService);
    }

    @Test
    public void update_Success() throws Exception {
        //given
        studentDTO.setName(NAME);
        studentDTO.setSurname(SURNAME);

        //when
        doNothing().when(studentService).update(ID, studentDTO);

        //then
        mockMvc.perform(put(STUDENT_PATH + "/id/" + ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk());

        verify(studentService, times(1)).update(ID, studentDTO);
        verifyNoMoreInteractions(studentService);
    }
}