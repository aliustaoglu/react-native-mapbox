const getFeatures = polyProps => {
  const features = polyProps.map(p => {
    return {
      type: "Feature",
      properties: {
        id: p.id,
        ...p.properties
      },
      geometry: {
        type: "LineString",
        coordinates: p.coordinates
      }
    };
  });
  const featureCollection = {
    type: "FeatureCollection",
    features
  };
  return featureCollection;
};

export const mergePolylines = props => {
  if (props.polylines) {
    const polylines = getFeatures(props.polylines);
    return Object.assign({}, props, { polylines });
  } else {
    return props;
  }
};
