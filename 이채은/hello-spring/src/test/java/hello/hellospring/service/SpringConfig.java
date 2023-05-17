package hello.hellospring.service; // spring.config => 조립하는 파트


import javax.sql.DataSource;

public class SpringConfig {
    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}