package edu.virginia.psyc.pi.DAO;

import edu.virginia.psyc.pi.Trial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/11/14
 * Time: 11:01 AM
 *
 * Handles saving and retrieveing Trail objects
 *
 */
public class TrialDAO {

    private JdbcTemplate      jdbcTemplate;

    public TrialDAO(JdbcTemplate jdbc) {
        this.jdbcTemplate = jdbc;
    }

    //no need to set datasource here
    public void insert(final Trial trial){
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int trialId;
        String stimuliSQL, mediaSQL, dataSQL;

        final String sql = "INSERT INTO TRIAL " +
                "(LOG_SERIAL, TRIAL_ID, NAME, RESPONSE_HANDLE, LATENCY) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setInt(1, trial.getLog_serial());
                ps.setInt(2, trial.getTrial_id());
                ps.setString(3, trial.getName());
                ps.setString(4, trial.getResponseHandle());
                ps.setInt(5, trial.getLatency());
                return ps;
            }
        }, keyHolder);
        trialId = keyHolder.getKey().intValue();

        stimuliSQL = "INSERT INTO TRIAL_STIMULI(TRIAL_ID, STIMULI) VALUES (?,?)";
        for(String stimuli : trial.getStimuli()) {
            jdbcTemplate.update(stimuliSQL, trialId, stimuli);
        }
    }

    public List<Trial> getTrials() {
        ResultSet rs;

        List<Trial> trials = jdbcTemplate.query(
                "select id, LOG_SERIAL, TRIAL_ID, NAME, RESPONSE_HANDLE, LATENCY from trial",
                new ProductMapper());
/*
        try {
            for(Trial t : trials) {
               rs = jdbcTemplate.query("select STIMULI from TRIAL_STIMULI where TRIAL_ID = ?", t.getId());
               while(rs.next()) {
                   t.addStimuli(rs.getString(0));
               }
             }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
*/
        return trials;
    }
    
    private static class ProductMapper implements ParameterizedRowMapper<Trial> {

        public Trial mapRow(ResultSet rs, int rowNum) throws SQLException {
            Trial trial = new Trial();
            trial.setId(rs.getInt("ID"));
            trial.setLog_serial(rs.getInt("LOG_SERIAL"));
            trial.setTrial_id(rs.getInt("TRIAL_ID"));
            trial.setName(rs.getString("name"));
            trial.setResponseHandle(rs.getString("RESPONSE_HANDLE"));
            trial.setLatency(rs.getInt("LATENCY"));

            return trial;
        }
    }
}