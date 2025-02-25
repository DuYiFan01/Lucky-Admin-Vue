<template>
  <div class="user-management">
    <div class="search-bar">
      <div class="grid-item">
        <span>
          表名称:
        </span>
        <el-input v-model="searchForm.name" placeholder="请输入表名称" style="width: 200px;" />
      </div>
      <div class="grid-item">
        <span>
          表描述:
        </span>
        <el-input v-model="searchForm.comment" placeholder="请输入表描述" style="width: 200px;" />
      </div>
      <div class="grid-item">
        <span>
          实体名称:
        </span>
        <el-input v-model="searchForm.entity" placeholder="请输入实体名称" style="width: 200px;" />
      </div>
      <div class="grid-item">
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      </div>
    </div>
    <div class="search-bar">
      <div class="grid-item">
        <span>
          生成包:
        </span>
        <el-input v-model="genPo.packageName" placeholder="请输入包路径" style="width: 200px;" />
        </div>
        <div class="grid-item">
        <span>
          生成模块:
        </span>
        <el-input v-model="genPo.mouldName" placeholder="请输入模块名称" style="width: 200px;" />
      </div>
      <div class="grid-item">
        <el-button
        :type="buttonBar.reFreshType"
        :size="buttonBar.size"
        :plain="buttonBar.plain"
        @click="handleRefresh"
      >刷新</el-button>
      <el-tag effect="plain">{{ genPo.packageName + '.' + genPo.mouldName }}</el-tag>
      </div>
    </div>
    <el-table
      v-loading="tableLoading"
      :data="tableData"
      style="width: 100%"
      max-height="560"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" v-bind="columnProps" />
      <el-table-column type="index" width="50" label="序号" v-bind="columnProps" />
      <el-table-column prop="name" label="表名称" v-bind="columnProps" />
      <el-table-column prop="comment" label="表描述" v-bind="columnProps" />
      <el-table-column prop="entity" label="实体名称" v-bind="columnProps" />
      <el-table-column prop="createTime" label="创建时间" v-bind="columnProps" />
      <el-table-column prop="updateTime" label="更新时间" v-bind="columnProps" />
      <el-table-column
        label="操作"
        width="200"
        fixed="right"
        :header-align="tableConfig.headerAlign"
      >
        <template slot-scope="scope">
          <el-button
            :size="toolBar.size"
            :type="toolBar.updateType"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
          >预览代码</el-button>
          <el-button
            :size="toolBar.size"
            :type="toolBar.updateType"
            icon="el-icon-download"
            @click="handleDownloadCode(scope.row)"
          >下载代码</el-button>
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
    <!-- 预览界面 -->
    <el-dialog
    :title="preview.title"
    :visible.sync="preview.open"
    :close-on-click-modal="false"
    center>
      <el-tabs v-model="preview.activeName" class="scrollbar">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :key="key"
          :label="key.indexOf('/')==-1?key:key.substring(key.indexOf('/')+1)"
          :name="key.indexOf('/')==-1?key:key.substring(key.indexOf('/')+1)"
        >
          <el-link
            v-clipboard:copy="value"
            v-clipboard:success="clipboardSuccess"
            :underline="false"
            icon="el-icon-document-copy"
            style="float:right"
          >复制</el-link>
          <pre><code class="hljs" v-html="highlightedCode(value, key)" /></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import { tableList, previewCode, downloadCode, getGenPo } from '@/api/tools/gen'
