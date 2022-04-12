<template>
  <div class="container">
    <div class="content">

      <div class="login-container animated fadeInDown" style="animation-delay:.3s;">
        <!--   Login   -->
        <div class="login justify-content-center" id="login-form">
          <h1 class="form-title"><i class="fas fa-user" style="color:#55a0ff;"></i>LOGIN</h1>

          <div class="form-container animated fadeIn" style="animation-delay:.7s;">
            <form>

              <label><i class="fas fa-at"></i> Name </label>
              <input type="text" name="name" placeholder="Name" v-model="userName">

              <label><i class="fab fa-slack-hash"></i> Password </label>
              <input type="password" name="password" placeholder="Password" v-model="userPassword">

              <div class="submit-buttons">
                <input type="primary" value="Connect" @click="userSubmit">
                <input type="button" value="Sign Up" class="btn-register" @click="toUserRegister">
              </div>

            </form>
          </div>
        </div>
        <!--   Login   -->

        <!--    Register    -->

        <div class="register justify-content-cente animatedr" style="animation-delay:.3s">
          <h1 class="form-title "><i class="fas fa-user-plus" style="color:#57efc4;"></i>REGISTER</h1>

          <div class="form-container animated fadeIn" style="animation-delay:.7s;">
            <form>
              <label><i class="fab fa-amilia"></i> Name </label>
              <input type="text" name="name" placeholder="Name" required autofocus v-model="userName">

              <label><i class="fab fa-slack-hash"></i> Password </label>
              <input type="password" name="password" placeholder="Password" v-model="userPassword">

              <label><i class="fab fa-slack-hash"></i> Confirm Password </label>
              <input type="password" name="password_confirmation" placeholder="Password" v-model="confirm">

              <div class="submit-buttons">
                <input type="primary" value="Register" style="background:#55efc4;" @click="userReg">
                <input type="button" value="Sign In" class="btn-login" @click="toUserLogin">
              </div>

            </form>
          </div>
        </div>
        <!--    Register    -->
        <div class="login animated fadeIn" style="animation-delay:.7s;animation-duration:4s;" id="login-bg"></div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapMutations, mapActions, mapGetters} from 'vuex'

  export default {
    name: "LoginIn",
    computed: {
      ...mapGetters([
        'userParams',
        'loginInSuccess',
        'errorMessage',
      ])
    },
    data() {
      return {
        userName: '',
        userPassword: '',
        confirm: '',
        isShow: false,
      }
    },
    methods: {
      ...mapMutations([
        'set_userParams',
        'set_userParamsClear',
      ]),

      ...mapActions([
        'userLogin',
        'userRegister',
      ]),

      toUserRegister() {
        setTimeout(function () {
          $('#login-form').removeClass('fadeIn');
          $('.register').removeClass(' zoomOut');
          $('#login-form').css('animation-delay', '0');
          $('.register').css(' animation-delay', '0');
        }, 10)

        $('#login-form').addClass('animated zoomOut');
        $('#login-form').css('display', 'none');
        $('.register').addClass('animated fadeIn');
        $('.register').css('display', 'block');
        $('.login-container').css('height', '95vh');
      },
      toUserLogin() {
        setTimeout(function () {
          $('.register').removeClass('fadeIn');
          $('#login-form').removeClass(' zoomOut');
        }, 10)


        $('.register').addClass('animated zoomOut');
        $('.register').css('display', 'none');
        $('#login-form').addClass('animated fadeIn');
        $('#login-form').css('display', 'block');
        $('.login-container').css('height', '70vh');
      },
      async userReg() {
        if (this.userName.length < 2 || this.userName.length > 16) {
          this.$message.error('用户名需有3-15位')
        } else if (this.userPassword.length < 5 || this.userPassword.length > 19) {
          this.userPassword = ''
          this.confirm = ''
          this.$message.error('密码需有6-18位')
        } else if (this.userPassword !== this.confirm) {
          this.userPassword = ''
          this.confirm = ''
          this.$message.error('两次密码输入不统一')
        } else {
          await this.set_userParamsClear({
            name: this.userName,
            password: this.userPassword
          })
          await this.userRegister()
          if (this.loginInSuccess) {
            localStorage.setItem('userName', this.userName)
            localStorage.setItem('userId', this.userParams.id)
            await this.$router.push('/WelcomePage')
          } else {
            await this.$message.error(this.errorMessage)
          }
        }
        this.userName = ''
        this.userPassword = ''
      },
      async userSubmit() {
        if (this.userName.length < 2 || this.userName.length > 16) {
          this.$message.error('用户名需有3-15位')
        } else if (this.userPassword.length < 5 || this.userPassword.length > 19) {
          this.userPassword = ''
          this.confirm = ''
          this.$message.error('密码需有6-18位')
        } else {
          await this.set_userParamsClear({
            name: this.userName,
            password: this.userPassword
          })
          await this.userLogin()
          if (this.loginInSuccess) {
            localStorage.setItem('userName', this.userName)
            localStorage.setItem('userId', this.userParams.id)
            await this.$router.push('/WelcomePage')
          } else {
            await this.$message.error(this.errorMessage)
          }
        }
        this.userName = ''
        this.userPassword = ''
        this.confirm = ''
      },

    },
  }

