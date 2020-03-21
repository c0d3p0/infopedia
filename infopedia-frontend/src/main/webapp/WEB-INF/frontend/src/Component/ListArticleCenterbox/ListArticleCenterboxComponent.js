import React from "react";

import "./ListArticleCenterbox.css";


export default function ListArticleCenterboxComponent(props)
{
	let pageContentElement;
  
  if(props.pageContent)
	{
		const {title, content} = props.pageContent;
		pageContentElement = (
			<div className="ListCenterBoxContent">
				{title ? (<h1>{title}</h1>) : ""}
				{content ? (<p>{content}</p>) : ""}
			</div>
		);
	}

	let itemsElems = props.articles.map((article, index) =>
	{
		return (
			<div
				key={index}
				onClick={props.handleClick}
				value={article.id}
				className="ListCenterboxItem"
			>
				<h2>{article.title}</h2>
				<p>{article.content}</p>
			</div>
		)
	});

	if(itemsElems.length < 1)
	{
		itemsElems = (
			<label className="ListCenterboxMessage">
				No articles found.
			</label>
		);
	}

	return (
		<div className="ListArticleCenterBox Contentbox Centerbox RowLinedFlex">
			{pageContentElement}
			{itemsElems}
		</div>
	);
}
