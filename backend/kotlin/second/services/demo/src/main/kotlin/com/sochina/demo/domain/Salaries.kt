package com.sochina.demo.domain

import com.baomidou.mybatisplus.annotation.TableField
import java.util.*

class Salaries {
    @TableField("emp_no")
    var empNo: Int? = null

    var salary: Int? = null

    @TableField("from_data")
    var fromDate: Date? = null

    @TableField("to_data")
    var toDate: Date? = null
}
