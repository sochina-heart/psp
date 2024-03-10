package com.sochina.demo.service

import com.alibaba.excel.EasyExcel
import com.sochina.base.utils.thread.ThreadPoolUtils
import com.sochina.demo.domain.Salaries
import com.sochina.demo.listener.SalariesListener
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.concurrent.Callable

@Service
class ImportService(
    private val salariesListener: SalariesListener
) {

    @Throws(IOException::class)
    fun importExcel(file: MultipartFile) {
        EasyExcel.read(file.inputStream, Salaries::class.java, salariesListener).doReadAll()
    }


    fun importExcelAsync(file: MultipartFile) {

        val tasks: MutableList<Callable<Any?>> = ArrayList()
        val size = EasyExcel.read(file.inputStream).build().excelExecutor().sheetList().size
        for (i in 0 until size) {
            tasks.add(Callable<Any?> {
                EasyExcel.read(file.inputStream, Salaries::class.java, salariesListener)
                    .sheet(i).doRead()
                null
            })
        }

        try {
            ThreadPoolUtils.executorPool!!.invokeAll(tasks)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
}