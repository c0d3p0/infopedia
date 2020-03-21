import React from "react";

import "./ContentSidebox.css";


export default function ContentSideboxComponent(props)
{
  let contentsElements = props.contents.map((content, index) =>
  {
    return (
      <div
        key={index}
        value={content}
        onClick={props.handleClick}
      >
        <label>{index + 1} - </label>
        <span title={content}>{content}</span>
      </div>
    );
  });

  if(contentsElements.length < 1)
    contentsElements = (<label>No sub content.</label>);

  return (
    <div className="Contentbox Sidebox RowLinedFlex">
      <h3>{props.title}</h3>
      {contentsElements}
    </div>
  );
}