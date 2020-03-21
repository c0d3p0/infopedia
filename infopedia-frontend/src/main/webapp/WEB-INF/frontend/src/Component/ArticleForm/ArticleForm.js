import React, { Component } from "react";
import ArticleFormComponent from "./ArticleFormComponent";
import infoPediaService from "../../Service/InfoPediaService";
import validateService from "../../Service/ValidateService";
import validateMap from "../../Data/ValidateMap";
import jsonApiService from "../../Service/JsonApiService";


export default class ArticleForm extends Component
{
  constructor(props)
  {
    super(props);
    const article = Object.assign({}, this.props.data);
    this.state =
    {
      article: article,
      subContentMap: this.createSubContentMap(article),
      editSubContentMap: {},
      fields: ["title", "content"],
      successMessages:
      [
        "Your article was created successfully!",
        "Your article was edited successfully!",
        "Your article was deleted successfully!",
        "Your article was created/edited/deleted successfully!"
      ],
      errorMessages:
      [
        "Problems trying to create article!",
        "Problems trying to edit article!",
        "Problems trying to delete article!",
        "Problems trying to create/edit/delete article!"
      ]
    };
  }

  handleSubmit = (event) =>
  {
    event.preventDefault();
    const pa = infoPediaService.getActualPageAction();

    if(pa.itemApiInfo === "deleteArticle" &&
        !window.confirm("Do you really want to delete this article?"))
    {
      return;
    }

    let validation = validateService.testFields(
        this.state.fields, event.target);

    if(validation.valid === true)
    {
      const newArticle = validation.data;
      newArticle.id = this.state.article.id;
      const headers = infoPediaService.createTokenHeaders();
      const extras = {requestExecuted: pa.itemApiInfo};
      infoPediaService.requestData(pa.itemApiInfo, newArticle.id, headers,
          newArticle, this.handleReceiveData, this.handleReceiveDataError, extras);
    } 
    else
      infoPediaService.handleValidationError(this, validation);
	}

	handleReceiveData = (extras) =>
	{
    const aik = extras.requestExecuted;
    const success = this.getMessage(aik, this.state.successMessages);
    const message = infoPediaService.getMessageObject(success, null);
    const pa = infoPediaService.getActualPageAction();
    const pak = pa.postApiInfoPageAction;
    this.setState({message: message});
    
    if(pa.key === "deleteArticle")
    {
      const user = infoPediaService.getUser();
      setTimeout(() => infoPediaService.executePageActionInMainArea(
          pak, user.id), 3000);
    }
    else
    {
      const article = extras.apiResponseData;
      setTimeout(() => infoPediaService.executePageActionInMainArea(
          pak, article.id), 3000);
    }
	}

  handleReceiveDataError = (extras) =>
  {
    const aik = extras.requestExecuted;
    const logMessage = this.getMessage(aik, this.state.errorMessages);
    infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }
  
  getMessage = (apiInfoKey, messages) =>
  {
    if(apiInfoKey === "createArticle")
      return messages[0];
    else if(apiInfoKey === "editArticle")
      return messages[1];
    else if(apiInfoKey === "deleteArticle")
      return messages[2];
    
    return messages[3];
  }

  toggleEditSubContent = (editSubContentKey) =>
  {
    let escm = {};

    if(editSubContentKey)
    {
      if(editSubContentKey !== "newSubContent")
      {
        const sc = this.state.subContentMap[editSubContentKey];
        escm.editSubContent = sc ? sc : null; 
      }
      else
        escm.newSubContent = {articleId: this.state.article.id};
    }     
    
    this.setState({editSubContentMap : escm});
  }

  handleChangeEditSubContent = (event) =>
  {
    const id = event.target.value;
    event.target.selectedIndex = 0;
    this.toggleEditSubContent(id);
  }

  handleChange = (event) =>
  {
    const article = this.state.article;
    article[event.target.name] = event.target.value;
    this.setState({ article: article });
  }

  handleRestore = (event) =>
  {
    this.setState({article: Object.assign({}, this.props.data)});
  }

  isPageAction = (pageActionKey) =>
  {
    const pa = infoPediaService.getActualPageAction();

    if(pa.key === pageActionKey)
      return true;
    
    return false;
  }

  createSubContentMap = (article) =>
  {
    let scm = {};
    let scl = article.subContentList;
    scl = scl ? scl : [];
    scl.forEach((subContent, index) =>
    {
      scm[subContent.id] = subContent;
    });

    return scm;
  }

  componentDidUpdate(prevProps, prevState)
  {
    if(prevProps !== this.props)
    {
      const a = Object.assign({}, this.props.data);
      const scm = this.createSubContentMap(a);
      let escm = {};
      const ns = {article: a, subContentMap: scm, editSubContentMap: escm};
      this.setState(ns)
    }
  }

  render()
  {
    infoPediaService.removeMessage(this, 3000);

    return (
      <ArticleFormComponent
        pageContent={infoPediaService.getActualPageContent()}
        isEditing={this.isPageAction("editArticle")}
        isDeleting={this.isPageAction("deleteArticle")}
        article={this.state.article ? this.state.article : {}}
        editSubContentMap={this.state.editSubContentMap}
        validateMap={validateMap}
        message={this.state.message ? this.state.message : {}}
        toggleEditSubContent={this.toggleEditSubContent}
        handleChange={this.handleChange}
        handleChangeEditSubContent={this.handleChangeEditSubContent}
        handleSubmit={this.handleSubmit}
        handleRestore={this.handleRestore}
      />
    );
  }
}