import clipboard from '@/directive/module/clipboard'
import hljs from 'highlight.js/lib/core'
import 'highlight.js/styles/github.css'
hljs.registerLanguage('java', require('highlight.js/lib/languages/java'))
hljs.registerLanguage('xml', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('html', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('vue', require('highlight.js/lib/languages/xml'))
hljs.registerLanguage('js', require('highlight.js/lib/languages/javascript'))
hljs.registerLanguage('sql', require('highlight.js/lib/languages/sql'))

export default {
  directives: {
  clipboard
  },
  data() {
    return {
      tableLoading: false,
      // 代码生成配置
      genPo: {
        tableName: '',
        packageName: 'cn.anlucky',
        mouldName: 'system'
      },
      // 预览界面
      preview: {
        title: '',
        open: false,
        activeName: '',
        data: []
      },
      // 表格样式设置
      tableConfig: {
        tooltip: true, // 表格内容长度过长时鼠标悬浮显示
        columnWidth: '180px', // 表格宽度
        align: 'center', // 内容对齐方式
        headerAlign: 'center' // 标题对齐方式
      },
      // 当前页码
      currentPage: 1,
      // 页码显示条数
      pageSize: 10,
      // 数据总条数
      total: 0,
      // 表格数据
      tableData: [],
      // 搜索条件
      searchForm: {
      },
      // 复选框选择的主键
      selectedIds: []
    }
  },
  computed: {
    // 计算属性
    columnProps() {
      return {
        showOverflowTooltip: this.tableConfig.tooltip,
        headerAlign: this.tableConfig.headerAlign,
        align: this.tableConfig.align,
        width: this.tableConfig.width
      }
    }
  },
  created() {
    this.getTableData({}, this.currentPage, this.pageSize)
    this.getGenPo()
  },
  methods: {
    // 获取Redis中的GenPo
    getGenPo() {
      getGenPo().then(res => {
        this.genPo = res.data
      })
    },
    // 下载代码按钮被点击
    handleDownloadCode(row) {
      this.genPo.tableName = row.name
      if (this.validateGenPo(this.genPo)) {
        downloadCode(this.genPo)
      }
    },
    // 校验表单
    validateGenPo(genPo) {
      const requiredFields = [
        { field: 'tableName', message: '请输入表名' },
        { field: 'packageName', message: '请输入包名' },
        { field: 'mouldName', message: '请输入模块名' }
      ]
      for (const { field, message } of requiredFields) {
        if (!genPo[field] || genPo[field].trim() === '') {
          this.$message.error(message)
          return false
        }
      }
      return true
    },
    /** 复制代码成功 */
    clipboardSuccess() {
      this.$message.success('复制成功')
    },
    /** 高亮显示 */
    highlightedCode(code, key) {
      const vmName = key.indexOf("/")==-1?key:key.substring(key.indexOf("/")+1)
      var language = vmName.substring(vmName.indexOf('.') + 1, vmName.length)
      const result = hljs.highlight(language, code, true)
      return result.value || '&nbsp;'
    },
    // 预览按钮被点击
    handlePreview(row) {
      this.genPo.tableName = row.name
      if (this.validateGenPo(this.genPo)) {
        this.preview.title = row.name + '表生成代码预览'
        this.preview.activeName = 'index.vue'
        this.preview.open = true
        this.getPreviewData()
      }
    },

    // 获取生成代码预览
    getPreviewData() {
      previewCode(this.genPo).then(res => {
        const { data } = res
        this.preview.data = data
      })
    },
    // 表格初始化获取数据
    getTableData(data, currentPage, pageSize) {
      this.tableLoading = true
      tableList(data, currentPage, pageSize).then(res => {
        const { data } = res
        this.currentPage = data.currentPage
        this.pageSize = data.pageSize
        this.total = data.total
        this.tableData = data.data
        this.tableLoading = false
      }).catch(() => {
        this.tableLoading = false
      })
    },
    // 搜索按钮被点击
    handleSearch() {
      this.handlePagination()
    },
    // 行数据复选框被选中
    handleSelectionChange(selection) {
      this.selectedUsers = selection
      this.selectedIds = selection.map(obj => obj.id)
    },
    // 导出按钮被点击
    handleExport() {
      // 假设这里有一个API调用导出用户
      // console.log('数据导出');
    },
    // 刷新按钮被点击
    handleRefresh() {
      this.handlePagination()
    },
    // 页码改变/页面显示条数改变
    handleSizeChange(newSize) {
      this.pageSize = newSize
      // console.log("页面显示条数被改变：" + newSize + "条");
      this.handlePagination()
    },
    // 页码被改变
    handleCurrentChange(newPage) {
      this.currentPage = newPage
      // console.log("页码被改变：" + newPage);
      this.handlePagination()
    },
    handlePagination() {
      this.getGenPo()
      this.getTableData(this.searchForm, this.currentPage, this.pageSize)
    }
  }
}
</script>

<style scoped>
.scrollbar {
    overflow: auto;
    overflow-x: hidden;
    max-height: 600px;
    padding: 10px 20px 0;
}
</style>
