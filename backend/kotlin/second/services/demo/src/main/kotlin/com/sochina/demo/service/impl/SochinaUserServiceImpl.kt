package com.sochina.demo.service.impl

import com.mybatisflex.spring.service.impl.ServiceImpl
import com.sochina.demo.domain.SochinaUser
import com.sochina.demo.mapper.SochinaUserMapper
import com.sochina.demo.service.ISochinaUserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SochinaUserServiceImpl(

):ServiceImpl<SochinaUserMapper, SochinaUser>(), ISochinaUserService
{
    private val logger = LoggerFactory.getLogger(SochinaUserServiceImpl::class.java)
}