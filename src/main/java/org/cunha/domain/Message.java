package org.cunha.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@RegisterForReflection
public class Message {

    private Long id;
    private LocalDateTime sendDate;
    private String recipient;
    private String content;
    private Channel channel;
    private Status status;

}
