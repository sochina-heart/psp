package com.sochina.demo.mapper

import com.mybatisflex.core.BaseMapper
import com.sochina.demo.domain.SochinaUser
import org.apache.ibatis.annotations.Mapper

@Mapper
interface SochinaUserMapper: BaseMapper<SochinaUser> {
}