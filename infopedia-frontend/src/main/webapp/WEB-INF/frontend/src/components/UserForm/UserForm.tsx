import { useEffect, useReducer } from "react";
import { useSelector } from "react-redux";
import { useLocation } from "react-router-dom";

import { genderUtil } from "../../model/Gender";
import appService from "../../service/AppService";
import apiMap from "../../data/ApiMap";
import User from "../../model/User";
import UserFormView from "./UserFormView";


export default function UserForm() {
  const [state, dispatch] = useReducer(reducer, createEmptyState());
  const currentUser = useSelector<any, User>((state: any) => state.currentUser.value);
  const genders = genderUtil.getValues();
  const urlParams = appService.getCurrentURLParameters();
  const location = useLocation();
  useEffect(() => {prepareForm()}, [currentUser, location]);


  const prepareForm = () => {
    const newFormData = formDataMap.get(urlParams[0]) ?? null;
    updateState("formData", newFormData);
    
    if(newFormData?.editing) {
      const apiData = apiMap.get(newFormData.apiKeys[0]);
      const params = newFormData.addParameters ? [urlParams[1]] : [];
      const cred = {username: currentUser.username, password: currentUser.token};
      const requestData = {apiData, params, credentials: cred};
      appService.exchange<User>(requestData).then((response) => {
        if(appService.isValidId(response.data?.id)) {
          const state = {...response.data, formData: newFormData, message: ""};
          updateState("state", state);
        } else {
          const valid = appService.isNumber(response.data?.id);
          const m = valid ? "The system is unavailable!" : "User data not found!";
          throw new Error(m);
        }
      }).catch((error) => {
        const status = error.response?.status;
        const gm = "An error happened processing the request!";
        const re = appService.getRequestErrorMessage(error);
        const cm = status === 500 ? gm : (error.message ?? gm);
        const m = status === 404 ? re : cm;
        console.log(m);
        console.log(error);
        updateState("message", m);
      });
    } else {
      updateState("state", {formData: newFormData, message: ""});
    }
  }

  const onMainFormSaveClick = () => {
    const validation = validateMainFormFields(state);

    if(validation.valid) {
      const apiData = apiMap.get(state.formData.apiKeys[1]);
      const body = createUserFromState(state);
      const cred = createCredentials(state, currentUser, false);
      const params = state.formData.addParameters ? [urlParams[1]] : [];
      const requestData = {apiData, body, credentials: cred, params};
      appService.exchange<User>(requestData).then((response) => {
        if(!state.formData.editing) {
          const newState = {formData: state.formData};
          updateState("state", newState);
        }
        
        const text = state.formData.editing ? "edited" : "added";
        alert(`User ${text} with success!`);
      }).catch((error) => {
        const status = error.response?.status;
        const vre = status === 400 || status === 401 || status === 409;
        const re = appService.getRequestErrorMessage(error);
        const m = vre ? re : "An error happened sending the data to the system!";
        console.log(m);
        console.log(error);
        alert(m);
      });
    } else {
      console.log(validation.error);
      alert(validation.error);
    }
  }

  const onPasswordFormSaveClick = () => {
    const validation = validatePasswordFormFields(state);

    if(validation.valid) {
      const apiData = apiMap.get(state.formData.apiKeys[2]);
      const credentials = createCredentials(state, currentUser, true);
      const params = state.formData.addParameters ? [urlParams[1]] : [];
      const requestData = {apiData, body: {}, credentials, params};
      appService.exchange<User>(requestData).then((response) => {
        alert(`Password changed with success!`);
      }).catch((error) => {
        const status = error.response?.status;
        const vre = status === 400 || status === 401 || status === 409;
        const re = status === 401 ? "Invalid current password!" :
            appService.getRequestErrorMessage(error);
        const m = vre ? re : "An error happened sending the data to the system!";
        console.log(m);
        console.log(error);
        alert(m);
      });
    } else {
      console.log(validation.error);
      alert(validation.error);
    }
  }

  const updateState = (field: string, value: any) => {
    dispatch({type: field, value});
  }


  return (
    <UserFormView
      state={state}
      genders={genders}
      updateState={updateState}
      onMainFormSaveClick={onMainFormSaveClick}
      onPasswordFormSaveClick={onPasswordFormSaveClick}
    />
  );
}


