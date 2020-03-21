import React from "react";
import UserPasswordForm from "../UserPasswordForm/UserPasswordForm";

import "./UserForm.css";


export default function UserFormComponent(props)
{
  const {user, validateMap, message,
      handleSubmit, handleRestore, handleChange} = props;
  let errorElement, successElement;

  if(message.success)
    successElement = (<div className="SuccessMessage">{message.success}</div>);

  if(message.error)
    errorElement = (<div className="ErrorMessage">{message.error}</div>);
  
  return (
    <div className="UserForm RowLinedFlex">
      <h2>Change Your Data</h2>
      <form
        className="RowLinedFlex"
        onSubmit={handleSubmit}
      >
        <div className="ColLinedFlex">
          <label>Full Name:</label>
          <input
            type="text"
            name="fullName"
            value={user.fullName ? user.fullName : ""}
            placeholder="Full Name"
            required="required"
            onChange={handleChange}
            className="TextField"
          />
        </div>
        <div className="ColLinedFlex">
          <label>Username:</label>
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
        </div>
        <div className="ColLinedFlex">
          <label>Email:</label>
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
        </div>
        <div className="ColLinedFlex">
          <label>Age:</label>
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
        </div>
        <div className="ColLinedFlex">
          <label>Gender:</label>
          <select
            name="gender"
            value={user.gender ? user.gender : "male"}
            onChange={handleChange}
            className="Dropdownlist"
          >
            <option value="male">male</option>
            <option value="female">female</option>
          </select>
        </div>
        <div className="ColLinedFlex">
          <label>Country:</label>
          <input
            type="text"
            name="country"
            value={user.country ? user.country : ""}
            required="required"
            placeholder="Country"
            onChange={handleChange}
            className="TextField"
          />
        </div>
        {successElement}
        {errorElement}
        <div className="ColLinedFlex">
          <button className="ButtonBlack">Confirm</button>
          <button
            type="button"
            onClick={handleRestore}
            className="ButtonBlack"
          >
            Restore
          </button>
        </div>
      </form>
      <UserPasswordForm data={user}/>
    </div>
  );
}