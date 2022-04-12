// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import router from './router'
import store from './store'
import axios from "axios";
import VueAxios from "vue-axios";
import * as echarts from 'echarts';
// import VueLive2D from 'vue-live2d-model'

Vue.prototype.$echarts = echarts

axios.defaults.baseURL = "/api"
axios.defaults.withCredentials = true
Vue.use(VueAxios, axios);
Vue.use(ElementUI);
// Vue.use(VueLive2D)

Vue.config.productionTip = false
axios.defaults.withCredentials = true //允许获取Cookie

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: {App},
  template: '<App/>',
})
