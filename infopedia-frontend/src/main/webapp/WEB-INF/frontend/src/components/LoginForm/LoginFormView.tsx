import { Link } from "react-router-dom";

import icon from "../../images/infopedia_icon.png";

import "./LoginForm.css";


export default function LoginFormView(props: IProps) {
  return (
    <div className="login-form">
      <div className="box-dark">
        <img src={icon} title="Infopedia" />
        <h3>Log into Infopedia</h3>
      </div>
      <div className="box-dark">
        <form onSubmit={(e) => {e.preventDefault(); props.onLoginClick()}}>
          <label>Username or email</label>
          <input
            className="textfield-light"
            type="text"
            value={props.username ?? ""}
            onChange={(e) => {props.setUsername(e.target.value)}}
          />
          <label>Password</label>
          <input
            className="textfield-light"
            type="password"
            value={props.password ?? ""}
            onChange={(e) => {props.setPassword(e.target.value)}}
          />
          <button className="button-light">Login</button>
        </form>
      </div>
      <div className="box-dark">
        <div>
          <span>Don't have an account?&nbsp;</span>
          <Link className="link" to="/sign-up-form">Create an account</Link>
        </div>
      </div>
    </div>
  );
}


interface IProps {
  username: string;
  password: string;
  setUsername(username: string): void;
  setPassword(password: string): void;
  onLoginClick(): void;
}