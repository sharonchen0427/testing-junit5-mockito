package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService specialitySDJpaService;

    @Test
    void testDeleteById() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, times(2)).deleteById(1L);
    }

    @Test
    void testNeverDeleteById() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, never()).deleteById(5L);
    }

    @Test
    void testAtLeastDeleteById() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, atLeast(1)).deleteById(1L);
    }

    @Test
    void testAtmostDeleteById() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        verify(specialtyRepository, atMost(5)).deleteById(1L);
    }

    @Test
    void testFindByIdReturnSpecialty() {
        //build a return value
        Speciality s = new Speciality();
        when(specialtyRepository.findById(anyLong())).thenReturn(Optional.of(s)); //deeper call

        //actual method call
        Speciality byId = specialitySDJpaService.findById(1L);

        assertNotNull(byId);
//        assertThat(byId).isNotNull(); //assertJ
        assertEquals(byId, s);

        verify(specialtyRepository).findById(anyLong());
    }

    @Test
    void tesDeleteByObjectReturnVoid() {
        Speciality s = new Speciality();
        specialitySDJpaService.delete(s);
        verify(specialtyRepository).delete(any(Speciality.class));
    }
}