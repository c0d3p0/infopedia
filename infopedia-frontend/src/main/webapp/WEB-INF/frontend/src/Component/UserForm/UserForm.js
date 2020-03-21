import React, { Component } from "react";
import UserFormComponent from "./UserFormComponent";
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
      updatedUser: Object.assign({}, this.props.data),
      fields:
      [
        "fullName", "username", "email", "age", "gender",
        "country"
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
      const editUser = validation.data;
      editUser.id = this.state.updatedUser.id;
      editUser.age = parseInt(validation.data.age, 10);
      const headers = infoPediaService.createTokenHeaders();
      infoPediaService.requestData("editUser", editUser.id, headers,
          editUser, this.handleReceiveData, this.handleReceiveDataError);
    }
    else
      infoPediaService.handleValidationError(this, validation);
  }

  handleReceiveData = (extras) =>
  {
    const success = "Your data was changed successfully!"
    const newState = {updatedUser: extras.apiResponseData};
    newState.message = infoPediaService.getMessageObject(success, null);
    this.setState(newState);

    const user = infoPediaService.getUser();
    user.id = newState.updatedUser.id.valueOf();
    user.username = newState.updatedUser.username.valueOf();
    user.age = newState.updatedUser.age.valueOf();
    user.gender = newState.updatedUser.gender.valueOf();
    user.country = newState.updatedUser.country.valueOf();

    infoPediaService.setUser(user);
  }

  handleReceiveDataError = (extras) =>
  {
    const logMessage = "Problems trying to edit user!";
    infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }

  handleChange = (event) =>
  {
    const user = this.state.updatedUser;
    user[event.target.name] = event.target.value;
    this.setState({updatedUser: user});
  }

  handleRestore = (event) =>
  {
    this.setState({updatedUser: Object.assign({}, this.props.data)});
  }
  
  render()
  {
    infoPediaService.removeMessage(this, 3000);

    return (
      <UserFormComponent
        user={this.state.updatedUser ? this.state.updatedUser : {}}
        validateMap={validateMap}
        message={this.state.message ? this.state.message : {}}
        handleChange={this.handleChange}
        handleSubmit={this.handleSubmit}
        handleRestore={this.handleRestore}
      />
    );
  }
}
