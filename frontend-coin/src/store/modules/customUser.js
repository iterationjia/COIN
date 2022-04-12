import {
  userLoginInAPI,
  userRegisterAPI,
  userGraphFindAPI,
  userFindAPI, userEditAPI, favourStockAPI,
} from "../../api/user";
import {getRecommandAPI} from "../../api/customGraph";

const customUser = {
  state: {
    userParams: {},
    loginInSuccess: false,
    errorMessage: '',
    userGraphList: [],
    recommendList: [],
    favourStockName: '',
  },

  mutations: {
    set_userParams: function (state, data) {
      state.userParams = {
        ...state.userParams,
        ...data
      }
    },
    set_userParamsClear: function(state, data){
      state.userParams = data
    },
    set_loginInSuccess: function (state, data) {
      state.loginInSuccess = data
    },
    set_errorMessage: function (state, data) {
      state.errorMessage = data
    },
    set_userGraphList: function (state, data) {
      state.userGraphList = data
    },
    set_recommendList: function (state, data) {
      state.recommendList = data
    },
    set_favourStockName: function (state, data) {
      state.favourStockName = data
    }
  },

  actions: {
    userLogin: async ({state, commit}) => {
      await userLoginInAPI(state.userParams).then(res => {
        commit('set_loginInSuccess', res.data.success)
        if (state.loginInSuccess) {
          commit('set_userParams', {id: res.data.content})
        } else {
          commit('set_errorMessage', res.data.message)
        }
      })
    },
    userRegister: async ({state, commit}) => {
      await userRegisterAPI(state.userParams).then(res => {
        commit('set_loginInSuccess', res.data.success)
        if (state.loginInSuccess) {
          commit('set_userParams', {id: res.data.content})
        } else {
          commit('set_errorMessage', res.data.message)
        }
      })
    },
    getUserGraphList: async ({state, commit}) => {
      const res = await userGraphFindAPI(state.userParams.id)
      if (res) {
        commit('set_userGraphList', res.data.content)
      }
    },
    findUserParams: async ({state, commit}) => {
      const res = await userFindAPI(state.userParams.id)
      if (res) {
        commit('set_userParams', res.data.content)
      }
    },
    editUserParams: async ({state, commit}) => {
      const res = await userEditAPI(state.userParams)
    },
    getRecommendList: async ({state, commit}) => {
      const res = await getRecommandAPI(localStorage.getItem('userName'))
      if (res) {
        commit('set_recommendList', res.data.content)
      }
    },
    postUserFavourStock: async ({state}) => {
      const res = await favourStockAPI(state.userParams.name, state.favourStockName)
    }
  },
}

export default customUser
