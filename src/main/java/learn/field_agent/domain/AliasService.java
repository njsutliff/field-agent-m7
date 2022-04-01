package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {

    private final AliasRepository repository;

    public AliasService(AliasRepository repository) {
        this.repository = repository;
    }

    public Result<List<Alias>> findByAgentId(int agentId) {
        Result<List<Alias>> result = new Result<>();
        result.setPayload(repository.findByAgentId(agentId));
        return result;
    }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }
        if (alias.getId() != 0) {
            result.addMessage("Alias id cannot be set during add. ", ResultType.INVALID);
            return result;
        }
/*
        Integer value1 = repository.getJdbcTemplate().queryForObject(
                "select count(*) from agent where agent_id = ?", Integer.class, alias.getAgentId());
        if (value1 != null && !(value1 > 0)) {
            result.addMessage("Agent ID not found. Need a valid agent ID to add an alias.", ResultType.INVALID);
            return result;
        }*/

        if(findByAgentId(alias.getAgentId()).getPayload().contains(alias)
        && Validations.isNullOrBlank(alias.getPersona())){
            result.addMessage("Persona is required with a duplicate name. ", ResultType.INVALID);
        }
        // require persona if name duplicated
        alias = repository.add(alias);
        result.setPayload(alias);
        return result;
    }

//  For PUT return a 400 if the alias fails one of the domain rules
    public Result<Alias> update(Alias agent) {
        Result<Alias> result = validate(agent);
        if (!result.isSuccess()) {
            return result;
        }

        if (agent.getAgentId() <= 0) {
            result.addMessage("agentId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(agent)) {
            String msg = String.format("agentId: %s, not found", agent.getAgentId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return repository.deleteById(aliasId);
    }

    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();
        if (alias == null) {
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }
        if(alias.getAgentId()<= 0){
            result.addMessage("Invalid agent ID", ResultType.INVALID);
        }
        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("Name is required", ResultType.INVALID);
            return result;
        }
        return result;
    }
}
