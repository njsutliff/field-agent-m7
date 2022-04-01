package learn.field_agent.domain;

import learn.field_agent.data.AgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class SecurityClearanceServiceTest {
    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test void testFindAll(){
        System.out.println(service.findAll().size());
    }
    @Test void findGuy(){
        SecurityClearance expected = makeClearance();
        when(repository.findById(1)).thenReturn(expected);
        SecurityClearance actual = service.findById(1).getPayload();
        assertEquals(expected, actual);
    }
    //Fails because tried to set ID with add.
    @Test void testFailAdd() {
        SecurityClearance sc = makeClearance();
        SecurityClearance expected = makeClearance();
        Result<SecurityClearance> result = service.add(sc);
        when(repository.add(sc)).thenReturn(expected);

        assertEquals(ResultType.INVALID, result.getType());

    }
        // Only SUCCESS if ID not set
    @Test void testSuccessAdd(){
        SecurityClearance sc = makeClearance();
        SecurityClearance expected = makeClearance();
        sc.setSecurityClearanceId(0);

        when(repository.add(sc)).thenReturn(expected);
        Result<SecurityClearance> result = service.add(sc);

        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(expected, result.getPayload());


    }
    @Test void updateSuccess(){
        SecurityClearance toUpdate = makeClearance();
        toUpdate.setName("New guy");
        when(repository.update(toUpdate)).thenReturn(true);
        Result<SecurityClearance> result = service.update(toUpdate);
        assertEquals(ResultType.SUCCESS, result.getType());
    }
    @Test void updateFail() {
        SecurityClearance toUpdate = makeClearance();
        toUpdate.setName(null);
        when(repository.update(toUpdate)).thenReturn(true);
        Result<SecurityClearance> result = service.update(toUpdate);
        assertEquals(ResultType.INVALID, result.getType());

    }
        SecurityClearance makeClearance() {
        SecurityClearance sc = new SecurityClearance();
        sc.setSecurityClearanceId(1);
        sc.setName("test");
        return sc;
    }

}
