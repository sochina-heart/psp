package com.sochina.base.utils

import cn.hutool.core.date.SystemClock
import cn.hutool.core.io.FileUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.regex.Pattern

object MavenCleanUtils {

    private val LOGGER: Logger = LoggerFactory.getLogger(MavenCleanUtils::class.java)

    private val LAST_UPDATED_PATTERN: Pattern = Pattern.compile(".*lastUpdated.*")
    private val IN_PROGRESS_PATTERN: Pattern = Pattern.compile(".*-in-progress")
    private val METADATA_XML_PATTERN: Pattern = Pattern.compile(".*maven-metadata-.*\\.xml.*")
    private val RESOLVER_STATUS_PATTERN: Pattern = Pattern.compile(".*resolver-status\\.properties")
    private val REMOTE_REPOSITORIES_PATTERN: Pattern = Pattern.compile(".*_remote.repositories.*")
    private val UN_KNOWN_PATTERN: Pattern = Pattern.compile("unknown")


    fun clean(url: File) {
        val start = SystemClock.now()
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
        val end = SystemClock.now()
        LOGGER.info("maven clean consuming time ${(end - start) / 1000} s")
    }

    fun handleFile(url: File) {
        url.listFiles()?.forEach {
            if (it.isDirectory) {
                handleFile(it)
            } else {
                if (isMatchPattern(it.name)) {
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

    private fun isMatchPattern(url: String): Boolean {
        return LAST_UPDATED_PATTERN.matcher(url).matches() ||
                IN_PROGRESS_PATTERN.matcher(url).matches() ||
                METADATA_XML_PATTERN.matcher(url).matches() ||
                RESOLVER_STATUS_PATTERN.matcher(url).matches() ||
                REMOTE_REPOSITORIES_PATTERN.matcher(url).matches() ||
                UN_KNOWN_PATTERN.matcher(url).matches()
    }

    private fun isMatchPattern(url: String, list: List<String>): Boolean {
        return StringUtils.matches(url, list)
    }
}