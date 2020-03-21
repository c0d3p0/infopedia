import React, { Component } from "react";
import UserPasswordFormComponent from "./UserPasswordFormComponent";
import infoPediaService from "../../Service/InfoPediaService";
import validateService from "../../Service/ValidateService";
import validateMap from "../../Data/ValidateMap";


export default class UserPasswordForm extends Component
{
  constructor(props)
  {
    super(props);
    this.state =
    {
      user: {id: this.props.data.id},
      fields: ["currentPassword", "newPassword", "confirmNewPassword"]
    };
  }

  handleSubmit = (event) =>
  {
    event.preventDefault();
    let validation = validateService.testFields(
      this.state.fields, event.target);
  
    if(validation.valid === true)
    {
      const d = validation.data;
      const u = infoPediaService.getUser();
      const headers = infoPediaService.createDefaultHeaders(
          u.username, d.currentPassword, u.username, d.newPassword);
      infoPediaService.requestData("changeUserPassword", this.state.user.id,
          headers, null, this.handleReceiveData, this.handleReceiveDataError);
    }
    else
      infoPediaService.handleValidationError(this, validation);
  }

  handleReceiveData = (extras) =>
  {
		const user = {};
		user.id = this.props.data.id;
    const success = "Your password was changed successfully!"
    const message = infoPediaService.getMessageObject(success, null);
    this.setState({user: user, message: message});
  }

  handleReceiveDataError = (extras) =>
  {
    const logMessage = "Problems trying to change user password!";
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
      <UserPasswordFormComponent
				user={this.state.user ? this.state.user : {}}
        validateMap={validateMap}
        message={this.state.message ? this.state.message : {}}
        handleChange={this.handleChange}
        handleSubmit={this.handleSubmit}
      />
    );
  }
}
