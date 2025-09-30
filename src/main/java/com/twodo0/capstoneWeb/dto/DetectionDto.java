package com.twodo0.capstoneWeb.dto;

import com.twodo0.capstoneWeb.domain.enums.DamageType;

import java.util.List;

public record DetectionDto(List<ClassProbDto> classProbDto, int x, int y, int w, int h) {}