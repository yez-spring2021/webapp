package com.zhenyuye.webapp.dtos.bookDto;

import com.zhenyuye.webapp.annotation.ValidImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class ImageDTO {
    @ValidImage
    @NotNull
    private MultipartFile image;
}
