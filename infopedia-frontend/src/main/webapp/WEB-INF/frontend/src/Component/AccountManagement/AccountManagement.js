import React, { Component } from "react";
import AccountManagementComponent from "./AccountManagementComponent";
import infoPediaService from "../../Service/InfoPediaService";
import accountSectionMap from "../../Data/AccountSectionMap";
import htmlUtil from "../../Util/HtmlUtil";

import images from "../../Data/Images";


export default class AccountManagement extends Component
{
  constructor(props)
  {
    super(props);
    this.state = {};
  }

  handleClick = (event) =>
  {
    event.preventDefault();
    const ask = event.target.getAttribute("value");
    const as = accountSectionMap[ask];
    const u = this.props.user;

    if(as.key === "logout")
    {
      const h = infoPediaService.createTokenHeaders();
      infoPediaService.requestData(as.pageAction, null, h, null,
          this.handleReceiveData, this.handleReceiveDataError);
    }
    else if(as.key === "yourData")
    {
      const h = infoPediaService.createTokenHeaders();
      infoPediaService.executePageActionInMainArea(as.pageAction, u.id, h);
    }
    else
      infoPediaService.executePageActionInMainArea(as.pageAction, u.id);
  }

	handleReceiveData = (extras) =>
	{
    infoPediaService.setUser(null);
	}

  handleReceiveDataError = (extras) =>
  {
		const logMessage = "Problems trying to logout the user!";
		infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }

  toggleDropdown = (event) =>
  {
    const elem = event.target;
    const dropdown = this.accountManagementDropdown;

    if(elem.tagName === "IMG" && elem.name === "accountManagementImg")
      htmlUtil.toggleDropdown(dropdown, true);
    else if(!dropdown.contains(elem))
      htmlUtil.toggleDropdown(dropdown, false);
  }

  setDropdown = (dropdown) =>
  {
    if(dropdown)
      this[dropdown.getAttribute("name")] = dropdown;
  }

  componentDidMount()
  {
    document.addEventListener('click', this.toggleDropdown);
  }

  render()
  {
    return (
      <AccountManagementComponent
        user={this.props.user}
        setDropdown={this.setDropdown}
        accountImage={images.account}
        accountSectionMap={accountSectionMap}
        handleClick={this.handleClick}
      />
    );
  }
}