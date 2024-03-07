package com.sochina.base.domain

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.awt.image.BufferedImage

class VerificationCode {
    var image: BufferedImage? = null
    var code: String? = null
}