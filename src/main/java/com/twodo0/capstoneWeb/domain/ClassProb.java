package com.twodo0.capstoneWeb.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassProb {
    private String label;
    private double prob;
}
