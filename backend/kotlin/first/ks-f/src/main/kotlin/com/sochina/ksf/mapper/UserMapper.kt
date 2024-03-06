package com.sochina.ksf.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.sochina.ksf.domain.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : BaseMapper<User> {
}