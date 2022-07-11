import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import User from "../../model/User";
import ElementData from "../../model/ElementData";
import UserSettingsSideMenuView from "./UserSettingsSideMenuView";


export default function UserSettingsSideMenu() {
  const user = useSelector<any, User>((state) => state.currentUser.value);
  const [fullAccess, setFullAcess] = useState(false);
  const navigate = useNavigate();
  useEffect(() => setFullAcess(user.systemAdmin ?? false), [user]);

  
  const onShowUsersClick = () => {
    navigate("/users");
  }
  
  const onAddUserClick = () => {
    navigate("/add-user");
  }
  
  const onMyProfileClick = () => {
    navigate("/my-profile");
  }

  const onMyArticlesClick = () => {
    navigate("/my-articles");
  }
  
  const onAddArticleClick = () => {
    navigate("/add-article");
  }

  const onRemoveMyAccountClick = () => {
    navigate("/remove-my-account");
  }
  
  const menuItemMap = new Map<string, ElementData>([
    ["my-profile", new ElementData("My Profile", true, onMyProfileClick)],
    ["my-articles", new ElementData("My Articles", true, onMyArticlesClick)],
    ["add-article", new ElementData("Add Article", true, onAddArticleClick)],
    ["users", new ElementData("Show Users", false, onShowUsersClick)],
    ["add-user", new ElementData("Add User", false, onAddUserClick)],
    ["remove-my-account", new ElementData("Remove My Account", true, onRemoveMyAccountClick)]
  ]);


  return (
    <UserSettingsSideMenuView
      fullAccess={fullAccess}
      menuItemMap={menuItemMap}
    />
  );
}


