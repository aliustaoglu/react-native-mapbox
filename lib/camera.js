import { NativeEventEmitter, NativeModules, Platform } from 'react-native'
import { invokeMapFunction } from '../index'

const setCamera = (ref, cameraParams) => {
  invokeMapFunction('setCamera', ref, [cameraParams])
}

const setBounds = (ref, boundsParams) => {
  invokeMapFunction('setBounds', ref, boundsParams)
}

const setPadding = (ref, paddingParams) => {
  invokeMapFunction('setPadding', ref, paddingParams)
}

const getCameraPosition = ref => {
  invokeMapFunction('getCameraPosition', ref, [])

  let cameraPositionEmitter = null
  if (Platform.OS === 'ios') {
    cameraPositionEmitter = new NativeEventEmitter(NativeModules.ReactNativeEventEmitter)
  } else {
    cameraPositionEmitter = new NativeEventEmitter(NativeModules.UIManager.MapBoxViewController)
  }

  return new Promise((resolve, reject) => {
    const evtHandler = cameraPositionEmitter.addListener('onGetCameraPosition', result => {
      resolve(result)
      evtHandler.remove()
    })
  })
}

export default {
  setCamera,
  setBounds,
  setPadding,
  getCameraPosition
}
