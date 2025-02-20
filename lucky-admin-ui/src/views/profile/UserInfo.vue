<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="80px">
    <el-form-item label="昵称" prop="name">
      <el-input v-model="form.name" maxlength="30" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="form.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model="form.phone" maxlength="50" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="form.sex">
        <el-radio label="1">男</el-radio>
        <el-radio label="0">女</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" size="mini" @click="submit">保存信息</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapGetters } from 'vuex'
import { updateInfo } from '@/api/user'

export default {
  name: 'UserInfo',
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          id: '',
          name: '',
          email: '',
          phone: '',
          sex: ''
        }
      }
    }
  },
  data() {
    return {
      form: {
        id: '',
        name: '',
        email: '',
        phone: '',
        sex: ''
      },
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ]
      }
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
    this.form = {
      id: this.id,
      name: this.name,
      email: this.email,
      phone: this.phone,
      sex: this.sex
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          updateInfo(this.form).then(res => {
            this.$message.success(res.message)
          })
        }
      })
    },
    close() {
      // this.$emit('close')
    }
  }

}
</script>
