import React from "react";

import "./DeleteUserForm.css";


export default function DeleteUserFormComponent(props)
{
  const {user, validateMap, message, handleSubmit, handleChange} = props;
	let errorElement, successElement;

	if(message.success)
		successElement = (<div className="SuccessMessage">{message.success}</div>);

  if(message.error)
    errorElement = (<div className="ErrorMessage">{message.error}</div>);

	return (
		<div className="DeleteUserForm">
			<h2>Delete Account</h2>
			<form className="RowLinedFlex" onSubmit={handleSubmit}>
				<div className="ColLinedFlex">
					<label>Password:</label>
					<input
						type="password"
						name="currentPassword"
						value={user.currentPassword ? user.currentPassword : ""}
						placeholder="Password"
						required="required"
						pattern={validateMap.currentPassword.pattern}
						title={validateMap.currentPassword.message}
						onChange={handleChange}
						className="TextField"
					/>
				</div>
				{errorElement}
				{successElement}
				<button className="ButtonBlack">Confirm</button>
			</form>
		</div>
	);
}
