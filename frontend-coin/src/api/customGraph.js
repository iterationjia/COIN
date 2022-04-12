import axios from "axios";

const api = {
  customGraphPre: '/api/graph'
}

export function addGraphAPI(data, userId) {
  return axios({
    url: `${api.customGraphPre}/add/${userId}`,
    method: 'POST',
    data,
  })
}

export function deleteGraphAPI(id) {
  return axios({
    url: `${api.customGraphPre}/delete/${id}`,
    method: 'GET',
  })
}

export function deleteAllNodesInGraphAPI(id) {
  return axios({
    url: `${api.customGraphPre}/deleteAll/${id}`,
    method: 'GET',
  })
}

export function editGraphAPI(data) {
  return axios({
    url: `${api.customGraphPre}/edit`,
    method: 'POST',
    data,
  })
}

export function retrieveAllNodesInGraphAPI(id) {
  return axios({
    url: `${api.customGraphPre}/findAllNodes/${id}`,
    method: 'GET',
  })
}

export function retrieveAllRelationsInGraphAPI(id) {
  return axios({
    url: `${api.customGraphPre}/findAllRelations/${id}`,
    method: 'GET',
  })
}

export function retrieveAllGraphAPI() {
  return axios({
    url: `${api.customGraphPre}/findAllGraphs`,
    method: 'GET',
  })
}

export function loadGraphAPI(data) {
  return axios({
    url: `${api.customGraphPre}/loadGraph`,
    method: 'POST',
    data,
  })
}

export function fuzzyMatchingAPI(string) {
  return axios({
    url: `${api.customGraphPre}/fuzzyMatching/${string}`,
    method: 'POST',
  })
}

export function typesettingAPI(id) {
  return axios({
    url: `${api.customGraphPre}/typesetting/${id}`,
    method: 'GET',
  })
}

export function getChatBotAnswerAPI(data) {
  return axios({
    url: `${api.customGraphPre}/chatBot/admin?question=${data}`,
    method: 'POST',
  })
}

export function getRecommandAPI(userName) {
  return axios({
    url: `${api.customGraphPre}/recommand/${userName}`,
    method: 'GET',
  })
}
