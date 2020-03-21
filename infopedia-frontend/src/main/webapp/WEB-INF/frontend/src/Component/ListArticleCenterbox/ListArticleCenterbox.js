import React, { Component } from "react";
import ListArticleCenterboxComponent from "./ListArticleCenterboxComponent";
import infoPediaService from "../../Service/InfoPediaService";


export default class ListArticleCenterbox extends Component
{
  constructor(props)
  {
    super(props);
    this.state = {};
  }

  handleClick = (event) =>
  {
    const articleId = event.currentTarget.getAttribute("value");
    const pa = infoPediaService.getActualPageAction();
    infoPediaService.executePageActionInMainArea(pa.itemsPageAction, articleId);
  }

  getArticles = () =>
  {
    if(Array.isArray(this.props.data))
      return this.props.data;
    else
      return this.props.data ? [this.props.data] : [];
  }

  render()
  {
    return (
      <ListArticleCenterboxComponent
        articles={this.getArticles()}
        pageContent={infoPediaService.getActualPageContent()}
        handleClick={this.handleClick}
      />
    );
  }
}
