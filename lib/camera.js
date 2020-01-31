import { NativeEventEmitter, NativeModules, Platform } from 'react-native'
import { invokeMapFunction } from '../index'

const setCamera = (ref, cameraParams) => {
  invokeMapFunction('setCamera', ref, [cameraParams])
}

const setBounds = (ref, boundsParams) => {
  invokeMapFunction('setBounds', ref, boundsParams)
}

const getCameraPosition = ref => {
  invokeMapFunction('getCameraPosition', ref, [])

  // Android does not support returning Promise from UI Component method
  // So Native UI component will emmit the response so need to use a listener
  if (Platform.OS === 'android') {
    const cameraPositionEmitter = new NativeEventEmitter(NativeModules.UIManager.MapBoxViewController)

    return new Promise((resolve, reject) => {
      const evtHandler = cameraPositionEmitter.addListener('onGetCameraPosition', result => {
        resolve(result)
        evtHandler.remove()
      })
    })
  }
}

export default {
  setCamera,
  setBounds,
  getCameraPosition
}
