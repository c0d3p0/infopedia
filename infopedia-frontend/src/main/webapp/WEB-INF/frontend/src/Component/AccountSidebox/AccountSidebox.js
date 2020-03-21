import React, {Component} from "react";
import AccountSideboxComponent from "./AccountSideboxComponent";
import infoPediaService from "../../Service/InfoPediaService";
import accountSectionMap from "../../Data/AccountSectionMap";


export default class AccountSidebox extends Component
{
	handleClick = (event) =>
	{
		const key = event.currentTarget.getAttribute("value");
		const as = accountSectionMap[key];
		const u = infoPediaService.getUser();

		if(key === "yourData")
		{
			const h = infoPediaService.createTokenHeaders();
      infoPediaService.executePageActionInMainArea(as.pageAction, u.id, h);
		}
		else
			infoPediaService.executePageActionInMainArea(as.pageAction, u.id);
	}

	render()
	{
		return (
			<AccountSideboxComponent
				title="Account Settings"
				accountSectionMap={accountSectionMap}
				handleClick={this.handleClick}
			/>
		);
	}
}