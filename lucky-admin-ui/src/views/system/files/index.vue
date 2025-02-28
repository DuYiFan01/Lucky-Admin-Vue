<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-permission="['system::files::query']" class="search-bar">
      <div class="grid-item">
        <span>原始文件名:</span>
        <el-input v-model="searchForm.originalName" placeholder="请输入原始文件名" />
      </div>
      <div class="grid-item">
        <span>当前文件名:</span>
        <el-input v-model="searchForm.fileName" placeholder="请输入当前文件名" />
      </div>
      <div class="grid-item">
        <span>文件类型:</span>
        <el-input v-model="searchForm.fileType" placeholder="请输入文件类型" />
      </div>
      <div class="grid-item">
        <span>业务分类:</span>
        <el-select v-model="searchForm.fileBusinessType" placeholder="请选择业务分类" :clearable="true" :filterable="true">
          <el-option
            v-for="(key,value) in fileBusinessTypes"
            :key="value"
            :label="key"
            :value="value"
          />
        </el-select>
      </div>
      <div class="grid-item">
        <el-button v-permission="['system::files::query']" type="primary" icon="el-icon-search" size="small" :loading="loading" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <!-- 操作按钮栏 -->
    <div v-permission="['system::files::query','system::files::insert','system::files::delete']" class="button-bar">
      <el-button v-permission="['system::files::insert']" type="primary" icon="el-icon-circle-plus-outline" size="small" plain :loading="loading" @click="handleAdd"> 新增项目 </el-button>
      <el-button v-permission="['system::files::delete']" type="danger" icon="el-icon-delete" size="small" plain :loading="loading" @click="handleRemove"> 批量删除 </el-button>
      <el-button v-permission="['system::files::query']" type="info" icon="el-icon-refresh-right" size="small" plain :loading="loading" @click="handleRefresh"> 刷新 </el-button>
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
        <el-table-column prop="originalName" label="原始文件名" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="fileName" label="当前文件名" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="fileType" label="文件类型" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="fileBusinessType" label="业务分类" :show-overflow-tooltip="showOverflowTooltip" >
          <template slot-scope="scope">
            {{ fileBusinessTypes[scope.row.fileBusinessType] }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="checkPermission(['system::files::query','system::files::delete'])"
          label="操作"
          width="200"
          align="center"
        >
          <template slot-scope="scope">
            <el-button v-permission="['system::files::query']" :size="toolBar.size" :type="toolBar.updateType" icon="el-icon-view" @click="handleEdit(scope.row)">预览下载</el-button>
            <el-button v-permission="['system::files::delete']" :size="toolBar.size" :type="toolBar.deleteType" :icon="toolBar.deleteIcon" @click="handleDelete(scope.row)">删除</el-button>
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
            <el-form-item label="原始文件名" prop="originalName">
              <el-input v-model="form.originalName" placeholder="请输入原始文件名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="当前文件名" prop="fileName">
              <el-input v-model="form.fileName" placeholder="请输入当前文件名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="存储路径" prop="storagePath">
              <el-input v-model="form.storagePath" placeholder="请输入存储路径" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="哈希值" prop="fileHash">
              <el-input v-model="form.fileHash" placeholder="请输入哈希值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="文件类型" prop="fileType">
              <el-input v-model="form.fileType" placeholder="请输入文件类型" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="业务分类" prop="fileBusinessType">
              <el-input v-model="form.fileBusinessType" placeholder="请输入业务分类" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注" />
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
import { getFileBusinessType, pageByParams, save, updateById, deleteByIds } from '@/api/system/sysFiles'
import {isHttp} from '@/utils/validate'
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
        originalName: null, // 原始文件名
        fileName: null, // 当前文件名
        storagePath: null, // 存储路径
        fileHash: null, // 哈希值
        fileType: null, // 文件类型
        fileBusinessType: null, // 业务分类
        remark: null // 备注
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
        originalName: null, // 原始文件名
        fileName: null, // 当前文件名
        storagePath: null, // 存储路径
        fileHash: null, // 哈希值
        fileType: null, // 文件类型
        fileBusinessType: null, // 业务分类
        remark: null // 备注
      },
      // 重置表单
      resetForm: {
        originalName: null, // 原始文件名
        fileName: null, // 当前文件名
        storagePath: null, // 存储路径
        fileHash: null, // 哈希值
        fileType: null, // 文件类型
        fileBusinessType: null, // 业务分类
        remark: null // 备注
      },
      // 表单校验
      rules: {
        originalName: [{ required: true, message: '原始文件名不能为空', trigger: 'blur' }],
        fileName: [{ required: true, message: '当前文件名不能为空', trigger: 'blur' }],
        storagePath: [{ required: true, message: '存储路径不能为空', trigger: 'blur' }],
        fileHash: [{ required: true, message: '哈希值不能为空', trigger: 'blur' }],
        fileType: [{ required: true, message: '文件类型不能为空', trigger: 'blur' }],
        fileBusinessType: [{ required: true, message: '业务分类不能为空', trigger: 'blur' }],
        remark: [{ required: true, message: '备注不能为空', trigger: 'blur' }]
      },
      // 所有的文件业务类型
      fileBusinessTypes:[],
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
      getFileBusinessType().then(res => {
        const { data } = res
        this.fileBusinessTypes = data
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
      this.dialogTitle = '新增文件管理'
      this.handleResetForm()
    },
    handleEdit(row) {
      // 编辑 按钮被点击
      // this.btnType = 'update'
      // this.dialogVisible = true
      // this.dialogTitle = '修改文件管理'
      // this.form = { ...row }
      if(isHttp(row.storagePath)){
        window.open(row.storagePath)
      }else{
        window.open(process.env.VUE_APP_BASE_API +row.storagePath)
      }
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
