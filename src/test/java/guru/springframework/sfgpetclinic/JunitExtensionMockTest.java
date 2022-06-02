package guru.springframework.sfgpetclinic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

//to init a mock
@ExtendWith(MockitoExtension.class)
public class JunitExtensionMockTest {
    @Mock
    Map<String, Object> mapMock;

    @Test
    void testMock(){
        mapMock.put("keyvalue","foo");
    }
}
