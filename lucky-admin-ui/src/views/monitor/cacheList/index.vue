<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="8">
        <el-card style="height: calc(100vh - 125px)">
          <div slot="header">
            <span><i class="el-icon-collection" /> 缓存列表</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-refresh-right"
              @click="refreshCacheNames()"
            />
          </div>
          <el-table
            v-loading="loading"
            :data="cacheNames"
            :height="tableHeight"
            highlight-current-row
            style="width: 100%"
            @row-click="getCacheKeys"
          >
            <el-table-column
              label="序号"
              width="60"
              type="index"
            />

            <el-table-column
              label="缓存名称"
              align="center"
              prop="cacheName"
              :show-overflow-tooltip="true"
            />

            <el-table-column
              label="备注"
              align="center"
              prop="remark"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              v-if="checkPermission(['monitor::cacheList::delete'])"
              label="操作"
              width="60"
              align="center"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleClearCacheName(scope.row)"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card style="height: calc(100vh - 125px)">
          <div slot="header">
            <span><i class="el-icon-key" /> 键名列表</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-refresh-right"
              @click="refreshCacheKeys()"
            />
          </div>
          <el-table
            v-loading="subLoading"
            :data="cacheKeys"
            :height="tableHeight"
            highlight-current-row
            style="width: 100%"
            @row-click="handleCacheValue"
          >
            <el-table-column
              label="序号"
              width="60"
              type="index"
            />
            <el-table-column
              label="缓存键名"
              align="center"
              :formatter="keyFormatter"
              :show-overflow-tooltip="true"
            />
            <el-table-column
              v-if="checkPermission(['monitor::cacheList::delete'])"
              label="操作"
              width="60"
              align="center"
              class-name="small-padding fixed-width"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleClearCacheKey(scope.row)"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card :bordered="false" style="height: calc(100vh - 125px)">
          <div slot="header">
            <span><i class="el-icon-document" /> 缓存内容</span>
            <el-button
              v-if="checkPermission(['monitor::cacheList::delete'])"
              style="float: right; padding: 3px 0"
              type="text"
              icon="el-icon-refresh-right"
              @click="handleClearCacheAll()"
            >清理全部</el-button>
          </div>
          <el-form :model="cacheForm">
            <el-row :gutter="32">
              <el-col :offset="1" :span="22">
                <el-form-item label="缓存名称:" prop="cacheName">
                  <el-input v-model="cacheForm.cacheName" :read-only="true" />
                </el-form-item>
              </el-col>
              <el-col :offset="1" :span="22">
                <el-form-item label="缓存键名:" prop="cacheKey">
                  <el-input v-model="cacheForm.cacheKey" :read-only="true" />
                </el-form-item>
              </el-col>
              <el-col :offset="1" :span="22">
                <el-form-item label="缓存内容:" prop="cacheValue">
                  <el-input
                    v-model="cacheForm.cacheValue"
                    type="textarea"
                    :rows="8"
                    :read-only="true"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { nameList, keyListByCacheName, cacheValueByKey, deleteCacheNameByCacheName, deleteCacheKeyByCacheKey, deleteCacheAll } from '@/api/monitor/cacheList'

export default {
  name: 'CacheList',
  data() {
    return {
      cacheNames: [],
      cacheKeys: [],
      cacheForm: {},
      loading: true,
      subLoading: false,
      nowCacheName: '',
      tableHeight: window.innerHeight - 200
    }
  },
  created() {
    this.getCacheNames()
  },
  methods: {
    /** 查询缓存名称列表 */
    getCacheNames() {
      this.loading = true
      nameList().then(response => {
        const { data } = response
        this.cacheNames = data
        this.loading = false
      })
    },
    /** 刷新缓存名称列表 */
    refreshCacheNames() {
      this.getCacheNames()
      this.$message.success('刷新缓存名称列表成功')
    },
    /** 清理指定名称缓存 */
    handleClearCacheName(row) {
      deleteCacheNameByCacheName(row.cacheName).then(response => {
        this.$message.success('清理缓存名称[' + row.cacheName + ']成功')
        this.getCacheKeys()
      })
    },
    /** 查询缓存键名列表 */
    getCacheKeys(row) {
      const cacheName = row !== undefined ? row.cacheName : this.nowCacheName
      if (cacheName === '') {
        return
      }
      this.subLoading = true
      keyListByCacheName(cacheName).then(response => {
        const { data } = response
        this.cacheKeys = data
        this.subLoading = false
        this.nowCacheName = cacheName
      })
    },
    /** 刷新缓存键名列表 */
    refreshCacheKeys() {
      this.getCacheKeys()
      // this.$message.success("刷新键名列表成功");
    },
    /** 清理指定键名缓存 */
    handleClearCacheKey(cacheKey) {
      deleteCacheKeyByCacheKey(cacheKey).then(response => {
        // this.$message.success("清理缓存键名[" + cacheKey + "]成功");
        this.getCacheKeys()
      })
    },
    /** 键名前缀去除 */
    keyFormatter(cacheKey) {
      // return cacheKey.replace(this.nowCacheName, "");
      return cacheKey
    },
    /** 查询缓存内容详细 */
    handleCacheValue(cacheKey) {
      cacheValueByKey(this.nowCacheName, cacheKey).then(response => {
        this.cacheForm = response.data
      })
    },
    /** 清理全部缓存 */
    handleClearCacheAll() {
      deleteCacheAll().then(response => {
        this.$message.success('清理全部缓存成功')
      })
    }
  }
}
</script>
