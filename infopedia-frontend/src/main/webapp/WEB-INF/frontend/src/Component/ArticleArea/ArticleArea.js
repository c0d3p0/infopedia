import React, { Component } from "react";
import ArticleAreaComponent from "./ArticleAreaComponent";
import infoPediaService from "../../Service/InfoPediaService";


export default class ArticleArea extends Component
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
    this.setState({rightSideData: data, error: null});
  }

  handleReceiveDataError = (extras) =>
  {
    const error = "Problems trying to get articles by keywords!";
    this.setState({sideArticles: null, error: error});
  }

  getAuthor = (article) =>
  {
    return article && article.user ? article.user : {};
  }

  componentDidMount()
  {
    if(this.props.data)
    {
      infoPediaService.requestData("getArticlesByUserId", this.props.data.userId,
          null, null, this.handleReceiveData, this.handleReceiveDataError);
    }
  }
  
  render()
  {
    return (
      <ArticleAreaComponent
        article={this.props.data ? this.props.data : {}}
        author={this.getAuthor(this.props.data)}
        rightSideData={this.state.rightSideData ? this.state.rightSideData : []}
      />
    );
  }
}
