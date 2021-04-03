package com.zhenyuye.webapp.dtos.snsMsgDto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookCreateMessageDTO {
    private String bookId;
    private String bookName;
    private String email;
    private String link;
    private String type = "DELETE";
}
