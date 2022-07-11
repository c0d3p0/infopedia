import { useSelector } from "react-redux";

import appService from "../../service/AppService";
import NavLink from "../NavLink/NavLink";
import Search from "../Search/Search";
import LoginMenu from "../LoginMenu/LoginMenu";
import UserMenu from "../UserMenu/UserMenu";
import User from "../../model/User";

import logo from "../../images/infopedia_logo.png";

import "./Header.css";


export default function Header() {
  const user = useSelector<any, User>((state) => state.currentUser.value);
  const userLogged = appService.isValidId(user.id);

  return (
    <header className="header">
      <img src={logo} title="Infopedia" />
      <NavLink />
      <Search />
      {userLogged ? <UserMenu /> : <LoginMenu />}

    </header>
  );
}