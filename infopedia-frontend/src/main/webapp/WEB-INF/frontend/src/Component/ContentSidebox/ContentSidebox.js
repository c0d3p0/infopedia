import React, { Component } from "react";
import ContentSideboxComponent from "./ContentSideboxComponent";
import jsUtil from "../../Util/JSUtil";


export default class ContentSidebox extends Component
{
  constructor(props)
  {
    super(props);
    this.state = {title: "Contents"};
  }

  handleClick = (event) =>
  {
    const content = event.currentTarget.getAttribute("value");
    const element = document.getElementById(content);
    window.scrollTo(0, element.offsetTop);
  }

  getContentTitles = () =>
  {
    let article = this.props.article;
    let contents = [];

    if(jsUtil.isValidObject(article))
    {
      const subContents = article.subContentList;

      if(Array.isArray(subContents))
        contents = subContents.map(subContent => subContent.title);
    }

    return contents;
  }
  
  render()
  {
    return (
      <ContentSideboxComponent
        title={this.state.title}
        contents={this.getContentTitles()}
        handleClick={this.handleClick}
      />
    );
  }
}