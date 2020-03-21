import React from "react";

import "./ArticleCenterbox.css";


export default function ArticleCenterboxComponent(props)
{
  const contentElems = props.subContents.map((sc, index) =>
  {
    const type = sc.type ? sc.type.toLowerCase() : "";

    if(type === "image-link")
      return getImagesContentElement(index + 1, sc.title, sc.content);
    else if(type === "link")
      return getLinksContentElement(index + 1, sc.title, sc.content);

    return getTextContentElement(index + 1, sc.title, sc.content);
  });
  contentElems.unshift(getTextContentElement(0,
      props.article.title, props.article.content));

  if(props.author && props.author.username)
    contentElems.push(getTextContentElement(0, "Author", props.author.username));

  function getTextContentElement(key, title, content)
  {
    const contents = content.split("\n");
    const ce = contents.map((content, index) =>
    {
      let c = content.trim();
      return (<p key={index}>{c = c ? c : "\u00a0"}</p>);
    });
    return getContentElement(key, title, ce);
  }

  function getLinksContentElement(key, title, content)
  {
    const links = props.getFixedLinks(content);
    const ce = links.map((link, index) =>
    {
      const l = link.trim();
      return (<a key={index} href={l} target="_blank">{l}</a>);
    });
    return getContentElement(key, title, ce);
  }

  function getImagesContentElement(key, title, content)
  {
    const links = props.getFixedLinks(content);
    let imageElements = [];

    for(let i = 0; i < links.length; i += 3)
    {
      const l1 = links[i];
      const l2 = links[i + 1];
      const l3 = links[i + 2];
      imageElements.push((
        <div className="ColLinedFlex">
          {l1 ? (<img key={i} src={l1.trim()} alt="" />) : ""}
          {l2 ? (<img key={i + 1} src={l2.trim()} alt="" />) : ""}
          {l3 ? (<img key={i + 2} src={l3.trim()} alt="" />) : ""}
        </div>
      ));
    }
    const ce = (<div className="RowLinedFlex">{imageElements}</div>)
    return getContentElement(key, title, ce);
  }

  function getContentElement(key, title, contentElement)
  {
    const nt = title ? title : "No article";

    return (
      <div key={key} className="RowLinedFlex">
        <h2 id={nt}>{nt}</h2>
        {contentElement}
      </div>
    );
  }

  return (
    <div className="ArticleCenterbox Contentbox Centerbox RowLinedFlex">
      {contentElems}
    </div>
  );
}


