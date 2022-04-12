import {
  addNodeAPI,
  deleteNodeAPI,
  getNodeInfoAPI,
  editNodeAPI,
  getSubNodeAPI,
} from "../../api/customNode";


const customNode = {
  state: {
    customNodeParams: {
      graphId: 0,
      name: '',
      x: 0,
      y: 0,
      typesetting_x: 0,
      typesetting_y: 0,
      shape: '',
      color: '#000000',
      node_size: 20,
      font_size: 20,
      isShown: true,
      isHighlighted: false,
    },
    currentNodeId: 0,
    subNodeList: [],
  },

  mutations: {
    set_customNodeParams: function (state, data) {
      state.customNodeParams = {
        ...state.customNodeParams,
        ...data,
      }
    },
    set_customNodeParamsClear: function (state, data) {
      state.customNodeParams = data
    },
    set_currentNodeId: function (state, data) {
      state.currentNodeId = data
    },
    set_subNodeList: function (state, data) {
      state.subNodeList = data
    }
  },

  actions: {
    addCustomNode: async ({state, commit}) => {
      const res = await addNodeAPI(state.customNodeParams)
      if (res) {
        commit('set_currentNodeId', res.data.content)
      }
    },
    deleteNode: async ({state, commit}) => {
      const res = await deleteNodeAPI(state.currentNodeId)
      if (res) {
        commit('set_currentNodeId', 0)
      }
    },
    getNodeInfo: async ({state, commit}) => {
      const res = await getNodeInfoAPI(state.currentNodeId)
      if (res) {
        commit('set_customNodeParams', res.data.content)
        commit('set_currentNodeId', 0)
      }
    },
    editCustomNode: async ({state, commit}) => {
      const res = await editNodeAPI(state.customNodeParams)
      if (res) {
        commit('set_customNodeParams', {
          graphId: 0,
          name: '',
          x: 0,
          y: 0,
          typesetting_x: 0,
          typesetting_y: 0,
          shape: '',
          color: '#000000',
          node_size: 20,
          font_size: 20,
          isShown: true,
          isHighlighted: false,
        })
      }
    },
    getSubNodes: async ({state, commit}) => {
      const res = await getSubNodeAPI(state.currentNodeId)
      console.log(res)
      if (res) {
        commit('set_subNodeList', res.data.content.content)
      }
    }
  }
}
export default customNode
