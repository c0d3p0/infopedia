import React, { Component } from "react";
import AccountAccessComponent from "./AccountAccessComponent";
import htmlUtil from "../../Util/HtmlUtil";


export default class AccountAccess extends Component
{
  constructor(props)
  {
    super();
    this.state = {};
    this.loginDropdown = null;
    this.signUpDropdown = null;
  }

  handleClick = (event) =>
  {
    const elem = event.target;

    if(elem.tagName === "BUTTON")
    {
      if(elem.name === "loginButton")
        this.toggleDropdowns(true, false);
      else if(elem.name === "signUpButton")
        this.toggleDropdowns(false, true);
    }
    else if(!this.loginDropdown.contains(elem) &&
        !this.signUpDropdown.contains(elem))
    {
      this.toggleDropdowns(false, false);
    }
  }

  toggleDropdowns = (loginActive, signUpActive) =>
  {
    htmlUtil.toggleDropdown(this.loginDropdown, loginActive);
    htmlUtil.toggleDropdown(this.signUpDropdown, signUpActive);
  }

  setDropdown = (dropdown) =>
  {
    if(dropdown)
      this[dropdown.getAttribute("name")] = dropdown;
  }

  componentDidMount()
  {
    document.addEventListener('click', this.handleClick);
  }
  
  render()
  {
    return (
      <AccountAccessComponent
        setDropdown={this.setDropdown}
        handleLoginClick={this.handleLoginClick}
        handleSignUpClick={this.handleSignUpClick}
      />
    );
  }
}
