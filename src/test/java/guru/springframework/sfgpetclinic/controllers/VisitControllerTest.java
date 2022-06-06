package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.OwnerService;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import guru.springframework.sfgpetclinic.services.map.PetMapService;
import guru.springframework.sfgpetclinic.services.springdatajpa.PetSDJpaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

    @Mock
    VisitService visitService;

    @Spy
    PetMapService service;
    //Please ensure that the type 'PetSDJpaService' has a no-arg constructor.
//    PetSDJpaService petService;//concrete class

    @InjectMocks
    VisitController controller;

    @Test
    void loadPetWithVisit() {
        Map<String, Object> model = new HashMap<>();
        Pet pet = new Pet(1L);
        service.save(pet);
        //given
        given(service.findById(anyLong())).willCallRealMethod();

        //when
        Visit res = controller.loadPetWithVisit(1L, model);

        //then
        assertThat(res).isNotNull();
        assertThat(res.getPet()).isNotNull();
        assertThat(res.getPet().getId()).isEqualTo(1L);
    }
}