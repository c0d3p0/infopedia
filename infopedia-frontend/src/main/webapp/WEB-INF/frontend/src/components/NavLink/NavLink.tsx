import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import User from "../../model/User";
import NavLinkView from "./NavLinkView";
import ElementData from "../../model/ElementData";


export default function NavLink() {
  const user = useSelector<any, User>((state) => state.currentUser.value);
  const [fullAccess, setFullAcess] = useState(false);
  const navigate = useNavigate();
  useEffect(() => setFullAcess(user.systemAdmin ?? false), [user]);


  const onUserClick = () => {
    navigate("/users");
  }
  
  const onArticleClick = () => {
    navigate("/articles");
  }
  
  const onAboutClick = () => {
    navigate("/about");
  }
  
  const menuItemMap = new Map<string, ElementData>([
    ["user", new ElementData("User", false, onUserClick)],
    ["article", new ElementData("Article", true, onArticleClick)],
    ["about", new ElementData("About", true, onAboutClick)],
  ]);


  return (
    <NavLinkView
      fullAccess={fullAccess}
      menuItemMap={menuItemMap}
    />
  );
}