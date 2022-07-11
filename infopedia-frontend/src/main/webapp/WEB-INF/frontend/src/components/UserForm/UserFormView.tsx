import { IState } from "./UserForm";
import Gender from "../../model/Gender";

import "./UserForm.css";


export default function UserFormView(props: IProps) {
  if(!props.state.message) {
    return (
      <div className="user-form">
        <h1>{props.state.formData.title}</h1>
        <hr />
        <form onSubmit={(e) => {e.preventDefault(); props.onMainFormSaveClick();}}>
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

          {createMainFormUsernameField(props)}
          {createMainFormPasswordFieldElements(props)}
          {createSystemAdminFieldElement(props)}
          <button className="button-light">Save</button>
        </form>
        {createChangePasswordFormElement(props)}
      </div>
    );
  } else {
    return (
      <div className="user-form">
        <h1>{props.state.formData.title}</h1>
        <hr />
        <p>{props.state.message}</p>
      </div>
    );
  }
}


const createChangePasswordFormElement = (props: IProps) => {
  if(props.state.formData.editing) {
    const {editing, adminAccess} = props.state.formData;
    const cpLabel = (<label>Current Password</label>);
    const cpField = (
      <input
        className="textfield-light"
        type="password"
        value={props.state.currentPassword ?? ""}
        onChange={(e) => {props.updateState("currentPassword", e.target.value)}}
        placeholder="Current Password"
      />
    );

    return (
      <div className="change-password-form">
        <h1>Change Password</h1>
        <hr />
        <form onSubmit={(e) => {e.preventDefault(); props.onPasswordFormSaveClick();}}>
          {editing && !adminAccess ? cpLabel : null}
          {editing && !adminAccess ? cpField : null}
          <label>New Password</label>
          <input
            className="textfield-light"
            type="password"
            value={props.state.newPassword ?? ""}
            onChange={(e) => {props.updateState("newPassword", e.target.value)}}
            placeholder="New Password"
          />

          <label>Confirm Password</label>
          <input
            className="textfield-light"
            type="password"
            value={props.state.confirmNewPassword ?? ""}
            onChange={(e) => {props.updateState("confirmNewPassword", e.target.value)}}
            placeholder="New Password"
          />

          <button className="button-light">Save</button>
        </form>
      </div>
    );
  }

  return null;
}

const createMainFormUsernameField = (props: IProps) => {
  return props.state.formData.adminAccess ? (
    <div>
      <label>Username</label>
      <input
        className="textfield-light"
        type="text"
        value={props.state.username ?? ""}
        onChange={(e) => {props.updateState("username", e.target.value)}}
        placeholder="Username"
      />
    </div>
  ) : null;
}

const createMainFormPasswordFieldElements = (props: IProps) => {
  const {editing, adminAccess} = props.state.formData;

  if(adminAccess && !editing) {
    return (
      <div>
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
      </div>
    );
  }

  return null;
}

const createSystemAdminFieldElement = (props: IProps) => {
  if(props.state.formData.adminAccess) {
    return (
      <div>
        <label>System Admin</label>
        <label htmlFor="system-admin-field" className="checkbox-light">
          <input
            id="system-admin-field"
            type="checkbox"
            checked={props.state.systemAdmin ?? false}
            onChange={(e) => {props.updateState("systemAdmin", e.target.checked)}}
          />
        </label>
      </div>
    );
  }

  return null;
}

const createGenderOptions = (genders: Gender[]) => {
  const o: JSX.Element[] = [];
  genders.forEach((g, index) => o.push(
    <option key={index} value={g}>{g.toUpperCase()}</option>)
  );
  return o;
}

interface IProps {
  state: IState;
  genders: Gender[];
  updateState(field: string, value: any): void;
  onMainFormSaveClick(): void;
  onPasswordFormSaveClick(): void;
}