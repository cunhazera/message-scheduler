package org.cunha.application;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.pgclient.PgPool;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.logging.Logger;

//@ApplicationScoped
public class InitDb {

    public static final String INIT_DB_QUERY = "CREATE TABLE IF NOT EXISTS message (id SERIAL PRIMARY KEY, send_date TIMESTAMP, recipient TEXT NOT NULL, content TEXT NOT NULL, status TEXT NOT NULL, channel TEXT NOT NULL)";

    private static final Logger LOGGER = Logger.getLogger("App");

    @Inject
    PgPool client;

    void onStart(/*@Observes StartupEvent ev*/) {
        LOGGER.info("The application is starting...");
        client.query(INIT_DB_QUERY).execute()
                .await()
                .indefinitely();
    }

    void onStop(/*@Observes ShutdownEvent ev*/) {
        LOGGER.info("The application is stopping...");
    }

}
