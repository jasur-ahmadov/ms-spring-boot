package com.example.mapper;

import com.example.dto.StudentDTO;
import com.example.entity.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.example.constant.TestConstant.ADDRESS;
import static com.example.constant.TestConstant.AGE;
import static com.example.constant.TestConstant.ID;
import static com.example.constant.TestConstant.NAME;
import static com.example.constant.TestConstant.SURNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StudentMapperTest {

    private static Student entity;
    private static StudentDTO dto;
    private final StudentMapper studentMapper = StudentMapper.INSTANCE;

    @BeforeAll
    public static void setUp() {
        dto = new StudentDTO();
        entity = new Student();
        entity.setId(ID);
        entity.setName(NAME);
        entity.setSurname(SURNAME);
        entity.setAge(AGE);
        entity.setAddress(ADDRESS);
    }

    @Test
    public void updateEntity_WhenNewDataGiven_Success() {
        dto = new StudentDTO("Kamil", "Heydarli");
        studentMapper.toEntity(entity, dto);
        assertNotNull(entity);
        assertEquals(ID, entity.getId());
        assertEquals(AGE, entity.getAge());
        assertEquals(ADDRESS, entity.getAddress());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getSurname(), entity.getSurname());
    }

    @Test
    void mapToDto_WhenStudentEntityGiven() {
        var dto = studentMapper.toDto(entity);
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getSurname(), dto.getSurname());
    }

    @Test
    void mapToEntity_WhenStudentDtoNull() {
        studentMapper.toEntity(dto);
        assertEquals(entity.getId(), ID);
        assertEquals(entity.getName(), NAME);
        assertEquals(entity.getSurname(), SURNAME);
        assertEquals(entity.getAge(), AGE);
        assertEquals(entity.getAddress(), ADDRESS);
    }
}