import React from "react";
import LoginForm from "../LoginForm/LoginForm";
import SignUpForm from "../SignUpForm/SignUpForm";

import "./AccountAccess.css";


export default function AccountAccessComponent(props)
{
  return (
    <div className="AccountAccess RowLinedFlex">
      <div className="ColLinedFlex">
        <button name="loginButton">
          Log In
        </button>
        <button name="signUpButton">
          Sign Up
        </button>
      </div>
      <div className="ColLinedFlex">
        <div
          ref={(ref) => {props.setDropdown(ref)}}
          name="loginDropdown"
          className="Dropdown"
        >
          <LoginForm />
        </div>
        <div
          ref={(ref) => {props.setDropdown(ref)}}
          name="signUpDropdown"
          className="Dropdown"
        >
          <SignUpForm />
        </div>
      </div>
    </div>
  );
}