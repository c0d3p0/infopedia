import React from "react";

import "./MainArea.css";


export default function MainAreaComponent(props)
{
  return (
    <props.component
      data={props.data}
      error={props.error}
    />
  );
}