</script>

<style scoped>
  .content {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .login-container {
    background: white;
    position: relative;
    border-radius: 5px;
    display: flex;
    width: 70vw;
    height: 70vh;
    box-shadow: 0 19px 38px rgba(0, 0, 0, 0.07);
    transition: .4s;
  }


  .login {
    transition: .4s;
    font-family: Comfortaa;
  }

  .login#login-form {
    width: 50%;
    display: block;
    padding: 15px;
    align-items: center;
    text-align: center;
    position: relative;
    margin-top: 20px;
  }

  .login#login-bg {
    width: 50%;
    display: inline-flex;
    background-size: cover;
    background: url(https://picsum.photos/700) center center;
    float: right;
    border-radius: 0 5px 5px 0;
  }

  .register {
    width: 50%;
    display: block;
    padding: 15px;
    align-items: center;
    text-align: center;
    position: relative;
    margin-top: 30px;
    transition: .4s;
    font-family: Comfortaa;
    display: none;
  }


  .form-title {
    position: absolute;
    top: 11%;
    left: 0;
    right: 0;
    text-align: center;
    color: #dedede;
    font-size: 2.2em;
    font-family: Spartan;
    line-height: 1.4;
    margin-top: -24px;
  }

  .form-title hr {
    width: 75%;
    border: 2px solid #dedede1f;
    border-radius: 10px;
    margin-top: 20px;
  }

  .register .form-title {
    margin-top: -54px;
  }


  .form-container {
    padding: 25px 10px;
    border-radius: 5px;
    width: 80%;
    text-align: left;
    margin-top: 70px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }


  label {
    font-size: .7em;
    color: #7392ff;
    margin: 0px 0 !important;
    margin-left: 4px !important;
  }

  input {
    background: #f8fafc;
    border: 0;
    width: 100%;
    display: block;
    padding: 10px 15px;
    border-radius: 5px;
    margin-bottom: 20px !important;
    outline: 0;
    transition: .4s;
  }

  input::placeholder {
    opacity: .4;
  }

  .submit-buttons {
    display: flex;
  }

  .submit-buttons input {
    width: 50%;
    text-align: center;
    color: white;
    transition: .2s;
  }

  .submit-buttons input:hover {
    opacity: .7;
  }

  .submit-buttons input:nth-of-type(1) {
    border-radius: 5px 0 0 5px;
    background: #54a0ff;
  }

  .submit-buttons input:nth-of-type(2) {
    border-radius: 0 5px 5px 0;
    background: #c8d6e5;
  }

  @media (max-width: 933px) {
    .login#login-bg {
      width: 0;
    }

    .login#login-form, .register {
      width: 100%;
    }

    input {
      width: 100%;
    }
  }
</style>
