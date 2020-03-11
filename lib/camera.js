import { NativeEventEmitter, NativeModules, Platform } from 'react-native'
import { invokeMapFunction } from '../index'

const getPromise = promiseName => {
  let emitter = null
  if (Platform.OS === 'ios') {
    emitter = new NativeEventEmitter(NativeModules.ReactNativeEventEmitter)
  } else {
    emitter = new NativeEventEmitter(NativeModules.UIManager.MapBoxViewController)
  }

  return new Promise((resolve, reject) => {
    const evtHandler = emitter.addListener(promiseName, result => {
      //console.log(`received ${promiseName}:`, result)
      resolve(result)
      evtHandler.remove()
    })
  })
}

const setCamera = (ref, cameraParams) => {
  invokeMapFunction('setCamera', ref, [cameraParams])
  return getPromise('onSetCamera')
}

const setBounds = (ref, boundsParams) => {
  invokeMapFunction('setBounds', ref, [boundsParams])
  return getPromise('onSetBounds')
}

const setPadding = (ref, paddingParams) => {
  invokeMapFunction('setPadding', ref, paddingParams)
  return getPromise('onSetPadding')
}

const getCameraPosition = ref => {
  invokeMapFunction('getCameraPosition', ref, [])
  return getPromise('onGetCameraPosition')
}

export default {
  setCamera,
  setBounds,
  setPadding,
  getCameraPosition
}
