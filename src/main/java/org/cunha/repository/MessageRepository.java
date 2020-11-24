package org.cunha.repository;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.cunha.domain.Channel;
import org.cunha.domain.Message;
import org.cunha.domain.Status;
import org.cunha.dto.MessageDTO;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessageRepository {

    public static final String INSERT = "INSERT INTO message (status, channel, send_date, recipient, content) VALUES ('NOT_SENT', $1, $2, $3, $4) RETURNING *";

    @Inject
    private PgPool client;

    public Uni<Message> getMessage(Long id) {
        return client.preparedQuery("SELECT * FROM message WHERE id = $1").execute(Tuple.of(id))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? buildMessage(iterator.next()) : null);
    }

    public Uni<Message> save(MessageDTO message) {
        return client.preparedQuery(INSERT).execute(
                Tuple.of(message.getChannel().toString(), message.getSendDate(), message.getRecipient(), message.getContent())
        )
                .onItem().transform(pgRowSet -> buildMessage(pgRowSet.iterator().next()));
    }

    public Uni<Boolean> delete(Long id) {
        return client.preparedQuery("DELETE FROM message WHERE id = $1")
                .execute(Tuple.of(id)).onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

    private Message buildMessage(Row row) {
        return Message.builder()
                .id(row.getLong("id"))
                .content(row.getString("content"))
                .recipient(row.getString("recipient"))
                .sendDate(row.getLocalDateTime("send_date"))
                .channel(Channel.valueOf(row.getString("channel")))
                .status(Status.valueOf(row.getString("status")))
                .build();
    }

}
