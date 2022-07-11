import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

import appService from "../../service/AppService";
import ArticleSideMenu from "../ArticleSideMenu/ArticleSideMenu";
import UserSettingsSideMenu from "../UserSettingsSideMenu/UserSettingsSideMenu";
import UserSideMenu from "../UserSideMenu/UserSideMenu";

import "./SideMenu.css";


export default function SideMenu() {
  const location = useLocation();
  const [componentName, setComponentName] = useState("");
  useEffect(() => {updateMenu()}, [location]);


  const updateMenu = () => {
    const urlParams = appService.getCurrentURLParameters();
    const key = urlParams[0] ?? "invalid_key";
    setComponentName(indexMap.get(key) ?? "");
  }


  return (
    <div className="side-menu">
      {createMenuElement(componentName)}
    </div>
  );
}


const createMenuElement = (name: string) => {
  if(name === "UserSideMenu")
    return (<UserSideMenu />);

  if(name === "ArticleSideMenu")
    return (<ArticleSideMenu />);

  if(name === "UserSettingsSideMenu")
    return (<UserSettingsSideMenu />);

  return null;
}

const indexMap = new Map<string, string>([
  ["", ""],
  ["index", ""],
  ["about", ""],
  ["articles", "UserSideMenu"],
  ["article-page", "ArticleSideMenu"],
  ["login-form", ""],
  ["sign-up-form", ""],

  ["my-profile", "UserSettingsSideMenu"],
  ["my-articles", "UserSettingsSideMenu"],
  ["my-article", "UserSettingsSideMenu"],
  ["my-article-sub-content", "UserSettingsSideMenu"],
  ["add-article", "UserSettingsSideMenu"],
  ["remove-my-account", "UserSettingsSideMenu"],

  ["users", "UserSettingsSideMenu"],
  ["add-user", "UserSettingsSideMenu"],
  ["edit-user", "UserSettingsSideMenu"],
  ["edit-article", "UserSettingsSideMenu"],
  ["edit-article-sub-content", "UserSettingsSideMenu"]
]);