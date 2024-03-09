package com.sochina.demo.listener

import com.alibaba.excel.context.AnalysisContext
import com.alibaba.excel.read.listener.ReadListener
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.sochina.demo.domain.Salaries
import com.sochina.demo.mapper.SalariesMapper
import jakarta.annotation.Resource
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@Component
class SalariesListener(
    @Lazy private val salariesListener: SalariesListener?
) : ServiceImpl<SalariesMapper?, Salaries?>(),
    ReadListener<Salaries>, IService<Salaries?> {
    private val executorService: ExecutorService = Executors.newFixedThreadPool(20)

    private val salariesList: ThreadLocal<java.util.ArrayList<Salaries>> =
        ThreadLocal.withInitial { ArrayList() }

    @Transactional(rollbackFor = [Exception::class])
    override fun invoke(data: Salaries, context: AnalysisContext) {
//        saveOne(data);

        salariesList.get().add(data)
        if (salariesList.get().size >= batchSize) {
//            saveData();
            asyncSaveData()
        }
    }

    fun saveOne(data: Salaries?) {
        save(data)
        logger.info("第" + count.getAndAdd(1) + "次插入1条数据")
    }

    fun saveData() {
        if (salariesList.get().isNotEmpty()) {
            saveBatch(salariesList.get() as Collection<Salaries?>?, salariesList.get().size)
            logger.info("第" + count.getAndAdd(1) + "次插入" + salariesList.get().size + "条数据")
            salariesList.get().clear()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun asyncSaveData() {
        if (salariesList.get().isNotEmpty()) {
            val salaries: java.util.ArrayList<Salaries> = salariesList.get().clone() as java.util.ArrayList<Salaries>
            executorService.execute(SaveTask(salaries, salariesListener))
            salariesList.get().clear()
        }
    }

    @Transactional(rollbackFor = [Exception::class])
    override fun doAfterAllAnalysed(context: AnalysisContext) {
        logger.info("一个Sheet全部处理完")
        if (salariesList.get().size >= batchSize) {
            saveData()
        }
    }

    internal class SaveTask(
        salariesList: List<Salaries>,
        private val salariesListener: SalariesListener?
    ) :
        Runnable {
        private val salariesList: List<Salaries> = salariesList

        override fun run() {
            salariesListener!!.saveBatch(salariesList)
            logger.info("第" + count.getAndAdd(1) + "次插入" + salariesList.size + "条数据")
        }
    }

    companion object {
        private val logger: Log = LogFactory.getLog(
            SalariesListener::class.java
        )

        private val count = AtomicInteger(1)
        private const val batchSize = 10000
    }
}