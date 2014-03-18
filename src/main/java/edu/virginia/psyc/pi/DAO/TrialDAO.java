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
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 3/11/14
 * Time: 11:01 AM
 *
 * Handles saving and retrieving Trail objects
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

        mediaSQL = "INSERT INTO TRIAL_MEDIA(TRIAL_ID, MEDIA) VALUES (?,?)";
        for(String media : trial.getMedia()) {
            jdbcTemplate.update(mediaSQL, trialId, media);
        }

        dataSQL = "INSERT INTO TRIAL_DATA(TRIAL_ID, NAME, VALUE) VALUES (?,?,?)";
        for(String key : trial.getData().keySet()) {
            jdbcTemplate.update(dataSQL, trialId, key, trial.getData().get(key));
        }

    }

    public List<Trial> getTrials() {
        List<String> stimuli;
        String selectTrial   = "select id, LOG_SERIAL, TRIAL_ID, NAME, RESPONSE_HANDLE, LATENCY from trial";
        String selectStimuli = "select STIMULI from TRIAL_STIMULI where TRIAL_ID = ?";
        String selectMedia   = "select MEDIA from TRIAL_MEDIA where TRIAL_ID = ?";
        String selectData    = "select NAME,VALUE from TRIAL_DATA where TRIAL_ID = ?";

        List<Trial> trials = jdbcTemplate.query(selectTrial, new TrialMapper());
        for(Trial t : trials) {
            t.setStimuli(jdbcTemplate.query(selectStimuli, new StringMapper("STIMULI"), t.getId()));
            t.setMedia(jdbcTemplate.query(selectMedia, new StringMapper("MEDIA"), t.getId()));
            List<Map<String,Object>> results = jdbcTemplate.queryForList(selectData, t.getId());
            for (Map m : results) t.addData((String)m.get("NAME"), m.get("VALUE"));
        }
        return trials;
    }

    /**
     * Maps an SQL Result row back to a Trial.
     */
    private static class TrialMapper implements ParameterizedRowMapper<Trial> {

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

    /**
     * Maps an SQL Result row back to a single string, looking for the given field name.
     */
    private static class StringMapper implements ParameterizedRowMapper<String> {
        private String field;

        public StringMapper(String field) {
            super();
            this.field = field;
        }

        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(field);
        }
    }


}