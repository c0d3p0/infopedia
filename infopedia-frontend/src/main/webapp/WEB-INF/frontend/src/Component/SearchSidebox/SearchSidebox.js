import React, { Component } from "react";
import SearchSideboxComponent from "./SearchSideboxComponent";
import infoPediaService from "../../Service/InfoPediaService"


export default class SearchSidebox extends Component
{
  handleClick = (event) =>
  {
    const pa = this.props.itemPageAction;
    const id = event.currentTarget.getAttribute("value");
    infoPediaService.executePageActionInMainArea(pa, id);
  }

  render()
  {
    return (
      <SearchSideboxComponent
        title={this.props.title}
        data={this.props.data ? this.props.data : []}
        labelFieldName={this.props.labelFieldName ? this.props.labelFieldName : ""}
        handleClick={this.handleClick}
      />
    );
  }
}
