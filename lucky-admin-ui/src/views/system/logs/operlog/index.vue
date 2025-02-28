<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-permission="['system::logs::operlog::query']" class="search-bar">
      <div class="grid-item">
        <span>模块标题:</span>
        <el-input v-model="searchForm.title" placeholder="请输入模块标题" />
      </div>
      <div class="grid-item">
        <span>业务类型:</span>
        <el-select v-model="searchForm.businessType" placeholder="请选择">
          <el-option
            v-for="item in businessTypeSelectOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="grid-item">
        <span>方法名称:</span>
        <el-input v-model="searchForm.method" placeholder="请输入方法名称" />
      </div>
      <div class="grid-item">
        <span>请求方式:</span>
        <el-select v-model="searchForm.requestMethod" placeholder="请选择">
          <el-option label="全部" value="" />
          <el-option label="GET" value="GET" />
          <el-option label="POST" value="POST" />
          <el-option label="PUT" value="PUT" />
          <el-option label="DELETE" value="DELETE" />
        </el-select>
      </div>
      <div class="grid-item">
        <span>用户名称:</span>
        <el-input v-model="searchForm.username" placeholder="请输入用户名称" />
      </div>
      <div class="grid-item">
        <span>操作状态:</span>
        <el-select v-model="searchForm.status" placeholder="请选择">
          <el-option label="全部" value="" />
          <el-option label="异常" value="0" />
          <el-option label="正常" value="1" />
        </el-select>
      </div>
      <div class="grid-item">
        <span>错误消息:</span>
        <el-input v-model="searchForm.errorMsg" placeholder="请输入错误消息" />
      </div>
      <div class="grid-item">
        <el-button v-permission="['system::logs::operlog::query']" type="primary" icon="el-icon-search" size="small" :loading="loading" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <!-- 操作按钮栏 -->
    <div class="button-bar">
      <!-- <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" plain :loading="loading" @click="handleAdd"> 新增项目 </el-button>
      <el-button type="danger" icon="el-icon-delete" size="small" plain :loading="loading" @click="handleRemove"> 批量删除 </el-button> -->
      <el-button v-permission="['system::logs::operlog::query']" type="info" icon="el-icon-refresh-right" size="small" plain :loading="loading" @click="handleRefresh"> 刷新 </el-button>
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
        <el-table-column prop="title" label="模块标题" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="businessType" label="业务类型" :show-overflow-tooltip="showOverflowTooltip">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.businessType === 0" type="warning">其他</el-tag>
            <el-tag v-if="scope.row.businessType === 1" type="">新增</el-tag>
            <el-tag v-if="scope.row.businessType === 2" type="success">修改</el-tag>
            <el-tag v-if="scope.row.businessType === 3" type="danger">删除</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="method" label="方法名称" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="requestMethod" label="请求方式" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="username" label="用户名称" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="operUrl" label="请求URL" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="operIp" label="主机地址" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="operLocation" label="操作地点" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="operParam" label="请求参数" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="jsonResult" label="返回参数" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="status" label="操作状态" :show-overflow-tooltip="showOverflowTooltip">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 0" type="danger">异常</el-tag>
            <el-tag v-if="scope.row.status === 1">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="errorMsg" label="错误消息" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="operTime" label="操作时间" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="costTime" label="消耗时间" :show-overflow-tooltip="showOverflowTooltip" />
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
            <el-form-item label="模块标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入模块标题" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="方法名称" prop="method">
              <el-input v-model="form.method" placeholder="请输入方法名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="请求方式" prop="requestMethod">
              <el-input v-model="form.requestMethod" placeholder="请输入请求方式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名称" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="请求URL" prop="operUrl">
              <el-input v-model="form.operUrl" placeholder="请输入请求URL" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="主机地址" prop="operIp">
              <el-input v-model="form.operIp" placeholder="请输入主机地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作地点" prop="operLocation">
              <el-input v-model="form.operLocation" placeholder="请输入操作地点" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="请求参数" prop="operParam">
              <el-input v-model="form.operParam" placeholder="请输入请求参数" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="返回参数" prop="jsonResult">
              <el-input v-model="form.jsonResult" placeholder="请输入返回参数" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="错误消息" prop="errorMsg">
              <el-input v-model="form.errorMsg" placeholder="请输入错误消息" />
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
import { pageByParams, save, updateById, deleteByIds } from '@/api/system/logs/sysOperLog'

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
        title: null, // 模块标题
        businessType: null, // 业务类型 0-其他 1-新增 2-修改 3-删除
        method: null, // 方法名称
        requestMethod: null, // 请求方式
        username: null, // 用户名称
        operUrl: null, // 请求URL
        operIp: null, // 主机地址
        operLocation: null, // 操作地点
        operParam: null, // 请求参数
        jsonResult: null, // 返回参数
        status: null, // 操作状态（1正常 0异常）
        errorMsg: null // 错误消息
      },
      businessTypeSelectOptions: [
        { value: '', label: '全部' },
        { value: 0, label: '其他' },
        { value: 1, label: '新增' },
        { value: 2, label: '修改' },
        { value: 3, label: '删除' }
      ],
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
        title: null, // 模块标题
        method: null, // 方法名称
        requestMethod: null, // 请求方式
        username: null, // 用户名称
        operUrl: null, // 请求URL
        operIp: null, // 主机地址
        operLocation: null, // 操作地点
        operParam: null, // 请求参数
        jsonResult: null, // 返回参数
        errorMsg: null // 错误消息
      },
      // 重置表单
      resetForm: {
        title: null, // 模块标题
        method: null, // 方法名称
        requestMethod: null, // 请求方式
        username: null, // 用户名称
        operUrl: null, // 请求URL
        operIp: null, // 主机地址
        operLocation: null, // 操作地点
        operParam: null, // 请求参数
        jsonResult: null, // 返回参数
        errorMsg: null // 错误消息
      },
      // 表单校验
      rules: {
        title: [{ required: true, message: '模块标题不能为空', trigger: 'blur' }],
        method: [{ required: true, message: '方法名称不能为空', trigger: 'blur' }],
        requestMethod: [{ required: true, message: '请求方式不能为空', trigger: 'blur' }],
        username: [{ required: true, message: '用户名称不能为空', trigger: 'blur' }],
        operUrl: [{ required: true, message: '请求URL不能为空', trigger: 'blur' }],
        operIp: [{ required: true, message: '主机地址不能为空', trigger: 'blur' }],
        operLocation: [{ required: true, message: '操作地点不能为空', trigger: 'blur' }],
        operParam: [{ required: true, message: '请求参数不能为空', trigger: 'blur' }],
        jsonResult: [{ required: true, message: '返回参数不能为空', trigger: 'blur' }],
        errorMsg: [{ required: true, message: '错误消息不能为空', trigger: 'blur' }]
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
      this.dialogTitle = '新增操作日志'
      this.handleResetForm()
    },
    handleEdit(row) {
      // 编辑 按钮被点击
      this.btnType = 'update'
      this.dialogVisible = true
      this.dialogTitle = '修改操作日志'
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
