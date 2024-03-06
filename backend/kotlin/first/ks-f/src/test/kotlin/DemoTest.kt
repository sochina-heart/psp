import com.sochina.base.properties.CommonsProperties
import com.sochina.base.utils.Base64Utils
import com.sochina.base.utils.MavenCleanUtils
import com.sochina.base.utils.StringToHexUtils
import com.sochina.base.utils.encrypt.gm.sm2.SM2Utils
import com.sochina.base.utils.thread.runnable.Pooled
import com.sochina.base.utils.thread.runnable.TaskToolExecutor
import com.sochina.base.utils.thread.runnable.Worker
import com.sochina.base.utils.uuid.UuidUtils
import com.sochina.ksf.KsFApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.concurrent.CountDownLatch

@SpringBootTest(classes = [KsFApplication::class])
@RunWith(SpringRunner::class)
class DemoTest {

    @Test
    fun hexToStringDemo() {
        val stringToHexString = StringToHexUtils.stringToHexString("hello sochina")
        println(stringToHexString)
        val hexStringToString = StringToHexUtils.hexStringToString(stringToHexString)
        println("hexStringToString is $hexStringToString")
    }

    @Test
    fun base64Demo() {
        val str: String = "hello sochina"
        val decode = Base64Utils.decode(str)
        if (decode != null) {
            val a = String(decode, StandardCharsets.UTF_8)
            println(a)
        }
        val encode = Base64Utils.encode(decode)
        println(encode)
    }

    @Test
    fun test() {
        testDisPathTask()
    }

    fun createTestWorker(i: Int, countDownLatch: CountDownLatch): Worker<Any> {
        return Worker(Thread {
            println("my is task -- $i --- ${Thread.currentThread()}")
            countDownLatch.countDown()
        }, Pooled.PoolOverAct.NEW_THREAD)
    }

    fun testDisPathTask() {
        val ciToolExecutor = TaskToolExecutor()
        ciToolExecutor.name = "ciToolExecutor"
        ciToolExecutor.coreSize = 8
        ciToolExecutor.maxSize = 16
        ciToolExecutor.queueSize = 512
        ciToolExecutor.init()
        val countDownLatch = CountDownLatch(3)
        for (i in 0..2) {
            val worker = createTestWorker(i, countDownLatch)
            ciToolExecutor.execute(worker)
        }
        countDownLatch.await()
        println("-------------")
    }

    @Test
    fun associatedDemo() {
        val items = mutableMapOf<String, Array<String>>()
        items["1"] = arrayOf("1", "2")
        items["2"] = arrayOf("")
        items["3"] = emptyArray()
        println(items.entries.filter { it.key != null }
            .associateBy({ it.key }, { it.value.joinToString(separator = ",") }))
        println(items.entries.filter { it.key != null }
            .associateBy({ it.key }, {
                it.value.fold("") { acc, item ->
                    if (acc.isEmpty()) {
                        item
                    } else {
                        "$acc,$item"
                    }
                }
            }))
        println(items.entries.mapNotNull { entry ->
            val joinedValue = entry.value.fold("") { accumulator, value ->
                if (accumulator.isEmpty()) {
                    value
                } else {
                    "$accumulator,$value"
                }
            }
            if (joinedValue.isNotEmpty()) {
                entry.key to joinedValue
            } else {
                null
            }
        }.toMap())
        val associate4 =
            items.entries
                .associateBy({ it.key }, {
                    it.value.fold("") { acc, item ->
                        if (acc.isEmpty()) {
                            item
                        } else {
                            "$acc,$item"
                        }
                    }
                })
        println(associate4)
    }

    private lateinit var commonsProperties: CommonsProperties

    @Autowired
    fun setCommonsProperties(commonsProperties: CommonsProperties) {
        this.commonsProperties = commonsProperties
    }

    @Test
    fun mavenCleanDemo() {
        val url = "D:\\Program Files\\apache-maven-3.8.6\\repository"
        MavenCleanUtils.clean(File(url))
    }
}