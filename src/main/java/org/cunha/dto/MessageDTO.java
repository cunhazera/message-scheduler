package org.cunha.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cunha.domain.Channel;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDTO {

    private LocalDateTime sendDate;
    private String recipient;
    private String content;
    private Channel channel;

}
