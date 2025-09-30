package com.twodo0.capstoneWeb.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadResponseDto {
    private Long imageId;
    private double width;
    private double height;
}
