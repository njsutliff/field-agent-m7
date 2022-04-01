package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {
    private final SecurityClearanceRepository repository;
    private final JdbcTemplate jdbcTemplate;

    public SecurityClearanceService(SecurityClearanceRepository repository, JdbcTemplate jdbcTemplate) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SecurityClearance> findAll() {
        return repository.findAll();
    }

    public Result<SecurityClearance> findById(int securityClearanceId) {
        Result<SecurityClearance> result = new Result<>();
       if(repository.findById(securityClearanceId)== null){
           result.addMessage("Security Clearance not found!", ResultType.NOT_FOUND);
       }
        result.setPayload(repository.findById(securityClearanceId));
        return result;
    }

    public Result<SecurityClearance> add(SecurityClearance sc) {
        Result<SecurityClearance> result = validate(sc);
        if (!result.isSuccess()) {
            return result;
        }
        if (sc.getSecurityClearanceId() != 0) {
            result.addMessage("Cannot set security Clearance ID with `add`", ResultType.INVALID);
        }
        sc = repository.add(sc);
        result.setPayload(sc);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance sc) {
        Result<SecurityClearance> result = validate(sc);
        if (!result.isSuccess()){
            return result;
        }
        if(!repository.update(sc)){
            result.addMessage("Security Clearance not found", ResultType.INVALID);
        }

        return result;
    }

    public Result<SecurityClearance> deleteById(int securityClearanceId) {
        Result<SecurityClearance> result = new Result<>(); //TODO not working find if "in service"
        int count = jdbcTemplate.queryForObject(
                "select count(*) from agency_agent where security_clearance_id = ?;", Integer.class, securityClearanceId);
        if(count>0){
            result.addMessage("ID in use", ResultType.INVALID);
        }
        if (!repository.deleteById(securityClearanceId)){
            result.addMessage("Not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<SecurityClearance> validateFound(int securityClearanceId) {
        Result<SecurityClearance> result = new Result<>();
        if (!findAll().contains(findById(securityClearanceId).getPayload())){
            result.addMessage("Security Clearance not found", ResultType.NOT_FOUND);
        }
        return result;
    }

    private Result<SecurityClearance> validate(SecurityClearance toValidate) {
        Result<SecurityClearance> result = new Result<>();
        if (toValidate == null) {
            result.addMessage("Security clearance cannot be null", ResultType.INVALID);
            return result;
        }
        if (Validations.isNullOrBlank(toValidate.getName())) {
            result.addMessage("Security clearance name required.", ResultType.INVALID);
        }
        findAll().stream()
                .filter(securityClearance -> securityClearance.getName()
                        .equals(toValidate.getName()))
                .findAny().ifPresent(toNotFind -> result
                        .addMessage("Name cannot be duplicated.", ResultType.INVALID));
        return result;
    }
}
