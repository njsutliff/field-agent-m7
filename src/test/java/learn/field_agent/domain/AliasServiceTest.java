package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class AliasServiceTest {

    @Autowired
    AliasService aliasService;
    @MockBean
    AliasRepository repository;
    @Test void shouldAdd(){
        Alias expected = makeAlias();
        aliasService.add(expected);
    }

    @Test
    void shouldFindById(){
        List<Alias> expected = repository.findByAgentId(1);
        List<Alias> actual = aliasService.findByAgentId(1).getPayload();
        System.out.println(expected.size());
        System.out.println(actual.size());
       // System.out.println(expected.size());
        //System.out.println(actual.size());

        assertEquals(expected, actual);
        assertNotNull(expected);
        assertNotNull(actual);
        //assertEquals(2, expected.size());
       // assertEquals(2, actual.size());
    }

    private Alias makeAlias() {
        Alias alias = new Alias();
        alias.setName("test");
        alias.setAgentId(1);
        return  alias;
    }
}
