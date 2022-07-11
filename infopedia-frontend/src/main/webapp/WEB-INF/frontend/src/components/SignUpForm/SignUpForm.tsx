import { useReducer } from "react";
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";

import { genderUtil } from "../../model/Gender";
import appService from "../../service/AppService";
import apiMap from "../../data/ApiMap";
import User from "../../model/User";
import SignUpFormView from "./SignUpFormView";


export default function SignUpForm() {
  const [state, dispatch] = useReducer(reducer, {});
  const [cookies, setCookies] = useCookies(["user"]);
  const navigate = useNavigate();
  const genders = genderUtil.getValues();


  const onRegisterClick = () => {
    const validation = validateFields(state);

    if(validation.valid) {
      const apiData = apiMap.get("user-sign-up");
      const body = createUserFromState(state);
      const cred = {ipuUsername: state.username, ipuPassword: state.password};
      const requestData = {apiData, body, credentials: cred};
      appService.exchange<User>(requestData).then((response) => {
        const user = response.data;
        setCookies("user", appService.convertToCookie(user), {path: "/"});
        navigate("/my-articles");
      }).catch((error) => {
        const status = error.response?.status;
        const vre = status === 400 || status === 409;
        const re = appService.getRequestErrorMessage(error);
        const m = vre ? re : "An error happened sending the data to the system!";
        console.log(m);
        console.log(error);
        alert(m);
      })
    } else {
      console.log(validation.error);
      alert(validation.error);
    }
  }

  const updateState = (field: string, value: any) => {
    dispatch({type: field, value});
  }

  const validateFields = (state: IState) => {
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

  const createUserFromState = (state: IState) => {
    const {fullName, age, gender, country, email} = state;
    const a = parseInt(age ?? "-1");
    const g = genderUtil.createFrom(gender);
    return new User(undefined, fullName, a, g, country, email);
  }


  return (
    <SignUpFormView
      state={state}
      genders={genders}
      updateState={updateState}
      onRegisterClick={onRegisterClick}
    />
  );
}


const reducer = (state: IState, action: any): IState => {
  if(action.type === "state")
    return action.value;
  else
    return {...state, [action.type]: action.value};
};

interface IState {
  fullName?: string,
  age?: string,
  gender?: string,
  country?: string,
  email?: string,
  username?: string,
  password?: string,
  confirmPassword?: string
}