import React, { Component } from "react";
import HeaderComponent from "./HeaderComponent";
import AccountAccess from "../AccountAccess/AccountAccess"
import AccountManagement from "../AccountManagement/AccountManagement"
import infoPediaService from "../../Service/InfoPediaService";
import jsUtil from "../../Util/JSUtil";

import images from "../../Data/Images";


export default class Header extends Component
{
  constructor(props)
  {
    super(props);
    this.state = {user: null};
  }

  handleReceiveData = (extras) =>
  {
    this.setUser(extras.apiResponseData);
  }

  handleReceiveDataError = (extras) =>
  {
    const logMessage = "Problems trying to get the data!";
    infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }

  bindMethods = () =>
  {
    const header = {}
    header.handleReceiveData = this.handleReceiveData;
    header.handleReceiveDataError = this.handleReceiveDataError;
    header.getUser = this.getUser;
    header.setUser = this.setUser;
    infoPediaService.setHeader(header);
  }

  setUser = (user) =>
  {
    let u = (user && user.username && user.token) ? 
        Object.assign({}, user) : null;
    
    this.setState({user: u}, () =>
    {
      if(u === null && infoPediaService.isActualPageActionUserSpecific())
        infoPediaService.executePageActionInMainArea("listRandomArticles", 6);
    });
  }

  getUser = () =>
  {
    return this.state.user;
  }

  getAccountComponent = () =>
  {
    if(this.state.user)
      return AccountManagement;
    
    return AccountAccess;
  }

  componentDidMount()
  {
    this.bindMethods();
  }

  render()
  {
    return (
      <HeaderComponent
        logoImage={images.logo}
        accountComponent={this.getAccountComponent()}
        user={this.state.user}
      />
    );
  }
}
