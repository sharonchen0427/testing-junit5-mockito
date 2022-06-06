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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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

    /**
     * BDD tests:
     */
    @Test
    void testDeleteByIdBDD() {
        //given-none
        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        //then
        then(specialtyRepository).should(timeout(10).times(2)).deleteById(anyLong());
    }

    @Test
    void testAtLeastDeleteByIdBDD() {
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        then(specialtyRepository).should(atLeast(1)).deleteById(1L);
    }

    @Test
    void testFindByIdReturnSpecialtyBDD() {
        //given
        Speciality s = new Speciality();
        given(specialtyRepository.findById(anyLong())).willReturn(Optional.of(s));

        //when
        Speciality byId = specialitySDJpaService.findById(1L);

        //then
        assertThat(byId).isNotNull();
        then(specialtyRepository).should().findById(anyLong());//verify/then: using matchers
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void tesDeleteByObjectReturnVoidBDD() {
        //given
        Speciality s = new Speciality();
        //when
        specialitySDJpaService.delete(s);
        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    //then 是 BDDMockito 的 API，比 verify 更加语义化
    @Test
    void testFindByIdBDD() {
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        //when: actual call
        Speciality actualFound = specialitySDJpaService.findById(1L);

        //then
        assertThat(actualFound).isNotNull();
        then(specialtyRepository).should().findById(anyLong());//default 1 time
//        then(specialtyRepository).should(times(1)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void testDeleteThrow(){
        doThrow(new RuntimeException()).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, ()->specialtyRepository.delete(new Speciality()));
        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIdThrow(){
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException());

        assertThrows(RuntimeException.class, ()->specialtyRepository.findById(1L));
//
        then(specialtyRepository).should().findById(any());
    }

    @Test
    void testJava8ArgLambda_SaveSuccessfully() {
         //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1L);
        //as long as regex return true,
        //return savedSpecialty
        given(specialtyRepository.save(argThat(arg -> arg.getDescription().equals(MATCH_ME))))
                .willReturn(savedSpecialty);

        //when
        Speciality returnSpecialty = specialitySDJpaService.save(speciality);

        //then
        assertThat(returnSpecialty.getId()).isEqualTo(1L);
    }
}