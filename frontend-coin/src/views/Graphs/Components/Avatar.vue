<template>
  <div>
    <el-dropdown class="UserOpt" @command="handleUserCommand">
      <span class="el-dropdown-link">
        <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item command="changePassword">修改密码</el-dropdown-item>
        <el-dropdown-item command="logout">登出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <p class="UserName"><strong>{{this.userName}}</strong></p>
    <div class="UserPic">
      <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
    </div>
    <img class="gun" src="https://lililizi.oss-cn-beijing.aliyuncs.com/%E7%AB%96%E7%BA%BF.png" height="32" width="20"/>
    <el-dialog
      title="密码修改"
      :visible.sync="EditUserVisible"
      width="30%">
        <span>
          <el-input v-model="input_currentPassword" type="password" placeholder="请输入当前密码"></el-input>
          <el-input v-model="input_newPassword" type="password" placeholder="请输入需要要修改成的密码"></el-input>
          <el-input v-model="confirm_newPassword" type="password" placeholder="确认修改成的密码"></el-input>
        </span>
      <span slot="footer" class="dialog-footer">
          <el-button @click="EditUserVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleEditPasswordClose">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
  import {mapGetters, mapMutations, mapActions} from 'vuex'

  export default {
    name: "Avatar",
    computed: {
      ...mapGetters([
        'userParams'
      ])
    },
    data() {
      return {
        userName: localStorage.getItem('userName'),

        EditUserVisible: false,

        // 修改密码
        input_currentPassword: '',
        input_newPassword: '',
        confirm_newPassword: '',
      }
    },
    async mounted() {
      this.set_userParams({id: localStorage.getItem('userId')})
      await this.findUserParams()
    },
    methods: {
      ...mapMutations([
        'set_userParams',
        'set_userParamsClear',
      ]),

      ...mapActions([
        'findUserParams',
        'editUserParams',
      ]),

      handleUserCommand(command) {
        switch (command) {
          case 'changePassword':
            this.EditUserVisible = true;
            break;
          case 'logout':
            this.logOut();
            break;
          default:
            break;
        }
      },

      logOut() {
        console.log('log out')
        localStorage.removeItem('userName')
        localStorage.removeItem('userId')
        localStorage.removeItem('graphId')
        this.set_userParamsClear({})
        this.$router.push('/')
      },

      async handleEditPasswordClose() {
        if (this.userParams.password !== this.input_currentPassword) {
          this.$message.error('当前密码错误')
          this.clearEditGraph()
        } else if (this.input_currentPassword === this.input_newPassword) {
          this.$message.error('新旧密码不能一样')
          this.clearEditGraph()
        } else if (this.input_newPassword !== this.confirm_newPassword) {
          this.$message.error('两次密码输入不匹配')
          this.clearEditGraph()
        } else {
          this.set_userParams({
            password: this.input_newPassword,
          })
          await this.editUserParams()
          console.log(this.userParams)
          this.$message.success('密码修改成功，请重新登录')
          await this.$router.push('/')
        }
      },

      clearEditGraph() {
        this.input_currentPassword = ''
        this.input_newPassword = ''
        this.confirm_newPassword = ''
      }
    },
  }
</script>

<style scoped>
  .UserPic {
    float: right;
  }

  .UserName {
    float: right;
  }

  .gun {
    float: right;
    margin-top: 8px;
  }

  .el-dropdown-link {
    cursor: pointer;
    color: rgba(0, 0, 0, 0.94);
  }

  .el-input {
    margin-top: 10px;
  }

  .el-icon-arrow-down {
    font-size: 12px;
  }

  .UserOpt {
    margin-top: 17px;
    float: right;
    margin-right: 25px;
  }
</style>
