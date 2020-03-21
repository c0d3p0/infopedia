import React from "react";
import AccountSidebox from "../AccountSidebox/AccountSidebox"
import SearchSidebox from "../SearchSidebox/SearchSidebox";

import "./EditArea.css"


export default function EditAreaComponent(props)
{
	return (
		<div className="ColLinedFlex MidArea">
			<AccountSidebox />
			<props.centerComponent data={props.data}/>
			<SearchSidebox
        title="See Articles"
				data={props.rightSideData}
				labelFieldName="title"
				itemPageAction="showArticle"
      />
		</div>
	);
}