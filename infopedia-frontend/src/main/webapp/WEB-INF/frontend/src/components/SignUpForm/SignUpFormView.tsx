import Gender from "../../model/Gender";

import icon from "../../images/infopedia_icon.png";

import "./SignUpForm.css";


export default function SignUpFormView(props: IProps) {
  return (
    <div className="sign-up-form">
      <div className="box-dark">
        <img src={icon} title="Infopedia" />
        <h3>Sign Up to Infopedia</h3>
      </div>
      <div className="box-dark">
        <form onSubmit={(e) => {e.preventDefault(); props.onRegisterClick()}}>
          <label>Full Name</label>
          <input
            className="textfield-light"
            type="text"
            value={props.state.fullName ?? ""}
            onChange={(e) => {props.updateState("fullName", e.target.value)}}
            placeholder="Full Name"
          />

          <label>Age</label>
          <input
            className="textfield-light"
            type="text"
            value={props.state.age ?? ""}
            onChange={(e) => {props.updateState("age", e.target.value.replace(/\D/g, ""))}}
            placeholder="Age"
          />

          <label>Gender</label>
          <select
            className="combobox-light"
            value={props.state.gender ?? ""}
            onChange={(e) => {props.updateState("gender", e.target.value)}}
          >
            <option value="">Select a gender</option>
            {createGenderOptions(props.genders)}
          </select>
          <label>Country</label>
          <input
            className="textfield-light"
            type="text"
            value={props.state.country ?? ""}
            onChange={(e) => {props.updateState("country", e.target.value)}}
            placeholder="Country"
          />

          <label>Email</label>
          <input
            className="textfield-light"
            type="text"
            value={props.state.email ?? ""}
            onChange={(e) => {props.updateState("email", e.target.value)}}
            placeholder="Email"
          />

          <label>Username</label>
          <input
            className="textfield-light"
            type="text"
            value={props.state.username ?? ""}
            onChange={(e) => {props.updateState("username", e.target.value)}}
            placeholder="Username"
          />

          <label>Password</label>
          <input
            className="textfield-light"
            type="password"
            value={props.state.password ?? ""}
            onChange={(e) => {props.updateState("password", e.target.value)}}
            placeholder="Password"
          />

          <label>Confirm Password</label>
          <input
            className="textfield-light"
            type="password"
            value={props.state.confirmPassword ?? ""}
            onChange={(e) => {props.updateState("confirmPassword", e.target.value)}}
            placeholder="Password"
          />

          <button className="button-light">Register</button>
        </form>
      </div>
    </div>
  );
}


const createGenderOptions = (genders: Gender[]) => {
  const o: JSX.Element[] = [];
  genders.forEach((g, index) => o.push(
    <option key={index} value={g}>{g.toUpperCase()}</option>)
  );
  return o;
}

interface IProps {
  state: any;
  genders: Gender[];
  updateState(field: string, value: any): void;
  onRegisterClick(): void;
}