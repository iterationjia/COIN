import axios from "axios";

const api = {
  customNodePre: '/api/node'
}

export function addNodeAPI(data) {
  return axios({
    url: `${api.customNodePre}/add`,
    method: 'POST',
    data
  })
}

export function deleteNodeAPI(nodeId) {
  return axios({
    url: `${api.customNodePre}/delete/${nodeId}`,
    method: 'GET',
  })
}

export function editNodeAPI(data) {
  return axios({
    url: `${api.customNodePre}/edit`,
    method: 'POST',
    data
  })
}

export function getNodeInfoAPI(nodeId) {
  return axios({
    url: `${api.customNodePre}/find/${nodeId}`,
    method: 'GET',
  })
}

export function getSubNodeAPI(id) {
  return axios({
    url: `${api.customNodePre}/findSubNodes/${id}`,
    method: 'GET',
  })
}
