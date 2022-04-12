import axios from "axios";

const api = {
  userPre: '/api/user'
}

export function userLoginInAPI(data) {
  return axios({
    url: `${api.userPre}/login`,
    method: 'POST',
    data
  })
}

export function userRegisterAPI(data) {
  return axios({
    url: `${api.userPre}/register`,
    method: 'POST',
    data
  })
}

export function userDeleteAPI(userId) {
  return axios({
    url: `${api.userPre}/delete/${userId}`,
    method: 'GET'
  })
}

export function userGraphDeleteAPI(userId) {
  return axios({
    url: `${api.userPre}/deleteAllGraphs/${userId}`,
    method: 'GET',
  })
}

export function userEditAPI(data) {
  return axios({
    url: `${api.userPre}/edit`,
    method: 'POST',
    data,
  })
}

export function userFindAPI(userId) {
  return axios({
    url: `${api.userPre}/find/${userId}`,
    method: 'GET',
  })
}

export function userGraphFindAPI(userId) {
  return axios({
    url: `${api.userPre}/findGraphs/${userId}`,
    method: 'GET',
  })
}

export function favourStockAPI(userName,stockName) {
  return axios({
    url: `${api.userPre}/listFavors/${userName}/${stockName}`,
    method: 'GET',
  })
}
