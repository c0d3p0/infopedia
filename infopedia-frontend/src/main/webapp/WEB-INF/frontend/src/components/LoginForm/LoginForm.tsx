import { useState } from "react";
import { useCookies } from "react-cookie";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

import { setCurrentUser } from "../../features/CurrentUser";
import apiMap from "../../data/ApiMap";
import User from "../../model/User";
import appService from "../../service/AppService";
import LoginFormView from "./LoginFormView";


export default function LoginForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [cookies, setCookies] = useCookies(["user"]);
  const navigate = useNavigate();
  const dispatch = useDispatch();


  const onLoginClick = () => {
    const apiData = apiMap.get("user-login");
    const requestData = {apiData, credentials: {username, password}};
    appService.exchange<User>(requestData).then((response) => {
      const user = response.data;
      
      if(user?.id && user.id > -1) {
        dispatch(setCurrentUser({...user}));
        setCookies("user", appService.convertToCookie(user), {path: "/"});
        navigate("/my-articles");
      } else {
        const unavailable = user?.id && user.id < 0;
        const message = unavailable ?
            "System is unavailable now, try again later!" : 
            "Invalid username / email or password!";
        throw new Error(message);
      }
    }).catch((error) => {
      const status = error.response?.status;
      const e401 = status === 401;
      const em = status === 400 ? appService.getRequestErrorMessage(error) : 
          "An error happened processing the request!";
      const message = e401 ? "Invalid username / email or password!" : em;
      console.log(message);
      console.log(error);
      alert(message);
    });
  }


  return (
    <LoginFormView
      username={username}
      password={password}
      setUsername={setUsername}
      setPassword={setPassword}
      onLoginClick={onLoginClick}
    />
  );
}