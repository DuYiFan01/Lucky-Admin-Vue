<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div v-permission="['system::menus::query']" class="search-bar">
      <div class="grid-item">
        <span>菜单名称:</span>
        <el-input v-model="searchForm.title" placeholder="请输入菜单名称" />
      </div>
      <div class="grid-item">
        <span>菜单状态:</span>
        <el-select v-model="searchForm.visible" placeholder="请选择">
          <el-option label="全部状态" value="" />
          <el-option label="显示" value="1" />
          <el-option label="隐藏" value="0" />
        </el-select>
      </div>
      <div v-permission="['system::menus::query']" class="grid-item">
        <el-button type="primary" icon="el-icon-search" size="small" :loading="loading" @click="handleSearch">
          搜索
        </el-button>
      </div>
    </div>
    <!-- 操作按钮栏 -->
    <div v-permission="['system::menus::query','system::menus::insert']" class="button-bar">
      <el-button v-permission="['system::menus::insert']" type="primary" icon="el-icon-circle-plus-outline" size="small" plain :loading="loading" @click="handleAdd"> 新增菜单 </el-button>
      <el-button type="warning" icon="el-icon-s-fold" size="small" plain :loading="loading" @click="handleExpand"> 展开/收起 </el-button>
      <el-button v-permission="['system::menus::query']" type="info" icon="el-icon-refresh-right" size="small" plain :loading="loading" @click="handleRefresh"> 刷新 </el-button>
    </div>
    <!-- 表格 -->
    <div class="table-bar">
      <el-table
        v-if="tableShow"
        v-loading="loading"
        :data="tableData"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="isExpand"
        max-height="560px"
      >
        <el-table-column width="200" prop="title" label="菜单名称" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="icon" label="菜单图标" :show-overflow-tooltip="showOverflowTooltip">
          <template slot-scope="scope">
            <svg-icon :icon-class="scope.row.icon" />
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="显示顺序" />
        <el-table-column prop="roles" label="权限标识" :show-overflow-tooltip="showOverflowTooltip" />
        <!-- <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="showOverflowTooltip" /> -->
        <el-table-column prop="path" label="路由地址" :show-overflow-tooltip="showOverflowTooltip" />
        <el-table-column prop="visible" label="菜单状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.visible === 1">显示</el-tag>
            <el-tag v-if="scope.row.visible === 0" type="danger">隐藏</el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="createTime" label="创建时间" :show-overflow-tooltip="showOverflowTooltip" /> -->
        <el-table-column
          v-if="checkPermission(['system::menus::insert','system::menus::update','system::menus::delete'])"
          label="操作"
          width="200"
          fixed="right"
          align="center"
        >
          <template slot-scope="scope">
            <el-button v-permission="['system::menus::insert']" :size="toolBar.size" :type="toolBar.insertType" :icon="toolBar.insertIcon" @click="handleAdd(scope.row)">新增</el-button>
            <el-button v-permission="['system::menus::update']" :size="toolBar.size" :type="toolBar.updateType" :icon="toolBar.updateIcon" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-permission="['system::menus::delete']" :size="toolBar.size" :type="toolBar.deleteType" :icon="toolBar.deleteIcon" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
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
        <!-- 上级菜单 -->
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单" prop="parentId">
              <treeselect
                v-model="form.parentId"
                :options="menuOptions"
                :normalizer="normalizer"
                :show-count="true"
                placeholder="选择上级菜单"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 菜单类型 -->
        <el-row>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio aria-hidden="false" label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
                <el-radio label="N">内链</el-radio>
                <el-radio label="W">外链</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 图标选择 -->
        <el-row>
          <el-form-item v-if="form.menuType !== 'F'" label="菜单图标" prop="icon">
            <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
              <IconSelect
                ref="iconSelect"
                :active-icon="form.icon"
                @selected="selectedIcon"
              />
              <el-input slot="reference" v-model="form.icon" placeholder="点击选择图标" readonly>
                <svg-icon v-if="form.icon" slot="prefix" :icon-class="form.icon" style="width: 25px;" />
                <i v-else slot="prefix" class="el-icon-search el-input__icon" />
              </el-input>
            </el-popover>
          </el-form-item>
        </el-row>
        <!-- 菜单名称 路由名称 -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="title">
              <el-input v-model="form.title" placeholder="请输入菜单中文名称" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType === 'F'" :span="12">
            <el-form-item label="权限字符" prop="roles">
              <span slot="label">
                <el-tooltip content="权限字符,如:system:user:list" placement="top">
                  <i class="el-icon-question" />
                </el-tooltip>
                权限字符
              </span>
              <el-input v-model="form.roles" placeholder="请输入菜单权限字符" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType !== 'F'" :span="12">
            <el-form-item label="路由名称" prop="name">
              <span slot="label">
                <el-tooltip content="路由名称,如:系统管理-system (注意:英文小写)" placement="top">
                  <i class="el-icon-question" />
                </el-tooltip>
                路由名称
              </span>
              <el-input v-model="form.name" placeholder="请输入路由名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 显示顺序 路由地址 -->
        <el-row>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType !== 'F'" :span="12">
            <el-form-item label="路由地址" prop="path">
              <span slot="label">
                <el-tooltip content="路由地址,如:系统管理->菜单管理-/system/menus (注意:英文小写)" placement="top">
                  <i class="el-icon-question" />
                </el-tooltip>
                路由地址
              </span>
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>

        </el-row>
        <!-- 组件路径 路由参数 -->
        <el-row>
          <el-col v-if="form.menuType === 'C'" :span="12">
            <el-form-item label="组件路径" prop="component">
              <span slot="label">
                <el-tooltip content="组件路径,如:如：`system/user/index`，默认在`views`目录下" placement="top">
                  <i class="el-icon-question" />
                </el-tooltip>
                组件路径
              </span>
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col v-if="form.menuType === 'C'" :span="12">
            <el-form-item label="路由参数" prop="query">
              <span slot="label">
                <el-tooltip content="访问路由的默认传递参数，如：`{&quot;id&quot;: 1, &quot;name&quot;: &quot;ry&quot;}`" placement="top">
                  <i class="el-icon-question" />
                </el-tooltip>
                路由参数
              </span>
              <el-input v-model="form.query" placeholder="请输入路由参数" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 是否缓存 是否显示 -->
        <el-row>
          <el-col v-if="form.menuType === 'C'" :span="12">
            <el-form-item label="是否缓存" prop="isCache">
              <el-radio-group v-model="form.isCache">
                <el-radio v-for="v in isCacheTyps" :key="v.value" :label="v.value">{{ v.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示状态" prop="visible">
              <span slot="label">
                <el-tooltip content="选择隐藏则路由将不会出现在侧边栏，但仍然可以访问" placement="top">
                  <i class="el-icon-question" />
                </el-tooltip>
                显示状态
              </span>
              <el-radio-group v-model="form.visible">
                <el-radio v-for="v in visibleTyps" :key="v.value" :label="v.value">{{ v.label }}</el-radio>
              </el-radio-group>
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
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import IconSelect from '@/components/IconSelect/index'

import { list, save, updateById, deleteByIds } from '@/api/system/sysMenus'

export default {
  components: {
    Treeselect,
    IconSelect
  },
  data() {
    return {
      // 页面loading
      loading: false,
      // 表格的单元格字数过长提示的开关
      showOverflowTooltip: true,
      // 全部展开/收起
      isExpand: false,
      tableShow: true,
      // 弹窗标题
      dialogTitle: '',
      // 是否显示弹窗
      dialogVisible: false,
      // 按钮点击类型
      btnType: '', // insert / update
      // 搜索条件
      searchForm: {
        title: '',
        visible: ''
      },
      // 表格数据
      tableData: [],
      // 新增 / 修改表单
      form: {
        id: null, // 菜单ID
        title: '', // 菜单名称
        name: '', // 路由名称
        parentId: '', // 父菜单ID
        sort: '', // 显示顺序
        path: '', // 路由地址
        component: '', // 组件路径
        query: '', // 请求参数
        isCache: 0, // 是否缓存 1-缓存 0-不缓存
        menuType: 'M', // M-目录 C-菜单 F-按钮 N-内链 W-外链
        visible: 1, // 菜单状态（1-显示 0-隐藏）
        roles: '', // 权限标识
        icon: '', // 菜单图标
        remark: '' // 备注
      },
      // form 校验规则
      rules: {
        title: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入路由名称', trigger: 'blur' }
        ],
        parentId: [
          { required: true, message: '请选择上级菜单', trigger: 'blur' }
        ],
        menuType: [
          { required: true, message: '请选择菜单类型', trigger: 'blur' }
        ],
        component: [
          { required: true, message: '请输入组件路径', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入路由地址', trigger: 'blur' }
        ]
      },
      // 表单重置
      resetForm: {
        id: null, // 菜单ID
        title: '', // 菜单名称
        name: '', // 路由名称
        parentId: '', // 父菜单ID
        sort: '', // 显示顺序
        path: '', // 路由地址
        component: '', // 组件路径
        query: '', // 请求参数
        isCache: 0, // 是否缓存
        menuType: 'M', // M-目录 C-菜单 F-按钮 N-内链 W-外链
        visible: 1, // 菜单状态（1-显示 0-隐藏）
        roles: '', // 权限标识
        icon: '', // 菜单图标
        remark: '' // 备注
      },
      // 弹出框上级菜单选项树内容
      menuOptions: [],
      isCacheTyps: [
        { label: '缓存', value: 1 },
        { label: '不缓存', value: 0 }
      ],
      visibleTyps: [
        { label: '显示', value: 1 },
        { label: '隐藏', value: 0 }
      ]
    }
  },
  created() {
    // 页面初始化函数
    this.handleInit()
  },
  methods: {
    // 页面创建完毕初始化函数
    handleInit() {
      this.loading = true
      // 刷新逻辑
      list({}).then(res => {
        const { data } = res
        this.tableData = this.handleTree(data)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 搜索按钮 被点击
    handleSearch() {
      this.loading = true
      list(this.searchForm).then(res => {
        const { data } = res
        this.tableData = this.handleTree(data)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 新增按钮 被点击
    handleAdd(row) {
      this.dialogTitle = '新增菜单'
      this.dialogVisible = true
      this.btnType = 'insert'
      this.InitPopUpMenusTree()
      this.handleReset()
      this.$nextTick(() => {
        if (row.id) {
          this.form.parentId = row.id
        } else {
          this.form.parentId = 0
        }
      })
    },
    // 编辑按钮 被点击
    handleEdit(row) {
      this.dialogTitle = '编辑菜单'
      this.dialogVisible = true
      this.btnType = 'update'
      this.InitPopUpMenusTree()
      this.form = { ...row }
      console.log('菜单', row)
    },
    // 删除按钮 被点击
    handleDelete(row) {
      this.$confirm('此操作将永久删除该菜单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteByIds(row.id).then(res => {
          this.$message.success('删除成功')
          this.handleSearch()
        }).catch(() => {
          this.loading = false
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    // 重置表单
    handleReset() {
      this.form = { ...this.resetForm }
    },
    // 表单提交
    submitForm() {
      // 验证输入
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
            // 修改菜单
            this.loading = true
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
    // 刷新 按钮被点击
    handleRefresh() {
      this.handleSearch()
    },
    // 展开/收起 按钮被点击
    handleExpand() {
      this.isExpand = !this.isExpand
      this.tableShow = false
      this.$nextTick(() => {
        this.tableShow = true
      })
    },
    // 弹出框 上级菜单初始化数据
    InitPopUpMenusTree() {
      this.loading = true
      list({}).then(res => {
        const { data } = res
        this.menuOptions = []
        const menu = { id: 0, title: '主类目', children: [] }
        menu.children = this.handleTree(data)
        this.menuOptions.push(menu)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 弹出框 上级菜单 数据结构格式化
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.childrens
      }
      return {
        id: node.id,
        label: node.title,
        children: node.children
      }
    },
    // 弹出框 Icon选择器 被点击
    selectedIcon(icon) {
      this.form.icon = icon
    }
  }
}
</script>

<style scoped>

</style>
