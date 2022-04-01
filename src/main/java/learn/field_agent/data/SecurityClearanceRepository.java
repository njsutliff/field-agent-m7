package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SecurityClearanceRepository {
    JdbcTemplate getJdbcTemplate();
    List<SecurityClearance> findAll();
    SecurityClearance findById(int securityClearanceId);
    SecurityClearance add(SecurityClearance sc);
    boolean update(SecurityClearance sc);
    @Transactional //TODO figure out what this means
    boolean deleteById(int securityClearanceId);
}
