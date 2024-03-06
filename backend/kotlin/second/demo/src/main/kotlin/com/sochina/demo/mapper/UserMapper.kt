package com.sochina.demo.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.sochina.demo.domain.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : BaseMapper<User> {
}