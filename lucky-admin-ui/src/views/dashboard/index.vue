<template>
  <div class="dashboard-container">
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
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
