<template>
  <div class="app-container">
    <div>
      <el-row :gutter="20">
        <el-col :span="6" :xs="24">
          <UserCard :user-card-info="userCardInfo" :user-info-array="userInfoArray" />
        </el-col>
        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="个人资料" name="activity">
                <UserInfo />
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="timeline">
                <resetPwd />
              </el-tab-pane>
              <!-- <el-tab-pane label="通知公告" name="account">
                                <account :user="user" />
                            </el-tab-pane> -->
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import UserCard from './UserCard.vue'
import UserInfo from './UserInfo.vue'
import resetPwd from './resetPwd.vue'

export default {
  name: 'Profile',
  components: { UserCard, UserInfo, resetPwd }, // Activity, Timeline, Account
  data() {
    return {
      userCardInfo: {
        name: '',
        avatar: ''
      },
      userInfoArray: [
        {
          iconClass: 'el-icon-user',
          lable: '用户名',
          value: 'admin'
        }
      ],
      activeTab: 'activity'
    }
  },
  computed: {
    ...mapGetters([
      'id',
      'username',
      'name',
      'sex',
      'email',
      'phone',
      'avatar'
    ])
  },
  created() {
    this.getUser()
    this.initData()
  },
  methods: {
    getUser() {
      this.resetUserInfo()
      // store.dispatch('user/getInfo')
    },
    initData() {
      this.userCardInfo = {
        name: this.name,
        avatar: this.avatar
      }
      this.userInfoArray = [
        {
          iconClass: 'guide',
          lable: '用户名',
          value: this.username
        },
        {
          iconClass: 'guide',
          lable: '昵称',
          value: this.name
        },
        {
          iconClass: 'guide',
          lable: '性别',
          value: this.sex === '0' ? '女' : (this.sex === '1' ? '男' : '未知')
        },
        {
          iconClass: 'guide',
          lable: '邮箱',
          value: this.email
        },
        {
          iconClass: 'guide',
          lable: '手机号',
          value: this.phone
        }
      ]
    }
  }
}
</script>
