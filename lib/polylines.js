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
  return features;
};

export const mergePolylines = props => {
  if (props.polylines) {
    const polylines = getFeatures(props.polylines);
    return Object.assign({}, props, { polylines });
  } else {
    return props;
  }
};
