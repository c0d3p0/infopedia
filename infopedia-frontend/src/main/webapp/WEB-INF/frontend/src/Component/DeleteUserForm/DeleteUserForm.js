import React, { Component } from "react";
import DeleteUserFormComponent from "./DeleteUserFormComponent";
import infoPediaService from "../../Service/InfoPediaService";
import validateService from "../../Service/ValidateService";
import validateMap from "../../Data/ValidateMap";


export default class DeleteUserForm extends Component
{
	constructor(props)
	{
		super(props);
		this.state = {user: {}};
	}

	handleSubmit = (event) =>
	{
		event.preventDefault();

		if(window.confirm("Do you really want to delete your account?"))
		{
			let validation = validateService.testField("currentPassword",
					event.target);
			
			if(validation.valid === true)
			{
				const user = infoPediaService.getUser();
				const headers = infoPediaService.createDefaultHeaders(
						user.username, validation.data);
				infoPediaService.requestData("deleteUser", user.id, headers,
						null, this.handleReceiveData, this.handleReceiveDataError);
			}
			else
				infoPediaService.handleValidationError(this, validation);
		}
	}

	handleReceiveData = (extras) =>
	{
		const success = "Your account was deleted successfully!";
    const message = infoPediaService.getMessageObject(success, null);
		this.setState({message: message});
		setTimeout(() => infoPediaService.setUser(null), 3000);
	}

  handleReceiveDataError = (extras) =>
  {
		const logMessage = "Problems trying to delete the user!";
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
			<DeleteUserFormComponent
				user={this.state.user ? this.state.user : {}}
				validateMap={validateMap}
				message={this.state.message ? this.state.message : {}}
				handleChange={this.handleChange}
				handleSubmit={this.handleSubmit}
			/>
		);
	}
}
