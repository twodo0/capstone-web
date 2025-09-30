package com.twodo0.capstoneWeb.dto;

import com.twodo0.capstoneWeb.domain.enums.DamageType;

public record BoxDto(
        DamageType damageType, double prob,
                     int x, int y, int w, int h) {}