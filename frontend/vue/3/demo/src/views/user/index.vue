<template>
  <el-table :data="state.userList" style="width: 100%">
    <el-table-column fixed prop="userId" label="id" width="150"/>
    <el-table-column prop="account" label="账号" width="120"/>
    <el-table-column prop="userName" label="用户名" width="120"/>
    <el-table-column prop="userEmail" label="邮箱" width="120"/>
    <el-table-column prop="homeAddress" label="Address" width="120"/>
    <el-table-column prop="personalDescription" label="Zip" width="120"/>
    <el-table-column label="Operations" width="360">
      <template #default="scope">
        <el-button type="primary" :icon="Plus"/>
        <el-button type="primary" :icon="Edit"/>
        <el-button
            type="primary"
            :icon="Delete"
            @click="handleDelete(scope.row)"/>
      </template>
    </el-table-column>
  </el-table>
</template>

<script lang="ts" setup>

import {reactive, onMounted} from "vue"
import {removeUser, userList} from "@/api/user"
import {Delete, Edit, Plus} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from "element-plus";

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

const handleDelete = (row: any) => {
  const id = row.userId
  const ids: string[] = [];
  ids.push(id);
  ElMessageBox.confirm('是否确认用户编号为"' + ids + '"的数据项？').then(function() {
    return removeUser(ids);
  }).then(() => {
    userListData();
    ElMessage({
      message: '删除成功！',
      type: 'success',
    });
  })
}
</script>
