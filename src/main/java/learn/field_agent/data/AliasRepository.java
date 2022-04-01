package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AliasRepository {
        List<Alias> findByAgentId(int agentId);
        Alias add(Alias alias);
        boolean update(Alias alias);
        @Transactional
        boolean deleteById(int aliasId);

        JdbcOperations getJdbcTemplate();
}
