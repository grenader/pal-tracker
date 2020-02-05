package io.pivotal.pal.tracker;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    private RowMapper<TimeEntry> timeEntryRowMapper = (ResultSet result, int rowNum) -> {
        var timeEntry = new TimeEntry(
                result.getLong("id"),
                result.getLong("project_id"),
                result.getLong("user_id"),
                result.getDate("date").toLocalDate(),
                result.getInt("hours"));

        return timeEntry;
    };


    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        final int newId = jdbcTemplate.queryForObject(
                "insert into time_entries(project_id, user_id, date, hours) values(?,?,?,?) RETURNING id",
                new Object[]{timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours()}, Integer.class);

        timeEntry.setId(newId);

        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        try {
            return jdbcTemplate.queryForObject(
                    "select * from time_entries where id = ?",
                    new Object[]{id}, timeEntryRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<TimeEntry> list() {
        return jdbcTemplate.query("select * from time_entries order by id",
                new Object[]{}, timeEntryRowMapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update("update time_entries set id = ?, project_id = ?, user_id = ?, date = ?, hours = ? where id = ?",
                id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours(), id);
        timeEntry.setId(id);
        return timeEntry;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(
                "delete from time_entries where id = ?", id);
    }
}
