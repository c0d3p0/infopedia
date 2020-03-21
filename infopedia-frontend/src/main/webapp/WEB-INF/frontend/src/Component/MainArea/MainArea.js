import React, { Component } from "react";
import MainAreaComponent from "./MainAreaComponent";

import infoPediaService from "../../Service/InfoPediaService";



export default class MainArea extends Component
{
  constructor(props)
  {
    super(props);
    const pa = infoPediaService.getPageAction("listRandomArticles");
    this.state = this.createEmptyState(pa);
    this.state.newInstanceParams = 10;
    this.bindExternalMethods();
  }

  handleReceiveData = (extras) =>
  {
    let newState = this.createEmptyState(extras.pageAction);
    this.addDataAttribute(newState, extras.apiResponseData);
    this.setState(newState);
  }

  handleReceiveDataError = (extras) =>
  {
    const logMessage = "Problems trying to get the data!";
    infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }

  setActualPageAction = (pageAction) =>
  {
    let newState = this.createEmptyState(pageAction);
    this.setState(newState);
  }

  getActualPageAction = () =>
  {
    return this.state.actualPageAction;
  }

  addDataAttribute = (object, data) =>
  {
    let nd = data ? data : {};

    if(Array.isArray(nd.list))
      nd = nd.list;

    if(Array.isArray(nd) && nd.length === 1)
      object.data = nd[0];
    else
      object.data = nd;
  }

  createEmptyState = (pageAction) =>
  {
    return ({
      actualPageAction: pageAction,
      data: null,
      error: null
    });
  }

  bindExternalMethods = () =>
  {
    const ma = {}
    ma.getActualPageAction = this.getActualPageAction;
    ma.handleReceiveData = this.handleReceiveData;
    ma.handleReceiveDataError = this.handleReceiveDataError;
    ma.setActualPageAction = this.setActualPageAction;
    infoPediaService.setMainArea(ma);
  }

  getMainComponent = () =>
  {
    return infoPediaService.getScreenComponents(
        this.state.actualPageAction).main;
  }

  componentDidMount()
  { 
    const pak = this.state.actualPageAction.key;
    const params = this.state.newInstanceParams;
    infoPediaService.executePageActionInMainArea(pak, params);
  }

  render()
  {
    return (
      <MainAreaComponent
        component={this.getMainComponent()}
        data={this.state.data}
        error={this.state.error}
      />
    );
  }
}
