package com.sochina.base.domain

import java.awt.image.BufferedImage
import java.io.Serializable

class VerificationCode: Serializable {
    var image: BufferedImage? = null
    var code: String? = null
}