package com.sochina.demo.service.impl

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.sochina.demo.domain.SochinaUser
import com.sochina.demo.mapper.SochinaUserMapper
import com.sochina.demo.service.SochinaUserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SochinaUserServiceImpl(
    private val sochinaUserMapper: SochinaUserMapper
): ServiceImpl<SochinaUserMapper, SochinaUser>(), SochinaUserService
{
    private val logger = LoggerFactory.getLogger(SochinaUserServiceImpl::class.java)
}