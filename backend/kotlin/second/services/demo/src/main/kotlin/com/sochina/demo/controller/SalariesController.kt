package com.sochina.demo.controller

import com.sochina.demo.service.ExportService
import com.sochina.demo.service.ImportService
import jakarta.annotation.Resource
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.IOException


@RestController
class SalariesController {
    @Resource
    private val exportService: ExportService? = null

    @Resource
    private val importService: ImportService? = null


    @GetMapping("export1")
    @Throws(IOException::class)
    fun exportExcel1(response: HttpServletResponse?) {
        exportService!!.exportExcel1(response!!)
    }


    @GetMapping("export2")
    @Throws(IOException::class)
    fun exportExcel2(response: HttpServletResponse?) {
        exportService!!.exportExcel2(response!!)
    }


    @GetMapping("export3")
    @Throws(IOException::class)
    fun exportExcel3(response: HttpServletResponse?) {
        exportService!!.exportExcel3(response!!)
    }

    @GetMapping("export4")
    @Throws(IOException::class, InterruptedException::class)
    fun exportExcel4(response: HttpServletResponse?) {
        exportService!!.exportExcel4(response!!)
    }

    @PostMapping("import")
    @Throws(IOException::class)
    fun importExcel(file: MultipartFile?) {
//        importService.importExcel(file);
        importService!!.importExcelAsync(file!!)
    }
}
