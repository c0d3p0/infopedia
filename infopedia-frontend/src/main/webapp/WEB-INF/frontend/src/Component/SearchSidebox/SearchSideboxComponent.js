import React from "react";

import "./SearchSidebox.css";


export default function SearchSideboxComponent(props)
{
  let elements = props.data.map((data, index) =>
  {
    let dataContent = data[props.labelFieldName];
    return (
      <div
        key={index}
        value={data.id}
        onClick={props.handleClick}
      >
        <span title={dataContent}>{dataContent}</span>
      </div>
    );
  });

  if(elements.length < 1)
    elements = (<label>No data found.</label>);

  return (
    <div className="SearchSidebox Contentbox Sidebox RowLinedFlex">
      <h3 title={props.title}>{props.title}</h3>
      {elements}
    </div>
  );
}
