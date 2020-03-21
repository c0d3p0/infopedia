import React, { Component } from "react";
import ListUserCenterboxComponent from "./ListUserCenterboxComponent";
import infoPediaService from "../../Service/InfoPediaService";


export default class ListUserCenterbox extends Component
{
  constructor(props)
  {
    super(props);
    this.state = {};
  }

  handleClick = (event) =>
  {
    const userId = event.currentTarget.getAttribute("value");
    const pa = infoPediaService.getActualPageAction();
    infoPediaService.executePageActionInMainArea(pa.itemsPageAction, userId);
  }

  getUsers = () =>
  {
    if(Array.isArray(this.props.data))
      return this.props.data;
    else
      return this.props.data ? [this.props.data] : [];
  }

  render()
  {
    return (
      <ListUserCenterboxComponent
        users={this.getUsers()}
        pageContent={infoPediaService.getActualPageContent()}
        handleClick={this.handleClick}
      />
    );
  }
}
