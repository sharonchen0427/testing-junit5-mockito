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
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService sdJpaService;

    @Test
    void testFindById() {
        //build a return value
        Visit v = new Visit();
        when(visitRepository.findById(anyLong())).thenReturn(Optional.of(v));//1L

        //actual call
        Visit actualFound = sdJpaService.findById(1L);//1L
        //(Object expected, Object actual)
//        assertEquals(v, actualFound);
        verify(visitRepository).findById(anyLong());

        assertThat(actualFound).isNotNull();
    }

    @Test
    void testFindAll() {
        //build a return value
        Set<Visit> visits = new HashSet<>();
        Visit v = new Visit();
        visits.add(v);

        //actual call
        when(visitRepository.findAll()).thenReturn(visits);

        Set<Visit> actualFoundVisits = sdJpaService.findAll();
        //(Object expected, Object actual)
        //assertEquals(visits, actualFound);
        assertThat(actualFoundVisits).hasSize(1);//assertJ
        verify(visitRepository).findAll();//be called once//verify(visitRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        Visit v = new Visit();

        when(visitRepository.save(any(Visit.class))).thenReturn(v);

        //actual call
        Visit actualSavedObj = sdJpaService.save(new Visit()); //who?

        //(Object expected, Object actual)
//        assertEquals(v, actualSavedObj);
        verify(visitRepository).save(any(Visit.class));

        //assertj
        assertThat(actualSavedObj).isNotNull();
    }

    @Test
    void testDelete() {
        sdJpaService.delete(new Visit());
        verify(visitRepository).delete(any(Visit.class));//be called once
    }

    @Test
    void testDeleteById() {
        sdJpaService.deleteById(1L);
        verify(visitRepository).deleteById(anyLong());//be called once
    }
}