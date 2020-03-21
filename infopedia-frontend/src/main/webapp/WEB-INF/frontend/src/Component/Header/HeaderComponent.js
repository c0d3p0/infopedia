import React from "react";
import Searchbar from "../Searchbar/Searchbar";

import "./Header.css";


export default function HeaderComponent(props)
{
  return (
    <header className="Header ColLinedFlex">
      <div className="ColLinedFlex">
        <div className="HeaderApp ColLinedFlex">
          <img src={props.logoImage} alt="logo" />
          <h1>InfoPedia</h1>
        </div>
        <Searchbar />
        <props.accountComponent user={props.user} />
      </div>
    </header>
  );
}
