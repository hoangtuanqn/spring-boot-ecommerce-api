package mst.local.mstsoftware;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class BaseController {

    private final JdbcTemplate jdbcTempalte;

    public BaseController(JdbcTemplate jdbcTempalte) {
        this.jdbcTempalte = jdbcTempalte;
    }

    @GetMapping("test")
    public String test() {
        String sql = "CREATE TABLE IF NOT EXISTS test_table ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(255)"
                + ")";

        jdbcTempalte.execute(sql);
        return sql;
    }
}
