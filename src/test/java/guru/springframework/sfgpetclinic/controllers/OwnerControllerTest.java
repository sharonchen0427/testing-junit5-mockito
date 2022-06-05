package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    OwnerService ownerService;

    @InjectMocks
    OwnerController ownerController;

    @Mock
    BindingResult bindingResult;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testProcessFindForm_returnOneResult() {
        List<Owner> results = new ArrayList<>();
        Owner owner = new Owner(1L,"Test", "Case");
        results.add(owner);

        when(ownerService.findAllByLastNameLike("%" + "Case" + "%")).thenReturn(results);

//when
        String actualRes = ownerController.processFindForm(owner, bindingResult, null);

        //then
        verify(ownerService).findAllByLastNameLike(anyString());
        assertNotNull(actualRes);
    }

    @Test
    void testProcessCreateForm_HasErrors() {
        Owner owner = new Owner(1L,"Test", "Case");

        given(bindingResult.hasErrors()).willReturn(true);
//        given(ownerService.save(any())).willReturn("owners/createOrUpdateOwnerForm");

        String viewName = ownerController.processCreationForm(owner, bindingResult);

        assertEquals("owners/createOrUpdateOwnerForm", viewName);
    }
    @Test
    void testProcessCreateForm_NoErrors() {
        Owner owner = new Owner(1L,"Test", "Case");

        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any())).willReturn(owner);

        String viewName = ownerController.processCreationForm(owner, bindingResult);

        assertEquals("redirect:/owners/1", viewName);
    }

    //捕获参数值以进行进一步的声明
    @Test
    void processFindFormWildCard() { //bdd mockito
        //given
        Owner owner=new Owner(2L,"Joe","Buck");
        List<Owner> owners=new ArrayList<>();
        //ArgumentCaptor
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        given(ownerService.findAllByLastNameLike(captor.capture())).willReturn(owners);
        //when
        String res = ownerController.processFindForm(owner, bindingResult, null);
        //then
        assertThat("%Buck%").isEqualToIgnoringCase(captor.getValue());
    }


}