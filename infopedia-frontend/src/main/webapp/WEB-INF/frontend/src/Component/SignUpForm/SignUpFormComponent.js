import React from "react";

import "./SignUpForm.css";


export default function SignUpFormComponent(props)
{
  const {user, validateMap, message, handleSubmit, handleChange} = props;
  let errorElement;

  if(message.error)
    errorElement = (<div className="ErrorMessage">{message.error}</div>);

  return (
    <div className="SignUpForm">
      {errorElement}
      <form
        className="RowLinedFlex"
        onSubmit={handleSubmit}
      >
        <input
          type="text"
          name="fullName"
          placeholder="Full Name"
          value={user.fullName ? user.fullName : ""}
          required="required"
          onChange={handleChange}
          className="TextField"
        />
        <input
          type="text"
          name="username"
          value={user.username ? user.username : ""}
          placeholder="Username"
          required="required"
          pattern={validateMap.username.pattern}
          title={validateMap.username.message}
          onChange={handleChange}
          className="TextField"
        />
        <input
          type="text"
          name="email"
          value={user.email ? user.email : ""}
          placeholder="Email"
          required="required"
          pattern={validateMap.email.pattern}
          title={validateMap.email.message}
          onChange={handleChange}
          className="TextField"
        />
        <input
          type="text"
          name="age"
          value={user.age ? user.age : ""}
          placeholder="Age"
          required="required"
          pattern={validateMap.age.pattern}
          title={validateMap.age.message}
          onChange={handleChange}
          className="TextField"
        />
        <select
          name="gender"
          value={user.gender ? user.gender : "male"}
          onChange={handleChange}
          className="Dropdownlist"
        >
          <option value="male">male</option>
          <option value="female">female</option>
        </select>
        <input
          type="text"
          name="country"
          value={user.country ? user.country : ""}
          placeholder="Country"
          required="required"
          onChange={handleChange}
          className="TextField"
        />
        <div className="RowLinedFlex">
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
          <input
            type="password"
            name="confirmPassword"
            value={user.confirmPassword ? user.confirmPassword : ""}
            placeholder="Confirm password"
            required="required"
            onChange={handleChange}
            className="TextField"
          />
        </div>
        <button className="ButtonBlack">Sign Up</button>
      </form>
    </div>
  );
}