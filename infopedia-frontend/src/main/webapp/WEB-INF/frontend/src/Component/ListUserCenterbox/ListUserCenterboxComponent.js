import React from "react";

import "./ListUserCenterbox.css";


export default function ListUserCenterboxComponent(props)
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

	let itemsElems = props.users.map((user, index) =>
	{
		return (
			<div
				key={index}
				onClick={props.handleClick}
				value={user.id}
				className="ListCenterboxItem"
			>
				<h2>{user.username}</h2>
        <p>{user.age} years old, {user.gender}, {user.country}</p>
			</div>
		)
	});

	if(itemsElems.length < 1)
	{
		itemsElems = (
			<label className="ListCenterboxMessage">
				No users found.
			</label>
		);
	}

	return (
		<div className="ListUserCenterBox Contentbox Centerbox RowLinedFlex">
			{pageContentElement}
			{itemsElems}
		</div>
	);
}