package guru.springframework.sfgpetclinic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnnotationMockTest {

    @Mock
    Map<String, String> mapMock;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this); //init current test class
    }

    @Test
    void testAnnotationMock(){
        mapMock.put("test", "ans");
//        assertEquals(mapMock.get("test"), "ans");
    }
}
