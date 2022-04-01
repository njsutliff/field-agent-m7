package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {
//TODO known good state not working yet
    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }
    @Test
    void shouldFindAll(){
        List<SecurityClearance> list = repository.findAll();
        assertTrue(list.size()> 0);
    }
    @Test
    void shouldNotFindNotExisting(){
        List<SecurityClearance> list = repository.findAll();
        SecurityClearance russianSpy = new SecurityClearance(3, "Russian spy");
        assertFalse(list.contains(russianSpy));
    }
    @Test
    void shouldFindSecret() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(10);
        assertNull(actual);
    }
    @Test void testAdd(){
        SecurityClearance testAdd = makeSC();
        SecurityClearance actual = repository.add(testAdd);
        assertNotNull(actual);
        assertEquals(3, actual.getSecurityClearanceId());
    }
    @Test void testUpdate(){
        SecurityClearance lessSecret = makeSC();
        lessSecret.setSecurityClearanceId(3);
        assertTrue(repository.update(lessSecret));
    }
    @Test void shouldDelete(){
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(2));

    }
    private  SecurityClearance makeSC(){
        SecurityClearance sc = new SecurityClearance();
        sc.setName("test");
        return sc;
    }

    }