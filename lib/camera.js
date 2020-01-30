import { invokeMapFunction } from '../index'

const setCamera = (ref, cameraParams) => {
  invokeMapFunction('setCamera', ref, [cameraParams])
}

const setBounds = (ref, boundsParams) => {
  invokeMapFunction('setBounds', ref, boundsParams)
}

export default {
  setCamera,
  setBounds
}