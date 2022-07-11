import { useState } from "react";
import { useCookies } from "react-cookie";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { setCurrentUser } from "../../features/CurrentUser";
import apiMap from "../../data/ApiMap";
import User from "../../model/User";
import appService from "../../service/AppService";
import RemoveAccountFormView from "./RemoveAccountFormView";


export default function RemoveAccountForm() {
  const [password, setPassword] = useState("");
  const [cookies, setCookies, removeCookies] = useCookies(["user"]);
  const currentUser = useSelector<any, User>((state: any) => state.currentUser.value);
  const navigate = useNavigate();
  const dispatch = useDispatch();


  const onRemoveAccountClick = () => {
    const validation = validateFields(password);

    if(validation.valid) {
      if(!confirm("Are you sure you want to remove your account?"))
        return;

      const apiData = apiMap.get("user-remove-my-acount");
      const cred = {username: currentUser.username, password: password};
      const requestData = {apiData, credentials: cred};
      
      appService.exchange<User>(requestData).then((response) => {
        removeCookies("user");
        dispatch(setCurrentUser({}));
        navigate("/");
      }).catch((error) => {
        const m = error.response?.status === 401 ? "Invalid password!" :
            "An error happened sending the data to the system!";
        console.log(m);
        console.log(error);
        alert(m);
      });
    } else {
      console.log(validation.error);
      alert(validation.error);
    }
  }


  return (
    <RemoveAccountFormView
      password={password}
      setPassword={setPassword}
      onRemoveAccountClick={onRemoveAccountClick}
    />
  );
}


const validateFields = (password: string) => {
  if(!password)
    return {valid: false, error: "You need to inform the password!"};

  return {valid: true, error: ""};
}