import React, { Component } from "react";
import LoginFormComponent from "./LoginFormComponent";
import infoPediaService from "../../Service/InfoPediaService";
import validateService from "../../Service/ValidateService";
import validateMap from "../../Data/ValidateMap";


export default class LoginForm extends Component
{
  constructor(props)
  {
    super(props);
    this.state =
    {
      user: {},
      fields: ["loginUser", "password"]
    };
  }

  handleSubmit = (event) =>
  {
    event.preventDefault();
    let validation = validateService.testFields(
        this.state.fields, event.target);
    
    if(validation.valid === true)
    {
      const headers = infoPediaService.createDefaultHeaders(
          validation.data.loginUser, validation.data.password);
      infoPediaService.requestData("login", null, headers, null,
          this.handleReceiveData, this.handleReceiveDataError);
    }
    else
      infoPediaService.handleValidationError(this, validation);
  }

  handleReceiveData = (extras) =>
  {
    this.setState({user: {}});
    infoPediaService.setUser(extras.apiResponseData);
  }

  handleReceiveDataError = (extras) =>
  {
    const logMessage = "Problems trying to login!";
    infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }

  handleChange = (event) =>
  {
    const user = this.state.user;
    user[event.target.name] = event.target.value;
    this.setState({user: user});
  }

  render()
  {
    infoPediaService.removeMessage(this, 3000);

    return (
      <LoginFormComponent
        user={this.state.user ? this.state.user : {}}
        validateMap={validateMap}
        message={this.state.message ? this.state.message : {}}
        handleChange={this.handleChange}
        handleSubmit={this.handleSubmit}
      />
    );
  }
}
