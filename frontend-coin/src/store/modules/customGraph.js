import {
  addGraphAPI,
  deleteGraphAPI,
  editGraphAPI,
  loadGraphAPI,
  deleteAllNodesInGraphAPI,
  retrieveAllGraphAPI,
  retrieveAllNodesInGraphAPI,
  retrieveAllRelationsInGraphAPI,
  fuzzyMatchingAPI,
  typesettingAPI,
  getChatBotAnswerAPI,
} from '../../api/customGraph'

const customGraph = {
  state: {
    graphList: [],
    // 存放当前图谱数据信息
    customGraphRes: {
      nodes: [],
      links: [],
    },
    // 存放当前图谱基本信息
    customGraphParams: {},
    currentGraphId: 0,
    currentGraphName: '',
    file: null,
    searchString: '',
    fuzzyMatchingOutComeList: [],
    showModel: '',
    graphMovable: true,
    searchBoardVisible: false,
    numericalVisible: false,
    relationClickVisible: false,
    nodeClickVisible: false,
    chatBotRequest: '',
    chatBotAnswer: '',
    stockCardVisible: true,
  },
  mutations: {
    set_graphList: function (state, data) {
      state.graphList = data
    },
    set_customGraphRes: function (state, data) {
      state.customGraphRes.nodes = data.nodes
      state.customGraphRes.links = data.links
    },
    set_customGraphParams: function (state, data) {
      state.customGraphParams = {
        ...state.customGraphParams,
        ...data,
      }
    },
    set_customGraphParamsClear: function (state, data) {
      state.customGraphParams = data
    },
    set_currentGraphId: function (state, data) {
      state.currentGraphId = data
    },
    set_currentGraphName: function (state, data) {
      state.currentGraphName = data
    },
    set_loadFile: function (state, data) {
      state.file = data
    },
    set_searchString: function (state, data) {
      state.searchString = data
    },
    set_filterList: function (state, data) {
      state.filterList = data
    },
    set_fuzzyMatchingOutComeList: function (state, data) {
      state.fuzzyMatchingOutComeList = data
    },
    set_showModel: function (state, data) {
      state.showModel = data
    },
    set_graphMovable: function (state, data) {
      state.graphMovable = data
    },
    set_searchBoardVisible: function (state, data) {
      if (data) {
        state.searchBoardVisible = true
        state.numericalVisible = false
        state.nodeClickVisible = false
        state.relationClickVisible = false
      } else {
        state.searchBoardVisible = false
      }
    },
    set_numericalVisible: function (state, data) {
      if (data) {
        state.numericalVisible = true
        state.searchBoardVisible = false
        state.nodeClickVisible = false
        state.relationClickVisible = false
      } else {
        state.numericalVisible = false
      }
    },
    set_relationClickVisible: function (state, data) {
      console.log(data)
      if (data) {
        state.relationClickVisible = true
        state.nodeClickVisible = false
        state.searchBoardVisible = false
        state.numericalVisible = false
      } else {
        state.relationClickVisible = false
      }
    },
    set_nodeClickVisible: function (state, data) {
      if (data) {
        state.nodeClickVisible = true
        state.relationClickVisible = false
        state.numericalVisible = false
        state.searchBoardVisible = false
      } else {
        state.nodeClickVisible = false
      }
    },
    set_chatBotRequest: function (state, data) {
      state.chatBotRequest = data
    },
    set_chatBotAnswer: function (state, data) {
      state.chatBotAnswer = data
    },
    set_stockCardVisible: function (state, data) {
      state.stockCardVisible = data
    }
  },
  actions: {
    // 传入当前新增graph名称，将返回的graphId回传到currentGraphId
    addGraph: async ({commit, state}) => {
      console.log(state.customGraphParams)
      const res = await addGraphAPI(state.customGraphParams, localStorage.getItem('userId'))
      if (res) {
        commit('set_currentGraphId', res.data.content)
      }
    },

    deleteGraph: async ({commit, state}) => {
      const res = await deleteGraphAPI(state.currentGraphId)
      if (res) {
        commit('set_customGraphParams', {
          id: 0,
          name: '',
          url: '',
          time: '',
          isFavored: false,
          isNodeLabelVisible: true,
          isRelationLabelVisible: true,
        })
      }
    },

    deleteAllNodesInGraph: async ({commit, state}) => {
      const res = await deleteAllNodesInGraphAPI(state.currentGraphId)
    },

    editGraph: async ({commit, state}) => {
      console.log(state.customGraphParams)
      const res = await editGraphAPI(state.customGraphParams)
    },

    getCustomGraph: async ({commit, state}) => {
      const nodeRes = await retrieveAllNodesInGraphAPI(state.currentGraphId)
      const linkRes = await retrieveAllRelationsInGraphAPI(state.currentGraphId)
      if (nodeRes && linkRes) {
        commit('set_customGraphRes', {
          nodes: nodeRes.data.content,
          links: linkRes.data.content,
        })
      }
    },

    getAllGraph: async ({commit}) => {
      const res = await retrieveAllGraphAPI()
      if (res) {
        commit('set_graphList', res.data.content)
      }
    },

    loadGraph: async ({commit, state}) => {
      const res = await loadGraphAPI(state.file)
      if (res) {
        commit('set_customGraphParams', res.data.content)
      }
    },

    getFuzzyMatching: async ({commit, state}) => {
      const res = await fuzzyMatchingAPI(state.searchString)
      console.log(res)
      if (res) {
        commit('set_fuzzyMatchingOutComeList', res.data.content)
        commit('set_searchString', '')
      }
    },

    getTypeSetting: async ({commit, state}) => {
      const res = await typesettingAPI(state.currentGraphId)
      if (res) {
        commit('set_customGraphRes', {
          nodes: res.data.content,
          links: state.customGraphRes.links,
        })
      }
    },

    getChatBotAnswer: async ({commit, state}) => {
      const res = await getChatBotAnswerAPI(state.chatBotRequest)
      if (res) {
        console.log(res)
        commit('set_chatBotAnswer', res.data.content)
      }
    }
  },
}

export default customGraph
