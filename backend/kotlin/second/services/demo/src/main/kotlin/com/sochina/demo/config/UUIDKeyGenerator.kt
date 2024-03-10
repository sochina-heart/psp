package com.sochina.demo.config

import com.mybatisflex.core.keygen.IKeyGenerator
import com.sochina.base.utils.uuid.UuidUtils

class UUIDKeyGenerator: IKeyGenerator{
    override fun generate(p0: Any?, p1: String?): Any {
        return UuidUtils.fastSimpleUUID()
    }
}