const validateMainFormFields = (state: IState) => {
  if(state.formData.editing)
    return {valid: true, error: ""};

  if(!state.fullName)
    return {valid: false, error: "You need to inform a name!"};

  if(!state.age || isNaN(parseInt(state.age)))
    return {valid: false, error: "You need to inform an age number!"};

  if(parseInt(state.age) < 16)
    return {valid: false, error: "You need to be 16+ years old to register!"};

  if(!genderUtil.isValid(state.gender))
    return {valid: false, error: "You need to select a gender!"};

  if(!state.country)
    return {valid: false, error: "You need to inform a country!"};

  if(!state.email)
    return {valid: false, error: "You need to inform an email!"};

  if(!state.email.match(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/))
    return {valid: false, error: "Invalid email format, it must look like abcde@mnopq.xyz!"};

  if(!state.username)
    return {valid: false, error: "You need to inform a username!"};

  if(!state.password)
    return {valid: false, error: "You need to inform a password!"};

  if(state.password !== state.confirmPassword)
    return {valid: false, error: "Password and confirm password must be the same!"};

  return {valid: true, error: ""};
}

const validatePasswordFormFields = (state: IState) => {
  const userEdit = state.formData.editing && !state.formData.addParameters;

  if(userEdit && !state.currentPassword)
    return {valid: false, error: "You need to inform the current password!"};

  if(!state.newPassword)
    return {valid: false, error: "You need to inform the new password!"};

  if(state.newPassword !== state.confirmNewPassword)
    return {valid: false, error: "New password and confirm new password must be the same!"};

  return {valid: true, error: ""};
}

const createUserFromState = (state: IState) => {
  const {fullName, age, gender, country, email, systemAdmin} = state;
  const a = parseInt(age ?? "-1");
  const g = genderUtil.createFrom(gender);
  const s = systemAdmin ? true : false;
  return new User(undefined, fullName, a, g, country, email, s);
}

const createCredentials = (state: IState,
    currentUser: User, passwordForm: boolean) => {
  const ipuUsername = passwordForm ? "" : state.username;
  const ipuPassword = passwordForm ? state.newPassword : state.password;
  const username = currentUser.username;
  const useToken = state.formData.adminAccess || !passwordForm;
  const password = useToken ? currentUser.token : state.currentPassword;
  return {ipuUsername, ipuPassword, username, password};
}

const reducer = (state: IState, action: any): IState => {
  if(action.type === "state")
    return action.value;
  else
    return {...state, [action.type]: action.value};
};

const createEmptyState = (): IState => {
  const m = "Please wait, the data is being requested!";
  const formData = {
    title: "Loading Data", apiKeys: ["", ""], editing: false,
    addParameters: false, adminAccess: false
  };
  return {formData, message: m};
}

const formDataMap = new Map<string, IFormData>([
  [
    "my-profile",
    {
      title: "My Profile", editing: true,
      addParameters: false, adminAccess: false,
      apiKeys: ["user-my-data", "user-edit-my-data", "user-change-my-password"]
    }
  ],
  [
    "add-user",
    {
      title: "Add User", editing: false, addParameters: false,
      adminAccess: true, apiKeys: ["", "user-add", ""], 
    }
  ],
  [
    "edit-user",
    {
      title: "Edit User", editing: true, addParameters: true,
      adminAccess: true, apiKeys: ["user-find-by-id", "user-edit", "user-edit"],
    }
  ]
]);

interface IState {
  formData: IFormData,
  message: string;

  fullName?: string,
  age?: string,
  gender?: string,
  country?: string,
  email?: string,
  username?: string,
  password?: string,
  confirmPassword?: string,
  systemAdmin?: boolean,

  currentPassword?: string,
  newPassword?: string,
  confirmNewPassword?: string,
}

interface IFormData {
  title: string;
  apiKeys: string[];
  editing: boolean;
  addParameters: boolean;
  adminAccess: boolean;
}


export type { IState, IFormData };