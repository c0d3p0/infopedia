import React from "react";

import "./UserPasswordForm.css";


export default function UserPasswordFormComponent(props)
{
  const {user, validateMap, message, handleSubmit, handleChange} = props;
  let errorElement, successElement;

  if(message.success)
    successElement = (<div className="SuccessMessage">{message.success}</div>);

  if(message.error)
    errorElement = (<div className="ErrorMessage">{message.error}</div>);

  return (
    <div className="UserPasswordForm RowLinedFlex">
      <h2>Change Password</h2>
      <form
        className="RowLinedFlex"
        onSubmit={handleSubmit}
      >
        <div className="ColLinedFlex">
          <label>Current Password:</label>
          <input
            type="password"
            name="currentPassword"
            value={user.currentPassword ? user.currentPassword : ""}
            placeholder="Current Password"
            required="required"
            pattern={validateMap.currentPassword.pattern}
            title={validateMap.currentPassword.message}
            onChange={handleChange}
            className="TextField"
          />
        </div>
        <div className="ColLinedFlex">
          <label>New Password:</label>
          <input
            type="password"
            name="newPassword"
            value={user.newPassword ? user.newPassword : ""}
            placeholder="New Password"
            required="required"
            pattern={validateMap.newPassword.pattern}
            title={validateMap.newPassword.message}
            onChange={handleChange}
            className="TextField"
          />
        </div>
        <div className="ColLinedFlex">
          <label>Confirm New Password:</label>
          <input
            type="password"
            name="confirmNewPassword"
            value={user.confirmNewPassword ? user.confirmNewPassword : ""}
            placeholder="Confirm New Password"
            required="required"
            pattern={validateMap.confirmNewPassword.pattern}
            title={validateMap.confirmNewPassword.message}
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