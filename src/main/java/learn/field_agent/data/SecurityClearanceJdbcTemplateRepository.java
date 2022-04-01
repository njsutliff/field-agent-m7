package learn.field_agent.data;

import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SecurityClearanceJdbcTemplateRepository implements SecurityClearanceRepository {

    private final JdbcTemplate jdbcTemplate;
    public SecurityClearanceJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public  JdbcTemplate getJdbcTemplate(){ return jdbcTemplate;}

    @Override
    public List<SecurityClearance> findAll() {
        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance ";
        return jdbcTemplate.query(sql, new SecurityClearanceMapper());
    }

    @Override
    public SecurityClearance findById(int securityClearanceId) {

        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance "
                + "where security_clearance_id = ?;";

        SecurityClearance sc = jdbcTemplate.query(sql, new SecurityClearanceMapper(), securityClearanceId)
                .stream()
                .findFirst().orElse(null);

        return sc;

    }

    @Override
    public SecurityClearance add(SecurityClearance sc) {
        final String sql = "insert into security_clearance (name)" + "values(?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sc.getName());
            return ps;
        }, keyHolder);
        if (rowsAffected <= 0) {
            return null;
        }
        sc.setSecurityClearanceId(keyHolder.getKey().intValue());
        return sc;
    }

    @Override
    public boolean update(SecurityClearance sc) {
        final String sql =
                "update security_clearance set "
                        + "name = ? "
                + "where security_clearance_id = ?";
        // returns > 0 if success
        return jdbcTemplate.update(sql, sc.getName(), sc.getSecurityClearanceId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int securityClearanceId) {
        // returns > 0 if success
        return jdbcTemplate.update("delete from security_clearance where security_clearance_id = ?;",
                securityClearanceId) > 0;
    }
}
