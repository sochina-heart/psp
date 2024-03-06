package com.sochina.base.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
    private BufferedImage image;
    private String code;
}
