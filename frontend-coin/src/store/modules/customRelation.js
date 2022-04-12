import {
  addRelationAPI,
  deleteRelationAPI,
  getRelationInfoAPI,
  editRelationAPI,
} from "../../api/customRelation";

const customRelation = {
  state: {
    customRelationParams: {
      source: 0,
      target: 0,
      graphId: 0,
      name: '',
      shown: true,
      solid: true,
      isHighlighted: false,
    },
    currentLinkId: 0,
  },

  mutations: {
    set_customRelationParams: function (state, data) {
      state.customRelationParams = {
        ...state.customRelationParams,
        ...data,
      }
    },
    set_currentLink: function (state, data) {
      state.currentLinkId = data
    },
  },

  actions: {
    addCustomRelation: async ({state, commit}) => {
      console.log(state.customRelationParams)
      const res = await addRelationAPI(state.customRelationParams)
      if (res) {
        commit('set_customRelationParams', {
          source: 0,
          target: 0,
          graphId: 0,
          name: '',
          shown: true,
          solid: true,
          isHighlighted: false,
        })
      }
    },
    deleteRelation: async ({state, commit}) => {
      const res = await deleteRelationAPI(state.currentLinkId)
      if (res) {
        commit('set_customRelationParams', {
          source: 0,
          target: 0,
          graphId: 0,
          name: '',
          shown: true,
          solid: true,
          isHighlighted: false,
        })
      }
    },
    getRelationInfo: async ({state, commit}) => {
      const res = await getRelationInfoAPI(state.currentLinkId)
      if (res) {
        commit('set_customRelationParams', res.data.content)
        commit('set_currentLink', this.customRelationParams.id)
      }
    },
    editRelation: async ({state, commit}) => {
      const res = editRelationAPI(state.customRelationParams)
      if (res) {
        commit('set_customRelationParams', {
          source: 0,
          target: 0,
          graphId: 0,
          name: '',
          shown: true,
          solid: true,
          isHighlighted: false,
        })
      }
    }
  }
}

export default customRelation
