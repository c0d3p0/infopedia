import React from "react";
import ContentSidebox from "../ContentSidebox/ContentSidebox";
import SearchSidebox from "../SearchSidebox/SearchSidebox";
import ArticleCenterBox from "../ArticleCenterbox/ArticleCenterbox";

import "./ArticleArea.css";


export default function ArticleAreaComponent(props)
{
  return (
    <div className="ColLinedFlex MidArea">
      <ContentSidebox article={props.article} />
      <ArticleCenterBox article={props.article} author={props.author}/>
      <SearchSidebox
        title={"See '" + props.author.username + "' Articles"}
        data={props.rightSideData}
        labelFieldName="title"
        itemPageAction="showArticle"
      />
    </div>
  );
}
