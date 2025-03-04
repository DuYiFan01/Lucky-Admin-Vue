<template>
  <div class="dashboard-container">
    <h1>请文明使用在线预览,不要修改关键信息，如：用户密码，角色权限信息</h1>
    <div class="dashboard-text">id: {{ id }}</div>
    <div class="dashboard-text">username: {{ username }}</div>
    <div class="dashboard-text">name: {{ name }}</div>
    <div class="dashboard-text">avatar: {{ avatar }}</div>
    <div class="dashboard-text">token: {{ token }}</div>
    <div class="dashboard-text">roles: <el-tag v-for="role in roles" :key="role">{{ role }}</el-tag></div>
    <div class="dashboard-text">permissions: <el-tag v-for="permission in permissions" :key="permission" type="danger">{{ permission }}</el-tag></div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'id',
      'username',
      'name',
      'avatar',
      'token',
      'roles',
      'permissions'

    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 20px;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

    h1 {
      font-size: 24px;
      color: #606266;
      margin-bottom: 20px;
      text-align: center;
    }
  }
  &-text {
    font-size: 16px;
    line-height: 2;
    margin-bottom: 15px;
    color: #303133;

    .el-tag {
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }
}

@media screen and (max-width: 768px) {
  .dashboard {
    &-container {
      margin: 10px;
      padding: 15px;

      h1 {
        font-size: 20px;
        margin-bottom: 15px;
      }
    }
    &-text {
      font-size: 14px;
      line-height: 1.8;
      margin-bottom: 12px;
    }
  }
}

@media screen and (max-width: 480px) {
  .dashboard {
    &-container {
      margin: 5px;
      padding: 10px;

      h1 {
        font-size: 18px;
        margin-bottom: 12px;
      }
    }
    &-text {
      font-size: 13px;
      line-height: 1.6;
      margin-bottom: 10px;

      .el-tag {
        margin-right: 4px;
        margin-bottom: 4px;
        font-size: 12px;
      }
    }
  }
}
</style>
