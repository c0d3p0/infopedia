import React, { Component } from "react";
import SearchbarComponent from "./SearchbarComponent";
import images from "../../Data/Images";
import infoPediaService from "../../Service/InfoPediaService"


export default class Searchbar extends Component
{
  constructor(props)
  {
    super(props);
    this.state =
    {
      searchData: "",
      searchTypeKey: "title",
      searchTypeMap:
      {
        title: "listArticlesByTitle",
        username: "listUsersByUsername"
      }
    };
  }

  handleChange = (event) =>
  {
    this.setState({[event.target.name]: event.target.value});
  }

  handleSubmit = (event) =>
  {
    event.preventDefault();
    const st = this.state.searchTypeMap[this.state.searchTypeKey];
    infoPediaService.executePageActionInMainArea(st, this.state.searchData);
  }

  render()
  {
    return (
      <SearchbarComponent
        searchImage={images.search}
        searchData={this.state.searchData}
        searchTypeKey={this.state.searchTypeKey}
        handleChange={this.handleChange}
        handleSubmit={this.handleSubmit}
      />
    );
  }
}
