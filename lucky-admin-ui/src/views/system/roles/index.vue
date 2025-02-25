<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-permission="['system::roles::query']" class="search-bar">
      <div class="grid-item">
        <span>角色名称:</span>
        <el-input v-model="searchForm.name" placeholder="请输入角色名称" />
      </div>
      <div class="grid-item">
        <span>角色描述:</span>
        <el-input v-model="searchForm.description" placeholder="请输入角色描述" />
      </div>
      <div class="grid-item">
        <el-button v-permission="['system::roles::query']" type="primary" icon="el-icon-search" size="small" :loading="loading" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <!-- 操作按钮栏 -->
    <div v-permission="['system::roles::query','system::roles::insert','system::roles::delete']" class="button-bar">
      <el-button type="primary" icon="el-icon-circle-plus-outline" size="small" plain :loading="loading" @click="handleAdd"> 新增项目 </el-button>
      <el-button type="danger" icon="el-icon-delete" size="small" plain :loading="loading" @click="handleRemove"> 批量删除 </el-button>
      <el-button type="info" icon="el-icon-refresh-right" size="small" plain :loading="loading" @click="handleRefresh"> 刷新 </el-button>
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
        <el-table-column prop="name" label="角色名称" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="description" label="角色描述" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="sort" label="显示顺序" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column
          v-if="checkPermission(['system::roles::auth','system::roles::update','system::roles::delete'])"
          label="操作"
          width="200"
          fixed="right"
          align="center"
        >
          <template slot-scope="scope">
            <el-button v-permission="['system::roles::auth']" :size="toolBar.size" :type="toolBar.insertType" :icon="toolBar.insertIcon" @click="handleAddUser(scope.row)">分配用户</el-button>
            <el-button v-permission="['system::roles::update']" :size="toolBar.size" :type="toolBar.updateType" :icon="toolBar.updateIcon" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-permission="['system::roles::delete']" :size="toolBar.size" :type="toolBar.deleteType" :icon="toolBar.deleteIcon" @click="handleDelete(scope.row)">删除</el-button>
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
          <el-col :span="24">
            <el-form-item label="角色名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入角色名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="角色描述" prop="description">
              <el-input v-model="form.description" placeholder="请输入角色描述" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示顺序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="分配权限" prop="menus">
            <el-input v-model="filterText" placeholder="请输入权限名称" />
            <el-button size="mini" type="primary" @click="handleCheckAll">全选/全不选</el-button>
            <el-button size="mini" type="danger" @click="handleExpand(form.menus)">展开/折叠</el-button>
            <el-tree
              v-if="showMenusTree"
              ref="tree"
              class="filter-tree"
              style="height: 200px; overflow: auto;"
              :data="formartMenusTree"
              node-key="id"
              show-checkbox
              :check-strictly="true"
              :props="defaultProps"
              :default-expand-all="isExpand"
              :default-expanded-keys="[1]"
              :filter-node-method="filterMenusTree"
              @current-change="treeChange"
            />
            <!-- @check="handleCheck" -->
          </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 分配用户对话框 -->
    <el-dialog
      :title="dialogTitleByUser"
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
          :titles="['未授权用户', '已授权用户']"
          :props="{key: 'id',label: 'username'}"
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
import { list } from '@/api/system/sysMenus'
import { getById, pageByParams, save, updateById, deleteByIds } from '@/api/system/sysRoles'
import { getAuthRole, saveAuthRole } from '@/api/user'

export default {
  data() {
    return {
      // 页面loading
      loading: false,
      // 表格的单元格字数过长提示的开关
      showOverflowTooltip: true,
      // 弹窗标题
      dialogTitle: '',
      // 分配用户弹窗标题
      dialogTitleByUser: '',
      // 是否显示弹窗
      dialogVisible: false,
      // 分配用户弹窗是否显示
      dialogVisibleByUser: false,
      // 按钮点击类型
      btnType: '', // insert / update
      // 表格复选框选中的Ids
      selectedIds: [],
      // 搜索条件
      searchForm: {
        name: null, // 角色名称
        description: null // 角色描述
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
        name: null, // 角色名称
        description: null, // 角色描述
        sort: 0, // 显示顺序
        menusIds: [] // 菜单
      },
      // 重置表单
      resetForm: {
        name: null, // 角色名称
        description: null, // 角色描述
        sort: 0, // 显示顺序
        menusIds: [] // 菜单
      },
      // 表单校验
      rules: {
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }],
        description: [{ required: true, message: '角色描述不能为空', trigger: 'blur' }],
        sort: [{ required: true, message: '显示顺序不能为空', trigger: 'blur' }]
      },
      // 菜单权限树
      menusTree: [],
      formartMenusTree: [],
      // 菜单树展开折叠
      isExpand: false,
      showMenusTree: true,
      // 菜单树搜索
      filterText: '',
      // 权限树渲染格式
      defaultProps: {
        id: 'id',
        children: 'children',
        label: 'title'
      },
      // 分配用户表单数据
      transferData: [],
      transferValue: [],
      // 分配用户表单
      formByUser: {
        roleId: null
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.handleInit()
  },
  methods: {
    treeChange() {
    },
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
      this.dialogTitle = '新增角色信息'
      this.handleResetForm()
      this.getMenusTree()
    },
    handleEdit(row) {
      // 编辑 按钮被点击
      this.btnType = 'update'
      this.dialogVisible = true
      this.dialogTitle = '修改角色信息'
      this.form = { ...row }
      this.getMenusTree()
      getById(row.id).then(res => {
        const { data } = res
        this.selectTreeNode(data.menusIds)
      }).catch(() => {
        this.loading = false
      })
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
          this.form.menusIds = this.$refs.tree.getCheckedKeys()
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
    handleAddUser(row) {
      // 分配用户
      this.dialogVisibleByUser = true
      this.dialogByUserTitle = row.name + '--' + row.description + '--分配用户'
      this.formByUser.roleId = row.id
      getAuthRole(row.id).then(res => {
        const { data } = res
        this.transferData = data.usersAll
        this.transferValue = data.hasUsersIds
      }).catch(() => {
        this.loading = false
      })
    },
    submitFormByUser() {
      // 分配用户表单提交
      this.loading = true
      saveAuthRole(this.formByUser.roleId, this.transferValue).then(res => {
        this.$message.success('分配成功')
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleCheckAll() {
      const arr = this.$refs.tree.getCheckedKeys()
      // 全选
      if (arr && arr.length > 0) {
        this.selectTreeNode([])
      } else {
        this.selectTreeNode(this.menusTree.map(item => item.id))
      }
    },

    handleExpand() {
      const arr = this.$refs.tree.getCheckedKeys()
      // 展开/折叠
      this.isExpand = !this.isExpand
      this.showMenusTree = false
      this.$nextTick(() => {
        this.showMenusTree = true
        this.$nextTick(() => {
          this.selectTreeNode(arr)
        })
      })
    },
    getMenusTree() {
      list({}).then(res => {
        const { data } = res
        this.menusTree = data
        this.formartMenusTree = this.handleTree(data)
      })
    },
    selectTreeNode(ids) {
      this.$nextTick(() => {
        this.$refs.tree.setCheckedKeys(ids)
      })
    },
    // 菜单过滤数据
    filterMenusTree(value, data) {
      if (!value) return true
      return data.title.indexOf(value) !== -1
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
      this.selectTreeNode([])
    },
    transferFilter(query, item) {
    // 分配角色搜索框输入过滤
      return item.username.indexOf(query) > -1
    }
  }
}
</script>

<style>

</style>
