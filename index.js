import React from 'react'
import { requireNativeComponent, UIManager, findNodeHandle } from 'react-native'
import { mergePolylines } from './lib/polylines'

const moduleName = 'MapBoxViewController'

const NativeMap = requireNativeComponent(moduleName, null)

class RNMBReactNativeMapbox extends React.Component {
  constructor(props) {
    super(props)
  }

  mergeProps(props) {
    let newProps = mergePolylines(props)
    return newProps
  }

  render() {
    const merged = this.mergeProps(this.props)
    return <NativeMap {...merged} ref={merged.mapRef} />
  }
}

const invokeNativeUIFunction = (moduleName, functionName, ref, params) => {
  if (Platform.OS === 'ios') {
    const func = NativeModules[moduleName][functionName]
    func(params)
  } else {
    const handle = findNodeHandle(ref)
    UIManager.dispatchViewManagerCommand(handle, functionName, params)
  }
}

export const invokeMapFunction = (functionName, ref, params) => {
  invokeNativeUIFunction(moduleName, functionName, ref, params)
}

export default RNMBReactNativeMapbox
