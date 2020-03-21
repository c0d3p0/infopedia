import React from "react";

import "./AccountManagement.css";


export default function AccountManagementComponent(props)
{
  const sectionKeys = Object.keys(props.accountSectionMap);
  const dropdownElements = [];
  sectionKeys.forEach((key, index) =>
  {
    const section = props.accountSectionMap[key];

    if(section.dropdown === true)
    {
      dropdownElements.push(
        <button
          key={index}
          value={section.key}
          onClick={props.handleClick}
        >
          {section.title}
        </button>
      );
    }
  });

  return (
    <div className="AccountManagement RowLinedFlex">
      <div className="ColLinedFlex">
        <label>Welcome&#160;</label>
        <label title={props.user.username}>
          {props.user.username}
        </label>
        <img name="accountManagementImg" src={props.accountImage} alt="accountIcon"/>
      </div>
      <div className="ColLinedFlex">
        <div
          ref={(ref) => {props.setDropdown(ref)}}
          name="accountManagementDropdown"
          className="Dropdown"
        >
          <div className="RowLinedFlex">
            {dropdownElements}
          </div>
        </div>
      </div>
    </div>
  );
}