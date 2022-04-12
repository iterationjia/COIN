const getters = {
  //customNode

  customNodeParams: state => state.customNode.customNodeParams,
  currentNodeId: state => state.customNode.currentNodeId,
  subNodeList: state => state.customNode.subNodeList,

  //customRelation

  customRelationParams: state => state.customRelation.customRelationParams,
  currentLinkId: state => state.customRelation.currentLinkId,

  //customGraph

  graphList: state => state.customGraph.graphList,
  // 存放当前图谱数据信息
  customGraphRes: state => state.customGraph.customGraphRes,
  // 存放当前图谱基本信息
  customGraphParams: state => state.customGraph.customGraphParams,
  currentGraphId: state => state.customGraph.currentGraphId,
  currentGraphName: state => state.customGraph.currentGraphName,
  file: state => state.customGraph.file,
  fuzzyMatchingOutComeList: state => state.customGraph.fuzzyMatchingOutComeList,
  showModel: state => state.customGraph.showModel,
  searchString: state => state.customGraph.searchString,
  graphMovable: state => state.customGraph.graphMovable,
  searchBoardVisible: state => state.customGraph.searchBoardVisible,
  numericalVisible: state => state.customGraph.numericalVisible,
  nodeClickVisible: state => state.customGraph.nodeClickVisible,
  relationClickVisible: state => state.customGraph.relationClickVisible,
  chatBotAnswer: state => state.customGraph.chatBotAnswer,
  chatBotRequest: state => state.customGraph.chatBotRequest,
  stockCardVisible: state => state.customGraph.stockCardVisible,

  //customUser
  userParams: state => state.customUser.userParams,
  loginInSuccess: state => state.customUser.loginInSuccess,
  errorMessage: state => state.customUser.errorMessage,
  userGraphList: state => state.customUser.userGraphList,
  recommendList: state => state.customUser.recommendList,
  favourStockName: state => state.customUser.favourStockName,
}

export default getters
