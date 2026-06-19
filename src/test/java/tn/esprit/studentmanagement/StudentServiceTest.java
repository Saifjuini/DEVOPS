package tn.esprit.studentmanagement;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;
import tn.esprit.studentmanagement.services.StudentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student();
        student.setIdStudent(1L);
        student.setFirstName("Saif");
        student.setLastName("Juini");
        student.setEmail("saif@esprit.tn");
        student.setPhone("12345678");
        student.setDateOfBirth(LocalDate.of(2000, Month.JANUARY, 1));
        student.setAddress("Tunis");
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));
        List<Student> result = studentService.getAllStudents();
        assertEquals(1, result.size());
        assertEquals("Saif", result.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById_found() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Student result = studentService.getStudentById(1L);
        assertNotNull(result);
        assertEquals("Juini", result.getLastName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_notFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        Student result = studentService.getStudentById(99L);
        assertNull(result);
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveStudent() {
        when(studentRepository.save(student)).thenReturn(student);
        Student result = studentService.saveStudent(student);
        assertNotNull(result);
        assertEquals("saif@esprit.tn", result.getEmail());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);
        studentService.deleteStudent(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}