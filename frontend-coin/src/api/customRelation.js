import axios from "axios";

const api = {
  customRelationPre: '/api/relation'
}


export function addRelationAPI(data) {
  console.log(data)
  return axios({
    url: `${api.customRelationPre}/add`,
    method: 'POST',
    data
  })
}

export function deleteRelationAPI(id) {
  return axios({
    url: `${api.customRelationPre}/delete/${id}`,
    method: 'GET',
  })
}

export function editRelationAPI(data) {
  return axios({
    url: `${api.customRelationPre}/edit`,
    method: 'POST',
    data
  })
}

export function getRelationInfoAPI(source, target) {
  return axios({
    url: `${api.customRelationPre}/find/${source}/${target}`,
    method: 'GET',
  })
}
