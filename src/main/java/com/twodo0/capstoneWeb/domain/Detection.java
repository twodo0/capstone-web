package com.twodo0.capstoneWeb.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Detection {
    private String label;
    private double prob;
    private int x; private int y; //최상단
    private int w; private int h; //너비 높이
}
