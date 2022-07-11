import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { setCurrentUser } from "../../features/CurrentUser";
import apiMap from "../../data/ApiMap";
import ElementData from "../../model/ElementData";
import User from "../../model/User";
import appService from "../../service/AppService";
import UserMenuView from "./UserMenuView";


export default function UserMenu() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector<any, User>((state) => state.currentUser.value);
  const [fullAccess, setFullAcess] = useState(false);
  const [username, setUsername] = useState(user.username ?? "Unknown");
  const [cookies, setCookies, removeCookies] = useCookies(["user"]);
  useEffect(() => {prepareMenu();}, [user]);


  const prepareMenu = () => {
    setFullAcess(user.systemAdmin ?? false);
    setUsername(user.username ?? "Unknown");
  }

  const onMyProfileClick = () => {
    navigate("/my-profile");
  }

  const onMyArticlesClick = () => {
    navigate("/my-articles");
  }

  const onShowUsersClick = () => {
    navigate("/users");
  }

  const onLogoutClick = () => {
    const apiData = apiMap.get("user-logout");
    const cred = {username: user.username, password: user.token};
    const requestData = {apiData, credentials: cred};
    appService.exchange<User>(requestData).then((r) => {}).catch((error) => {
      const m = error.response?.status === 401 ?
          "Login is already expired!" :
          "An error happened sending the data to the system!";
      console.log(m);
      console.log(error);
    });
    removeCookies("user");
    dispatch(setCurrentUser({}));
    navigate("/");
  }

  const menuItemMap = new Map<string, ElementData>([
    ["my-profile", new ElementData("My Profile", true, onMyProfileClick)],
    ["my-articles", new ElementData("My Articles", true, onMyArticlesClick)],
    ["users", new ElementData("Show Users", false, onShowUsersClick)],
    ["logout", new ElementData("Logout", true, onLogoutClick)],
  ]);


  return (
    <UserMenuView
      fullAccess={fullAccess}
      username={username}
      menuItemMap={menuItemMap}
    />
  )
}