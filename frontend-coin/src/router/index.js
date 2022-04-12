import Vue from 'vue'
import Router from 'vue-router'
import Perform from '@/views/Perform'
import Login from '@/views/Login/Login'
import Graphs from '@/views/Graphs/id'
import Home from '@/views/Home/home'
import WelcomePage from "../views/WelcomePage/WelcomePage";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/test',
      name: 'Test',
      component: Perform,
    },
    {
      path: '/graphs/:id',
      name: 'graphs',
      component: Graphs,
    },
    {
      path: '/home',
      name: 'home',
      component: Home,
    },
    {
      path: '/WelcomePage',
      name: 'WelcomePage',
      component: WelcomePage,
    },
  ],
  mode: 'history'
})
