package org.cunha.service;

import org.cunha.domain.Channel;
import org.cunha.dto.MessageDTO;
import org.cunha.exception.InvalidScheduleDateException;
import org.cunha.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

public class MessageSchedulerServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageSchedulerService service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InvalidScheduleDateException.class)
    public void testInvalidDateMessage() {
        service.scheduleNewMessage(
                MessageDTO.builder()
                        .sendDate(LocalDateTime.MIN)
                        .recipient("rec@emailtest.com")
                        .content("Random Content")
                        .channel(Channel.EMAIL)
                        .build()
        );
    }

    @Test
    public void testValidMessage() {
        MessageDTO message = MessageDTO.builder()
                .sendDate(LocalDateTime.MAX)
                .recipient("rec@emailtest.com")
                .content("Random Content")
                .channel(Channel.EMAIL)
                .build();
        service.scheduleNewMessage(message);
        Mockito.verify(messageRepository, Mockito.times(1)).save(message);
    }

}
