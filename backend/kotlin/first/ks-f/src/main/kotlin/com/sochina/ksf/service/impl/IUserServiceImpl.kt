package com.sochina.ksf.service.impl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.sochina.ksf.domain.User
import com.sochina.ksf.mapper.UserMapper
import com.sochina.ksf.service.IUserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class IUserServiceImpl(
    private val userMapper: UserMapper
) : ServiceImpl<UserMapper, User>(), IUserService {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(IUserServiceImpl::class.java)
    }
}