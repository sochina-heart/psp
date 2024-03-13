<template>
  <el-table :data="state.userList" style="width: 100%">
    <el-table-column fixed prop="userId" label="id" width="150"/>
    <el-table-column prop="account" label="账号" width="120"/>
    <el-table-column prop="userName" label="用户名" width="120"/>
    <el-table-column prop="userEmail" label="邮箱" width="120"/>
    <el-table-column prop="homeAddress" label="Address" width="600"/>
    <el-table-column prop="personalDescription" label="Zip" width="120"/>
    <el-table-column fixed="right" label="Operations" width="120">
      <template #default>
        <el-button link type="primary" size="small" @click=""
        >Detail
        </el-button
        >
        <el-button link type="primary" size="small">Edit</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>

import {reactive} from "vue";
import {userList} from "@/api/user";
import {onMounted} from 'vue'

onMounted(() => {
  userListData()
})

const state = reactive({
  userList: [],
  total: 0,
  loading: false,
  queryParams: {
    page: {
      pageNumber: 1,
      pageSize: 10
    },
  }
})

const userListData = () => {
  state.loading = true
  userList(state.queryParams).then((res) => {
    state.userList = res.data.records
    state.total = res.data.totalRow
    state.loading = false
  })
}
</script>
