import React, { Component } from "react";
import SignUpFormComponent from "./SignUpFormComponent";
import infoPediaService from "../../Service/InfoPediaService";
import validateService from "../../Service/ValidateService";
import validateMap from "../../Data/ValidateMap";


export default class UserForm extends Component
{
  constructor(props)
  {
    super(props);
    this.state =
    {
      user: {},
      fields:
      [
        "fullName", "age", "gender", "country",
        "email", "username", "password", "confirmPassword"
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
      const nu = validation.data;
      const headers = infoPediaService.createDefaultHeaders(null, null,
          nu.username.valueOf(), nu.password.valueOf());
      nu.username = undefined;
      nu.password = undefined;
      nu.age = parseInt(nu.age, 10);
      infoPediaService.requestData("createUser", null, headers, nu,
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
    const logMessage = "Problems trying to create user!";
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
      <SignUpFormComponent
        user={this.state.user ? this.state.user : {}}
        validateMap={validateMap}
        message={this.state.message ? this.state.message : {}}
        handleChange={this.handleChange}
        handleSubmit={this.handleSubmit}
      />
    );
  }
}
