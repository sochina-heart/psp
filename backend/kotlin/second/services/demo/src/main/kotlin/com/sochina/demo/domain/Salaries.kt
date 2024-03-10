package com.sochina.demo.domain

import com.mybatisflex.annotation.Column
import java.io.Serializable
import java.util.Date

class Salaries: Serializable {
    @Column("emp_no")
    var empNo: Int? = null

    var salary: Int? = null

    @Column("from_data")
    var fromDate: Date? = null

    @Column("to_data")
    var toDate: Date? = null
}
