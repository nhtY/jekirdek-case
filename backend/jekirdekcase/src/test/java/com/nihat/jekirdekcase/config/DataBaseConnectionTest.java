package com.nihat.jekirdekcase.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class DataBaseConnectionTest {
    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnectionAndName() throws Exception {
        // Get a connection from the DataSource
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT DATABASE()")) {

            // Check if the connection is valid
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(2)).isTrue();

            // Verify the database name
            if (resultSet.next()) {
                String currentDbName = resultSet.getString(1);
                assertThat(currentDbName).isEqualTo("jekirdek");
            } else {
                throw new RuntimeException("Failed to retrieve database name.");
            }
        }
    }
}
