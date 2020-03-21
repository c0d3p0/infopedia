import React, { Component } from "react";
import ArticleCenterboxComponent from "./ArticleCenterboxComponent";


export default class ArticleCenterbox extends Component
{
  getFixedLinks = (content) =>
  {
    const regex = /(([\\]{1,2}[tsrn])|[\u0085\u2028\u2029])+/gm;
    let fc = content ? content.replace(regex, "\n").trim() : "";
    return fc.split("\n").filter((link) => link && link.replace(regex, ""));
  }
  
  getSubContents(article)
  {
    return article && article.subContentList ? article.subContentList : [];
  }

  render()
  {
    return (
      <ArticleCenterboxComponent
        article={this.props.article ? this.props.article : {}}
        subContents={this.getSubContents(this.props.article)}
        author={this.props.author ? this.props.author : {}}
        getFixedLinks={this.getFixedLinks}
      />
    );
  }
}
