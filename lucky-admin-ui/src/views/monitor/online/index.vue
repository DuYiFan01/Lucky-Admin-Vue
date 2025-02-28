<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-permission="['monitor::online::query']" class="search-bar">
      <div class="grid-item">
        <span>登录账号:</span>
        <el-input v-model="searchForm.username" placeholder="请输入登录账号" />
      </div>
      <div class="grid-item">
        <el-button v-permission="['monitor::online::query']" type="primary" icon="el-icon-search" size="small" :loading="loading" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <!-- 操作按钮栏 -->
    <div class="button-bar">
      <el-button v-permission="['monitor::online::query']" type="info" icon="el-icon-refresh-right" size="small" plain :loading="loading" @click="handleRefresh"> 刷新 </el-button>
    </div>
    <!-- 表格 -->
    <div class="table-bar">
      <el-table
        v-loading="loading"
        :data="tableData"
        max-height="560px"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" width="50" label="序号" />
        <el-table-column prop="username" label="登录账号" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="ip" label="登录IP地址" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="address" label="登录地点" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="userAgent" label="浏览器类型" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="os" label="操作系统" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="loginTime" label="登录时间" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column
          v-if="checkPermission(['monitor::online::kickout'])"
          label="操作"
          width="150"
          align="center"
        >
          <template slot-scope="scope">
            <el-button :size="toolBar.size" :type="toolBar.deleteType" :icon="toolBar.deleteIcon" @click="handleDelete(scope.row)">强制下线</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          :current-page="currentPage"
          :page-sizes="[1, 10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { pageByParams, forceLogout } from '@/api/monitor/online'

export default {
  data() {
    return {
      // 页面loading
      loading: false,
      // 表格的单元格字数过长提示的开关
      showOverflowTooltip: true,
      // 弹窗标题
      dialogTitle: '',
      // 是否显示弹窗
      dialogVisible: false,
      // 按钮点击类型
      btnType: '', // insert / update
      // 表格复选框选中的Ids
      selectedIds: [],
      // 搜索条件
      searchForm: {
        username: null // 登录账号
      },
      // 表格数据
      tableData: [],
      // 当前页码
      currentPage: 1,
      // 每页数据个数
      pageSize: 10,
      // 总数据数量
      total: 0
    }
  },
  created() {
    this.handleInit()
  },
  methods: {
    handleInit() {
      // 画面加载完毕
      this.loading = true
      pageByParams({}, this.currentPage, this.pageSize).then(res => {
        const { data } = res
        this.tableData = data.data
        this.total = data.total
        this.currentPage = data.currentPage
        this.pageSize = data.pageSize
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    handleSearch() {
      // 搜索 按钮被点击
      this.loading = true
      pageByParams(this.searchForm, this.currentPage, this.pageSize).then(res => {
        const { data } = res
        this.tableData = data.data
        this.total = data.total
        this.currentPage = data.currentPage
        this.pageSize = data.pageSize
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleDelete(row) {
      // 删除 按钮被点击
      this.$confirm('此操作下线当前选择用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除
        this.loading = true
        forceLogout(row.token).then(res => {
          this.loading = false
          this.$message.success('下线成功')
          this.handleSearch()
        }).catch(() => {
          this.loading = false
        })
      }).catch(() => {
        this.$message.info('已取消操作')
      })
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize
      // console.log("页面显示条数被改变：" + newSize + "条");
      this.handleSearch()
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage
      // console.log("页码被改变：" + newPage);
      this.handleSearch()
    },
    handleSelectionChange(selection) {
      // 表格复选框被勾选
      this.selectedIds = selection.map(obj => obj.id)
    },
    handleRefresh() {
      // 刷新 按钮被点击\
      this.handleSearch()
    }
  }
}
</script>

  <style>

  </style>
