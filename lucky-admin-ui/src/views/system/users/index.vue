<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-permission="['system::users::query']" class="search-bar">
      <div class="grid-item">
        <span>账号:</span>
        <el-input v-model="searchForm.username" placeholder="请输入账号" />
      </div>
      <div class="grid-item">
        <span>昵称:</span>
        <el-input v-model="searchForm.name" placeholder="请输入昵称" />
      </div>
      <div class="grid-item">
        <span>手机号:</span>
        <el-input v-model="searchForm.phone" placeholder="请输入手机号" />
      </div>
      <div class="grid-item">
        <span>邮箱:</span>
        <el-input v-model="searchForm.email" placeholder="请输入邮箱" />
      </div>
      <div class="grid-item">
        <span>性别:</span>
        <el-select v-model="searchForm.sex" placeholder="请选择">
          <el-option label="全部" value="" />
          <el-option label="女" value="0" />
          <el-option label="男" value="1" />
          <el-option label="未知" value="2" />
        </el-select>
      </div>

      <div class="grid-item">
        <span>是否启用:</span>
        <el-select v-model="searchForm.enabled" placeholder="请选择">
          <el-option label="全部" value="" />
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </div>
      <div class="grid-item">
        <el-button v-permission="['system::users::query']" type="primary" icon="el-icon-search" size="small" :loading="loading" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <!-- 操作按钮栏 -->
    <div v-permission="['system::users::query','system::users::insert','system::users::delete']" class="button-bar">
      <el-button v-permission="['system::users::insert']" type="primary" icon="el-icon-circle-plus-outline" size="small" plain :loading="loading" @click="handleAdd"> 新增项目 </el-button>
      <el-button v-permission="['system::users::delete']" type="danger" icon="el-icon-delete" size="small" plain :loading="loading" @click="handleRemove"> 批量删除 </el-button>
      <el-button v-permission="['system::users::query']" type="info" icon="el-icon-refresh-right" size="small" plain :loading="loading" @click="handleRefresh"> 刷新 </el-button>
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
        <el-table-column prop="username" label="账号" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="name" label="昵称" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="phone" label="手机号" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="sex" label="性别" :show-overflow-tooltip="showOverflowTooltip">
          <template slot-scope="scope">
            <span v-if="scope.row.sex === '0'">女</span>
            <span v-if="scope.row.sex === '1'" type="danger">男</span>
            <span v-if="scope.row.sex === '2'" type="danger">未知</span>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="enabled" label="是否启用" :show-overflow-tooltip="showOverflowTooltip">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled === '0'" type="danger">禁用</el-tag>
            <el-tag v-if="scope.row.enabled === '1'">启用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="loginIp" label="最后登录IP" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="loginTime" label="最后登录时间" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column
          v-if="checkPermission(['system::users::auth','system::users::update','system::users::delete'])"
          label="操作"
          align="center"
          width="200"
        >
          <template slot-scope="scope">
            <el-button  v-permission="['system::users::auth']" :size="toolBar.size" :type="toolBar.insertType" :icon="toolBar.insertIcon" @click="handleAddRoles(scope.row)">分配角色</el-button>
            <el-button  v-permission="['system::users::update']" :size="toolBar.size" :type="toolBar.updateType" :icon="toolBar.updateIcon" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-permission="['system::users::delete']" :size="toolBar.size" :type="toolBar.deleteType" :icon="toolBar.deleteIcon" @click="handleDelete(scope.row)">删除</el-button>
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
            <el-form-item label="账号" prop="username">
              <el-input v-model="form.username" placeholder="请输入账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="昵称" prop="name">
              <el-input v-model="form.name" placeholder="请输入昵称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择">
                <el-option label="女" value="0" />
                <el-option label="男" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用" prop="sex">
              <el-select v-model="form.enabled" placeholder="请选择">
                <el-option label="禁用" value="0" />
                <el-option label="启用" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 分配角色对话框 -->
    <el-dialog
      :title="dialogByUserTitle"
      :visible.sync="dialogVisibleByUser"
      width="800px"
      :close-on-click-modal="false"
      center
    >
      <el-form ref="form" :model="formByUser" style="text-align: center;">
        <el-transfer
          v-model="transferValue"
          style="text-align: left; display: inline-block"
          filterable
          :filter-method="transferFilter"
          filter-placeholder="请输入用户名"
          :data="transferData"
          :button-texts="['取消', '添加']"
          :titles="['未拥有角色', '已拥有角色']"
          :props="{key: 'id',label: 'name'}"
        />
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormByUser">确 定</el-button>
        <el-button @click="dialogVisibleByUser = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { pageByParams, save, updateById, deleteByIds } from '@/api/system/sysUsers'
import { getAuthUser, saveAuthUser } from '@/api/user'

export default {
  data() {
    return {
      // 页面loading
      loading: false,
      // 表格的单元格字数过长提示的开关
      showOverflowTooltip: true,
      // 弹窗标题
      dialogTitle: '',
      // 分配角色弹窗标题
      dialogByUserTitle: '',
      // 是否显示弹窗
      dialogVisible: false,
      // 分配角色弹窗
      dialogVisibleByUser: false,
      // 按钮点击类型
      btnType: '', // insert / update
      // 表格复选框选中的Ids
      selectedIds: [],
      // 搜索条件
      searchForm: {
        username: '', // 账号
        name: '', // 昵称
        phone: '', // 手机号
        sex: '', // 性别
        email: '', // 邮箱
        enabled: '' // 是否启用
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
        id: null, // 用户ID
        username: '', // 账号
        name: '', // 昵称
        phone: '', // 手机号
        sex: '', // 性别
        email: '', // 邮箱
        enabled: '1' // 是否启用
      },
      // 重置表单
      resetForm: {
        id: null, // 用户ID
        username: '', // 账号
        name: '', // 昵称
        phone: '', // 手机号
        sex: '', // 性别
        email: '', // 邮箱
        enabled: '1' // 是否启用
      },
      // 表单校验
      rules: {
        username: [
          { required: true, message: '账号不能为空', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '昵称不能为空', trigger: 'blur' }
        ],
        sex: [
          { required: true, message: '性别不能为空', trigger: 'blur' }
        ]
      },
      // 用户可分配角色数据
      transferData: [],
      // 已分配角色ID
      transferValue: [],
      // 分配用户表单
      formByUser: {
        userId: null,
        roleIds: []
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
      this.dialogTitle = '新增用户'
      this.handleResetForm()
    },
    handleEdit(row) {
      // 编辑 按钮被点击
      this.btnType = 'update'
      this.dialogVisible = true
      this.dialogTitle = '修改用户'
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
            save({ ...this.form }).then(res => {
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
    handleAddRoles(row) {
      // 给用户分配角色
      this.dialogVisibleByUser = true
      this.dialogByUserTitle = row.username + '--' + row.name + '--分配角色'
      this.formByUser.userId = row.id
      getAuthUser(row.id).then(res => {
        const { data } = res
        this.transferData = data.rolesAll
        this.transferValue = data.hasRoles.map(item => item.id)
      }).catch(() => {
        this.loading = false
      })
    },
    submitFormByUser() {
      this.loading = true
      saveAuthUser(this.formByUser.userId, this.transferValue).then(res => {
        this.$message.success('分配成功')
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    transferFilter(query, item) {
    // 分配角色搜索框输入过滤
      return item.name.indexOf(query) > -1
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
