package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasJdbcTemplateRepositoryTest {
    @Autowired
    AliasJdbcTemplateRepository repository;
    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }
    @Test
    void shouldAdd() {
        Alias test = makeAlias();
        Alias actual = repository.add(test);
        assertEquals(test, actual);
    }

    @Test
    void shouldFindByAgentId() {
        List<Alias> list = repository.findByAgentId(1);
        System.out.println(list.get(0));
        System.out.println(list.get(0).getAgentId());
         assertNotNull(list);
         assertEquals(list.size(), 2); // should have Test in the sql now
    }

    @Test
    void shouldUpdate() {
        Alias toUpdate = makeAlias();
        toUpdate.setId(1);
        toUpdate.setName("new");
        assertTrue(repository.update(toUpdate));
    }



    @Test
    void deleteById() {
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(1));

    }
    private Alias makeAlias() {
        Alias alias = new Alias();
        alias.setName("test");
        alias.setAgentId(1);
        return  alias;
    }
}
