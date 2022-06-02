package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService sdJpaService;


    /**
     * convert to BDD tests:
     */

    @Test
    void testFindByIdBDD() {
        //given
        Visit v = new Visit();
        given(visitRepository.findById((1L))).willReturn(Optional.of(v));

        //when
        Visit actualFound = sdJpaService.findById(1L);//1L

        //then
        assertThat(actualFound).isNotNull();
        then(visitRepository).should().findById(anyLong());//called once
    }

    @Test
    void testFindAllBDD() {
        //given
        Set<Visit> visits = new HashSet<>();
        Visit v = new Visit();
        visits.add(v);
        given(visitRepository.findAll()).willReturn(visits);

        //when
        Set<Visit> actualFoundVisits = sdJpaService.findAll();

        //then
        then(visitRepository).should().findAll();
        assertThat(actualFoundVisits).hasSize(1);//assertJ
    }

    @Test
    void testSaveBDD() {
        //given
        Visit v = new Visit();
        given(visitRepository.save(any(Visit.class))).willReturn(v);
//        when(visitRepository.save(any(Visit.class))).thenReturn(v);

        //when
        Visit actualSavedObj = sdJpaService.save(new Visit());

        //then
        then(visitRepository).should().save(any(Visit.class));
        //        verify(visitRepository).save(any(Visit.class));
        assertThat(actualSavedObj).isNotNull();
    }

    @Test
    void testDeleteBDD() {
        //given
        Visit v = new Visit();

        //when
        sdJpaService.delete(v);

        //then
//        verify(visitRepository).delete(any(Visit.class));//be called once
        then(visitRepository).should().delete(any(Visit.class));
    }

    @Test
    void testDeleteByIdBDD() {
        //when
        sdJpaService.deleteById(1L);
        //then
//        verify(visitRepository).deleteById(anyLong());//be called once
        then(visitRepository).should().deleteById(anyLong());
    }
}