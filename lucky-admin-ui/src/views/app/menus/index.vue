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
    <div v-permission="['system::menus::query', 'system::menus::insert']" class="button-bar">
      <el-button
        v-permission="['system::menus::insert']"
        type="primary"
        icon="el-icon-circle-plus-outline"
        size="small"
        plain
        :loading="loading"
        @click="handleAdd"
      > 新增菜单 </el-button>
      <el-button type="warning" icon="el-icon-s-fold" size="small" plain :loading="loading" @click="handleExpand"> 展开/收起
      </el-button>
      <el-button
        v-permission="['system::menus::query']"
        type="info"
        icon="el-icon-refresh-right"
        size="small"
        plain
        :loading="loading"
        @click="handleRefresh"
      > 刷新 </el-button>
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
            <i :class="['iconfont', scope.row.icon]" />
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
          v-if="checkPermission(['system::menus::insert', 'system::menus::update', 'system::menus::delete'])"
          label="操作"
          width="200"
          align="center"
        >
          <template slot-scope="scope">
            <el-button
              v-permission="['system::menus::insert']"
              :size="toolBar.size"
              :type="toolBar.insertType"
              :icon="toolBar.insertIcon"
              @click="handleAdd(scope.row)"
            >新增</el-button>
            <el-button
              v-permission="['system::menus::update']"
              :size="toolBar.size"
              :type="toolBar.updateType"
              :icon="toolBar.updateIcon"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-permission="['system::menus::delete']"
              :size="toolBar.size"
              :type="toolBar.deleteType"
              :icon="toolBar.deleteIcon"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="680px" :close-on-click-modal="false" center>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" label-position="center">
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
                @input="handleParentChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 菜单类型 -->
        <el-row>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <!-- 只有在主目录下才能添加目录 -->
                <el-radio aria-hidden="false" label="M" :disabled="form.parentId !== -1">目录</el-radio>
                <!-- 只有在目录下才能添加菜单 -->
                <el-radio label="C" :disabled="!isParentDirectory">菜单</el-radio>
                <!-- 只有在菜单下才能添加按钮 -->
                <el-radio label="F" :disabled="!isParentMenu">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 图标选择 -->
        <el-row>
          <el-form-item v-if="form.menuType === 'C'" label="菜单图标" prop="icon">
            <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
              <IconSelectApp ref="iconSelect" :active-icon="form.icon" @selected="selectedIcon" />
              <el-input slot="reference" v-model="form.icon" :prefix-icon="['iconfont', form.icon]" placeholder="点击选择图标" readonly />
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
          <el-col :span="12">
            <el-form-item label="显示排序" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 显示顺序 路由地址 -->
        <el-row>
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
          <el-col v-if="form.menuType === 'C'" :span="12">
            <el-form-item label="路由地址" prop="path">
              <span slot="label">
                <el-tooltip content="路由地址,page.json中对应的path如:首页->pages/index" placement="top">
                  <i class="el-icon-question" />
                </el-tooltip>
                路由地址
              </span>
              <el-input v-model="form.path" placeholder="请输入路由地址" />
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
import IconSelectApp from '@/components/IconSelectApp/index'

import { list, save, updateById, deleteByIds } from '@/api/system/sysMenus'

export default {
  components: {
    Treeselect,
    IconSelectApp
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
  computed: {
    // 判断父节点是否为目录
    isParentDirectory() {
      // 查找当前选中的父节点
      const parentNode = this.findParentNode(this.form.parentId)
      // 如果父节点是目录且不是主目录下的二级目录，则可以添加菜单
      return parentNode && parentNode.menuType === 'M'
    },
    // 判断父节点是否为菜单
    isParentMenu() {
      // 查找当前选中的父节点
      const parentNode = this.findParentNode(this.form.parentId)
      // 如果父节点是菜单，则可以添加按钮
      return parentNode && parentNode.menuType === 'C'
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
      debugger
      // 刷新逻辑
      list({}).then(res => {
        const { data } = res
        this.tableData = this.handleMenuTree(data, true)
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
        this.tableData = this.handleMenuTree(data, true)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 查找父节点
    findParentNode(id) {
      debugger
      if (id === -1) {
        return { id: -1, menuType: 'ROOT', title: '主类目' }
      }

      // 递归查找节点
      const findNode = (nodes, targetId) => {
        for (const node of nodes) {
          if (node.id === targetId) {
            return node
          }
          if (node.children && node.children.length) {
            const found = findNode(node.children, targetId)
            if (found) return found
          }
        }
        return null
      }

      // 在所有菜单中查找
      for (const option of this.menuOptions) {
        if (option.id === id) {
          return option
        }
        if (option.children && option.children.length) {
          const found = findNode(option.children, id)
          if (found) {
            return found
          }
        }
      }

      return null
    },
    // 监听上级菜单变化
    handleParentChange(value) {
      const parentNode = this.findParentNode(value)

      if (!parentNode) return

      // 根据父节点类型设置可选的菜单类型
      if (value === -1) {
        // 主目录下只能添加目录
        this.form.menuType = 'M'
      } else if (parentNode.menuType === 'M') {
        // 目录下只能添加菜单
        this.form.menuType = 'C'
      } else if (parentNode.menuType === 'C') {
        // 菜单下只能添加按钮
        this.form.menuType = 'F'
      }
    },
    // 新增按钮 被点击
    handleAdd(row) {
      this.dialogTitle = '新增菜单'
      this.dialogVisible = true
      this.btnType = 'insert'
      this.handleReset()

      // 先初始化菜单树，等待数据加载完成后再设置父节点和菜单类型
      this.InitPopUpMenusTree().then(() => {
        if (row && row.id) {
          this.form.parentId = row.id
          // 根据父节点类型设置可选的菜单类型
          const parentNode = this.findParentNode(row.id)
          if (parentNode) {
            if (parentNode.menuType === 'M') {
              this.form.menuType = 'C'
            } else if (parentNode.menuType === 'C') {
              this.form.menuType = 'F'
            }
          } else {
            // 如果找不到父节点，默认设置为菜单类型
            this.form.menuType = 'C'
          }
        } else {
          this.form.parentId = -1
          // 主目录下只能添加目录
          this.form.menuType = 'M'
        }
      })
    },

    // 编辑按钮 被点击
    handleEdit(row) {
      this.dialogTitle = '编辑菜单'
      this.dialogVisible = true
      this.btnType = 'update'
      // 先初始化菜单树，等待数据加载完成后再设置表单数据
      this.InitPopUpMenusTree().then(() => {
        this.form = { ...row }
      })
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
      // 返回Promise以便在数据加载完成后执行后续操作
      return new Promise((resolve) => {
        list({}).then(res => {
          const { data } = res
          this.menuOptions = []
          const menu = { id: -1, title: '主类目', menuType: 'ROOT', children: [] }
          menu.children = this.handleMenuTree(data, true)
          this.menuOptions.push(menu)
          this.loading = false
          resolve() // 数据加载完成，解析Promise
        }).catch(() => {
          this.loading = false
          resolve() // 即使出错也解析Promise
        })
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

<style scoped></style>
