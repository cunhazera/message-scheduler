package org.cunha.config;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class PostgresContainer implements QuarkusTestResourceLifecycleManager {

    private static final int PORT = 5433;
    private static final PostgreSQLContainer DATABASE = new PostgreSQLContainer<>("postgres:10.5")
            .withDatabaseName("message_db")
            .withUsername("message_user")
            .withPassword("message_pass");

    @Override
    public Map<String, String> start() {
        Consumer<CreateContainerCmd> cmd = e -> e.withPortBindings(new PortBinding(Ports.Binding.bindPort(PORT), new ExposedPort(5432)));
        DATABASE.withCreateContainerCmdModifier(cmd);
        DATABASE.start();
        return Collections.singletonMap("quarkus.datasource.jdbc.url", DATABASE.getJdbcUrl());
    }

    @Override
    public void stop() {
        DATABASE.stop();
    }
}
