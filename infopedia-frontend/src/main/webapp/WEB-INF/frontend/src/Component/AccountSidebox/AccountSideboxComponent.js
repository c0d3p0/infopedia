import React from "react";

import "./AccountSidebox.css";


export default function AccountSideboxComponent(props)
{
  const sectionKeys = Object.keys(props.accountSectionMap);
  const sectionElements = [];
  sectionKeys.forEach((key, index) =>
  {
    const section = props.accountSectionMap[key];
    
    if(section.sidebox === true)
    {
      sectionElements.push(
        <div
          key={index}
          value={section.key}
          onClick={props.handleClick}
        >
          <span>{section.title}</span>
        </div>
      );
    }
  });

  return (
    <div className="AccountSidebox Contentbox Sidebox RowLinedFlex">
      <h3>{props.title}</h3>
      {sectionElements}
    </div>
  );

}