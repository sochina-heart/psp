package com.sochina.demo.service

import com.alibaba.excel.EasyExcel
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.sochina.demo.domain.Salaries
import com.sochina.demo.mapper.SalariesMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@Service
class ExportService(
    private val salariesMapper: SalariesMapper
) {

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    fun exportExcel1(response: HttpServletResponse) {
        setExportHeader(response)

        val salaries: List<Salaries> = salariesMapper.selectList(null) as List<Salaries>

        EasyExcel.write(response.outputStream, Salaries::class.java).sheet().doWrite(salaries)
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(IOException::class)
    fun exportExcel2(response: HttpServletResponse) {
        setExportHeader(response)

        val salaries: List<Salaries> = salariesMapper.selectList(null) as List<Salaries>

        EasyExcel.write(response.outputStream, Salaries::class.java).build().use { excelWriter ->
            val writeSheet1 = EasyExcel.writerSheet(1, "模板1").build()
            val writeSheet2 = EasyExcel.writerSheet(2, "模板2").build()
            val writeSheet3 = EasyExcel.writerSheet(3, "模板3").build()

            val data1: List<Salaries> = salaries.subList(0, salaries.size / 3)
            val data2: List<Salaries> = salaries.subList(salaries.size / 3, salaries.size * 2 / 3)
            val data3: List<Salaries> = salaries.subList(salaries.size * 2 / 3, salaries.size)


            excelWriter.write(data1, writeSheet1)
            excelWriter.write(data2, writeSheet2)
            excelWriter.write(data3, writeSheet3)
        }
    }


    @Throws(IOException::class)
    fun exportExcel3(response: HttpServletResponse) {
        setExportHeader(response)

        EasyExcel.write(response.outputStream, Salaries::class.java).build().use { excelWriter ->
            val count: Long = salariesMapper.selectCount(null)
            val pages = 10
            val size = count / pages
            for (i in 0 until pages) {
                val writeSheet = EasyExcel.writerSheet(i, "模板$i").build()

                val page: Page<Salaries> =
                    Page<Salaries>()
                page.setCurrent((i + 1).toLong())
                page.setSize(size)
                val selectPage: Page<Salaries> =
                    salariesMapper.selectPage(page, null)

                excelWriter.write(selectPage.records, writeSheet)
            }
        }
    }


    @Throws(IOException::class, InterruptedException::class)
    fun exportExcel4(response: HttpServletResponse) {
        setExportHeader(response)

        val count: Long = salariesMapper.selectCount(null)

        val pages = 20
        val size = count / pages

        val executorService = Executors.newFixedThreadPool(pages)
        val countDownLatch = CountDownLatch(pages)

        val pageMap: MutableMap<Int, Page<Salaries>> = HashMap<Int, Page<Salaries>>()
        for (i in 0 until pages) {
            executorService.submit {
                val page: Page<Salaries> =
                    Page<Salaries>()
                page.setCurrent((i + 1).toLong())
                page.setSize(size)
                val selectPage: Page<Salaries> =
                    salariesMapper.selectPage(page, null)

                pageMap[i] = selectPage
                countDownLatch.countDown()
            }
        }

        countDownLatch.await()

        EasyExcel.write(
            response.outputStream,
            Salaries::class.java
        ).build().use { excelWriter ->
            for ((num, salariesPage) in pageMap) {
                val writeSheet = EasyExcel.writerSheet(num, "模板$num").build()
                excelWriter.write(salariesPage.records, writeSheet)
            }
        }
    }

    companion object {
        private const val CONTENT_TYPE: String = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

        private fun setExportHeader(response: HttpServletResponse) {
            response.contentType = CONTENT_TYPE
            response.characterEncoding = StandardCharsets.UTF_8.name()
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + "zhouyu.xlsx")
        }
    }
}
