<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-permission="['system::logs::loginlog::query']" class="search-bar">
      <div class="grid-item">
        <span>登录账号:</span>
        <el-input v-model="searchForm.username" placeholder="请输入登录账号" />
      </div>
      <div class="grid-item">
        <span>登录IP地址:</span>
        <el-input v-model="searchForm.ip" placeholder="请输入登录IP地址" />
      </div>
      <div class="grid-item">
        <span>登录地点:</span>
        <el-input v-model="searchForm.ipAddr" placeholder="请输入登录地点" />
      </div>
      <div class="grid-item">
        <span>浏览器类型:</span>
        <el-input v-model="searchForm.browser" placeholder="请输入浏览器类型" />
      </div>
      <div class="grid-item">
        <span>操作系统:</span>
        <el-input v-model="searchForm.os" placeholder="请输入操作系统" />
      </div>
      <div class="grid-item">
        <span>登录状态:</span>
        <el-select v-model="searchForm.status" placeholder="请选择">
          <el-option label="全部状态" value="" />
          <el-option label="登录失败" value="0" />
          <el-option label="登录成功" value="1" />
        </el-select>
      </div>
      <div class="grid-item">
        <span>提示消息:</span>
        <el-input v-model="searchForm.msg" placeholder="请输入提示消息" />
      </div>
      <div class="grid-item">
        <el-button v-permission="['system::logs::loginlog::query']" type="primary" icon="el-icon-search" size="small" :loading="loading" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <!-- 操作按钮栏 -->
    <div class="button-bar">
      <!-- <el-button v-permission="['system::logs::loginlog::insert']" type="primary" icon="el-icon-circle-plus-outline" size="small" plain :loading="loading" @click="handleAdd"> 新增项目 </el-button> -->
      <!-- <el-button v-permission="['system::logs::loginlog::delete']" type="danger" icon="el-icon-delete" size="small" plain :loading="loading" @click="handleRemove"> 批量删除 </el-button> -->
      <el-button v-permission="['system::logs::loginlog::query']" type="info" icon="el-icon-refresh-right" size="small" plain :loading="loading" @click="handleRefresh"> 刷新 </el-button>
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
        <el-table-column prop="ipAddr" label="登录地点" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="browser" label="浏览器类型" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="os" label="操作系统" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="status" label="登录状态" :show-overflow-tooltip="showOverflowTooltip">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === '0'" type="danger">登录失败</el-tag>
            <el-tag v-if="scope.row.status === '1'">登录成功</el-tag>
          </template>

        </el-table-column>
        <el-table-column prop="msg" label="提示消息" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="createTime" label="访问时间" :show-overflow-tooltip="showOverflowTooltip" />
        <!-- <el-table-column
          label="操作"
          width="200"
          fixed="right"
          align="center"
        >
          <template slot-scope="scope">
            <el-button :size="toolBar.size" :type="toolBar.updateType" :icon="toolBar.updateIcon" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button :size="toolBar.size" :type="toolBar.deleteType" :icon="toolBar.deleteIcon" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column> -->
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
    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="680px"
      :close-on-click-modal="false"
      size="mini"
      center
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        label-position="center"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="登录账号" prop="username">
              <el-input v-model="form.username" placeholder="请输入登录账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="登录IP地址" prop="ip">
              <el-input v-model="form.ip" placeholder="请输入登录IP地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="登录地点" prop="ipAddr">
              <el-input v-model="form.ipAddr" placeholder="请输入登录地点" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="浏览器类型" prop="browser">
              <el-input v-model="form.browser" placeholder="请输入浏览器类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作系统" prop="os">
              <el-input v-model="form.os" placeholder="请输入操作系统" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="登录状态 （0-失败 1-成功）" prop="status">
              <el-input v-model="form.status" placeholder="请输入登录状态 （0-失败 1-成功）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="提示消息" prop="msg">
              <el-input v-model="form.msg" placeholder="请输入提示消息" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageByParams, save, updateById, deleteByIds } from '@/api/system/logs/sysLoginLog'

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
        username: null, // 登录账号
        ip: null, // 登录IP地址
        ipAddr: null, // 登录地点
        browser: null, // 浏览器类型
        os: null, // 操作系统
        status: null, // 登录状态 （0-失败 1-成功）
        msg: null // 提示消息
      },
      // 表格数据
      tableData: [],
      // 当前页码
      currentPage: 1,
      // 每页数据个数
      pageSize: 10,
      // 总数据数量
      total: 0,
      // 新增 / 修改表单
      form: {
        username: null, // 登录账号
        ip: null, // 登录IP地址
        ipAddr: null, // 登录地点
        browser: null, // 浏览器类型
        os: null, // 操作系统
        status: null, // 登录状态 （0-失败 1-成功）
        msg: null // 提示消息
      },
      // 重置表单
      resetForm: {
        username: null, // 登录账号
        ip: null, // 登录IP地址
        ipAddr: null, // 登录地点
        browser: null, // 浏览器类型
        os: null, // 操作系统
        status: null, // 登录状态 （0-失败 1-成功）
        msg: null // 提示消息
      },
      // 表单校验
      rules: {
        username: [{ required: true, message: '登录账号不能为空', trigger: 'blur' }],
        ip: [{ required: true, message: '登录IP地址不能为空', trigger: 'blur' }],
        ipAddr: [{ required: true, message: '登录地点不能为空', trigger: 'blur' }],
        browser: [{ required: true, message: '浏览器类型不能为空', trigger: 'blur' }],
        os: [{ required: true, message: '操作系统不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '登录状态 （0-失败 1-成功）不能为空', trigger: 'blur' }],
        msg: [{ required: true, message: '提示消息不能为空', trigger: 'blur' }]
      }
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
    handleAdd() {
      // 新增 按钮被点击
      this.btnType = 'insert'
      this.dialogVisible = true
      this.dialogTitle = '新增登录日志'
      this.handleResetForm()
    },
    handleEdit(row) {
      // 编辑 按钮被点击
      this.btnType = 'update'
      this.dialogVisible = true
      this.dialogTitle = '修改登录日志'
      this.form = { ...row }
    },
    handleDelete(row) {
      // 删除 按钮被点击
      this.$confirm('此操作将永久删除该项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除
        this.loading = true
        deleteByIds(row.id).then(res => {
          this.loading = false
          this.$message.success('删除成功')
          this.handleSearch()
        }).catch(() => {
          this.loading = false
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    handleRemove() {
      if (this.selectedIds.length === 0) {
        this.$message.info('请选择要删除的数据')
        return
      }
      // 批量删除
      this.$confirm('此操作将永久删除选中项, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 删除
        this.loading = true
        deleteByIds(this.selectedIds).then(res => {
          this.loading = false
          this.$message.success('删除成功')
          this.handleSearch()
        }).catch(() => {
          this.loading = false
        })
      })
    },
    submitForm() {
      // 表单提交
      this.$refs.formRef.validate((valid) => {
        if (valid) {
        // 新增菜单
          if (this.btnType === 'insert') {
            this.loading = true
            save(this.form).then(res => {
              this.$message.success('新增成功')
              this.loading = false
              this.dialogVisible = false
              this.handleSearch()
            }).catch(() => {
              this.loading = false
            })
          } else if (this.btnType === 'update') {
            updateById(this.form).then(res => {
              this.$message.success('修改成功')
              this.loading = false
              this.dialogVisible = false
              this.handleSearch()
            }).catch(() => {
              this.loading = false
            })
          }
        }
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
    },
    handleResetForm() {
      // 表单重置
      this.form = { ...this.resetForm }
    }
  }
}
</script>

<style>

</style>
