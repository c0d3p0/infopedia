import React from "react";

import "./LoginForm.css";


export default function LoginFormComponent(props)
{
  const {user, validateMap, message, handleSubmit, handleChange} = props;
  let errorElement;

  if(message.error)
    errorElement = (<div className="ErrorMessage">{message.error}</div>);

  return (
    <div className="LoginForm">
      {errorElement}
      <form
        className="RowLinedFlex"
        onSubmit={handleSubmit}
      >
        <input
          type="text"
          name="loginUser"
          value={user.loginUser ? user.loginUser : ""}
          placeholder="Username or email"
          required="required"
          onChange={handleChange}
          className="TextField"
        />
        <input
          type="password"
          name="password"
          value={user.password ? user.password : ""}
          placeholder="Password"
          required="required"
          pattern={validateMap.password.pattern}
          title={validateMap.password.message}
          onChange={handleChange}
          className="TextField"
        />
        <button className="ButtonBlack">Login</button>
      </form>
    </div>
  );
}