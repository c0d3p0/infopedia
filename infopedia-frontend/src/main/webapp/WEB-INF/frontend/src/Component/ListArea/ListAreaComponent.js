import React from "react";
import SearchSidebox from "../SearchSidebox/SearchSidebox";

import "./ListArea.css";


export default function ListAreaComponent(props)
{
  return (
    <div className="ColLinedFlex MidArea">
      <SearchSidebox
        title="See User Articles"
        data={props.leftSideData}
        labelFieldName="username"
        itemPageAction="listArticlesByUserId"
      />
      <props.screenComponents.centerbox data={props.data} />
      <SearchSidebox
        title="See Articles"
        data={props.rightSideData}
        labelFieldName="title"
        itemPageAction="showArticle"
      />
    </div>
  );
}