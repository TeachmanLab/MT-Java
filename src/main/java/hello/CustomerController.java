package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dan
 * Date: 2/27/14
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class CustomerController {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerController(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @RequestMapping("/customers")
    public @ResponseBody List<Customer> customerList() {
        List<Customer> results = jdbcTemplate.query(
                "select * from customers where first_name = ?", new Object[] { "Dan" },
                new RowMapper<Customer>() {
                    @Override
                    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Customer(rs.getLong("id"), rs.getString("first_name"),
                                rs.getString("last_name"));
                    }
                });

        return results;
    }

}
