import React, { Component } from "react";
import ListAreaComponent from "./ListAreaComponent";
import infoPediaService from "../../Service/InfoPediaService";


export default class ListArea extends Component
{
  constructor(props)
  {
    super(props);
    this.state = {};
  }

  handleReceiveData = (extras) =>
  {
    const rd = extras.apiResponseData;
    const data = infoPediaService.getArrayFromData(rd, "list");
    const newState = {error: null, [extras.stateFieldName]: data};
    this.setState(newState);
  }

  handleReceiveDataError = (extras) =>
  {
    const logMessage = "Problems trying to get the data!";
    const newState = {[extras.stateFieldName]: null};
    infoPediaService.handleReceiveDataError(this, extras, logMessage, newState);
  }
  
  componentDidMount()
  {
    infoPediaService.requestData("getRandomUsers", 8, null, null,
        this.handleReceiveData, this.handleReceiveDataError,
        {stateFieldName: "leftSideData"});
    infoPediaService.requestData("getRandomArticles", 8, null, null,
        this.handleReceiveData, this.handleReceiveDataError,
        {stateFieldName: "rightSideData"});
  }

  render()
  {
    return (
      <ListAreaComponent
        data={this.props.data}
        screenComponents={infoPediaService.getActualScreenComponents()}
        leftSideData={this.state.leftSideData}
        rightSideData={this.state.rightSideData}
      />
    );
  }
}
