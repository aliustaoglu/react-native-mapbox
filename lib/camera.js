import { invokeMapFunction } from '../index'

const setCamera = (ref, cameraParams) => {
  invokeMapFunction('setCamera', ref, [cameraParams])
}

export default {
  setCamera
}