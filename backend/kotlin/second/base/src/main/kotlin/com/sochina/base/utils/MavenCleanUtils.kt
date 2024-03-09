package com.sochina.base.utils

import cn.hutool.core.date.SystemClock
import cn.hutool.core.io.FileUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.StopWatch
import java.io.File
import java.util.regex.Pattern

object MavenCleanUtils {

    private val LOGGER: Logger = LoggerFactory.getLogger(MavenCleanUtils::class.java)

    private var patternList: List<String> = emptyList()

    init {
        patternList = listOf(
            ".*lastUpdated.*",
            ".*-in-progress",
            ".*maven-metadata-.*\\.xml.*",
            ".*resolver-status\\.properties",
            ".*_remote.repositories.*",
            "unknown",
        )
    }

    private var watch = StopWatch()


    fun clean(url: File) {
        watch.start()
        // handleFile(url)
        val patterList = listOf(
            ".*lastUpdated.*",
            ".*-in-progress",
            ".*maven-metadata-.*\\.xml.*",
            ".*resolver-status\\.properties",
            ".*_remote.repositories.*",
            "unknown",
        )
        handleFile(url, patterList)
        handleEmptyDirectory(url)
        watch.stop()
        LOGGER.info("maven clean consuming time ${watch.lastTaskInfo().timeMillis} s")
    }

    fun handleFile(url: File) {
        url.listFiles()?.forEach {
            if (it.isDirectory) {
                handleFile(it)
            } else {
                if (isMatchPattern(it.name, patternList)) {
                    it.delete()
                    LOGGER.info("file ${it.absolutePath} delete")
                }
            }
        }
    }

    fun handleFile(url: File, list: List<String>) {
        url.listFiles()?.forEach {
            if (it.isDirectory) {
                handleFile(it)
            } else {
                if (isMatchPattern(it.name, list)) {
                    it.delete()
                    LOGGER.info("file ${it.absolutePath} delete")
                }
            }
        }
    }

    private fun handleEmptyDirectory(url: File) {
        url.listFiles()?.forEach {
            if (FileUtil.isDirEmpty(it)) {
                it.delete()
                LOGGER.info("folder ${it.absolutePath} delete")
            }
        }
    }

    private fun isMatchPattern(url: String, list: List<String>): Boolean {
        return StringUtils.matches(url, list)
    }
}