package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Captor
    ArgumentCaptor<String> captor;

    @Test
    void processFindFormWildCard_Annotation() { //bdd mockito
        //given
        Owner owner=new Owner(2L,"Joe","Buck");
        List<Owner> owners=new ArrayList<>();
        //ArgumentCaptor
        given(ownerService.findAllByLastNameLike(captor.capture())).willReturn(owners);
        //when
        String res = ownerController.processFindForm(owner, bindingResult, null);
        //then
        assertThat("%Buck%").isEqualToIgnoringCase(captor.getValue());
    }

    @BeforeEach
    @Test
    void setUpGivenWillAnswers_insteadOfThreeGivens() {
        //invocation.getArgument(0)
        given(ownerService.findAllByLastNameLike(captor.capture())).willAnswer(
                invocation -> {
                    List<Owner> owners = new ArrayList<>();
                    String lastname = invocation.getArgument(0); //get lastname
                    if (lastname.equals("%Buck%")) {
                        owners.add(new Owner(2L, "joe", "buck"));
                        return owners;
                    } else if (lastname.equals("%Notfindme%")) {
                        return owners;
                    } else if (lastname.equals("%Findme%")) {
                        owners.add(new Owner(1L, "one", "one"));
                        owners.add(new Owner(2L, "joe", "buck"));
                        return owners;
                    }
                    throw new RuntimeException("invalid argument");
                }
        );
    }

    @Test
    void processFindForm_returnNoOwner() {
        Owner owner=new Owner(1L,"Joe","Notfindme");
        String view = ownerController.processFindForm(owner, bindingResult, null);
        //then                                               //setup captor when willAnswer()
        assertThat("%Notfindme%").isEqualToIgnoringCase(captor.getValue());
        assertEquals("owners/findOwners", view);
    }

    @Test
    void processFindForm_returnOneOwner() { //bdd mockito
        //given
        Owner owner=new Owner(2L,"Joe","Buck");
        //when
        String view = ownerController.processFindForm(owner, bindingResult, null);
        //then                                         //setup captor when willAnswer()
        assertThat("%Buck%").isEqualToIgnoringCase(captor.getValue());
        assertEquals("redirect:/owners/2", view);
    }

    @Test
    void processFindForm_returnNOwners() { //bdd mockito
        Owner owner=new Owner(1L,"Joe","Findme");
                                                                            //when to use mock?when any?
        String view = ownerController.processFindForm(owner, bindingResult, mock(Model.class));
        //then                                            //setup captor when willAnswer()
        assertThat("%Findme%").isEqualToIgnoringCase(captor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(view);
    }

    @Mock
    Model model;

    @Test
    void processFindForm_returnNOwners_InOrder() { //bdd mockito
        Owner owner=new Owner(1L,"Joe","Findme");
        //when to use mock?when any?
        String view = ownerController.processFindForm(owner, bindingResult, model);

        //verify 2 services->methods
        InOrder inOrder = inOrder(model, ownerService);
        //then                                            //setup captor when willAnswer()

        assertThat("%Findme%").isEqualToIgnoringCase(captor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(view);

        // define the order:
        //verify order matters
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());
    }

}