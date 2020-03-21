import React, { Component } from "react";
import SubContentFormComponent from "./SubContentFormComponent";
import infoPediaService from "../../Service/InfoPediaService";
import validateService from "../../Service/ValidateService";
import validateMap from "../../Data/ValidateMap";


export default class SubContentForm extends Component
{
  constructor(props)
  {
    super(props);
    this.state =
    {
      subContent: Object.assign({}, this.props.data),
      fields: ["position", "type", "title", "content"],
      successMessages:
      [
        "Your sub content was created successfully!",
        "Your sub content was edited successfully!",
        "Your sub content was deleted successfully!",
        "Your sub content was created/edited/deleted successfully!"
      ],
      errorMessages:
      [
        "Problems trying to create sub content!",
        "Problems trying to edit sub content!",
        "Problems trying to delete sub content!",
        "Problems trying to create/edit/delete sub content!",
      ]
    };
  }

  handleSubmit = (event) =>
  {
    event.preventDefault();
    let validation = validateService.testFields(
        this.state.fields, event.target);

    if(validation.valid === true)
    {
      const newsc = validation.data;
      newsc.position = parseInt(newsc.position, "10");
      const aik = this.props.isEditing ? "editSubContent" : "createSubContent";
      const id = this.props.isEditing ? this.state.subContent.id : null;
      newsc.articleId = !this.props.isEditing ?
          this.state.subContent.articleId : null;
      const headers = infoPediaService.createTokenHeaders();
      const extras = {requestExecuted: aik};
      infoPediaService.requestData(aik, id, headers, newsc, 
          this.handleReceiveData, this.handleReceiveDataError, extras);
    }
    else
      infoPediaService.handleValidationError(this, validation);
  }

  handleRemove = (event) =>
  {
    if(window.confirm("Do you really want to remove this sub content?"))
    {
      const sc = this.state.subContent;
      const headers = infoPediaService.createTokenHeaders();
      const extras = {requestExecuted: "deleteSubContent"};
      infoPediaService.requestData("deleteSubContent", sc.id, headers, sc,
          this.handleReceiveData, this.handleReceiveDataError, extras);
    }
  }

  handleReceiveData = (extras) =>
  {
    const aik = extras.requestExecuted;
    const success = this.getMessage(aik, this.state.successMessages);
    const message = infoPediaService.getMessageObject(success, null);
    this.setState({message: message});
    setTimeout(() => infoPediaService.executePageActionInMainArea(
        "editArticle", this.state.subContent.articleId), 3000);
  }

  handleReceiveDataError = (extras) =>
  {
    const aik = extras.requestExecuted;
    const logMessage = this.getMessage(aik, this.state.errorMessages);
    infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }

  getMessage = (apiInfoKey, messages) =>
  {
    if(apiInfoKey === "createSubContent")
      return messages[0];
    else if(apiInfoKey === "editSubContent")
      return messages[1];
    else if(apiInfoKey === "deleteSubContent")
      return messages[2];
    
    return messages[3];
  }

  handleHide = (event) =>
  {
    this.props.toggleEditSubContent();
  }

  handleChange = (event) =>
  {
    const subContent = this.state.subContent;
    subContent[event.target.name] = event.target.value;
    this.setState({subContent: subContent});
  }

  handleRestore = (event) =>
  {
    this.setState({subContent: Object.assign({}, this.props.data)});
  }

  componentDidUpdate(prevProps, prevState)
  {
    if(prevProps !== this.props)
      this.setState({subContent: Object.assign({}, this.props.data)});
  }

  render()
  {
    infoPediaService.removeMessage(this, 3000);

    return (
      <SubContentFormComponent
        pageContent={this.props.pageContent}
        isEditing={this.props.isEditing}
        subContent={this.state.subContent ? this.state.subContent : {}}
        validateMap={validateMap}
        message={this.state.message ? this.state.message : {}}
        handleChange={this.handleChange}
        handleSubmit={this.handleSubmit}
        handleRemove={this.handleRemove}
        handleHide={this.handleHide}
        handleRestore={this.handleRestore}
      />
    );
  }
}