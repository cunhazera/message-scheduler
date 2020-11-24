package org.cunha.service;

import io.smallrye.mutiny.Uni;
import org.cunha.domain.Message;
import org.cunha.dto.MessageDTO;
import org.cunha.exception.InvalidScheduleDateException;
import org.cunha.repository.MessageRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class MessageSchedulerService {

    @Inject
    private MessageRepository repository;

    public Uni<Message> scheduleNewMessage(MessageDTO message) {
        if (message.getSendDate().isBefore(LocalDateTime.now())) {
            throw new InvalidScheduleDateException(message.getSendDate());
        }
        return repository.save(message);
    }

    public Uni<Message> getMessage(Long id) {
        return repository.getMessage(id);
    }

    public Uni<Boolean> deleteMessage(Long id) {
        return repository.delete(id);
    }
}
