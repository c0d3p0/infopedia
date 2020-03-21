import React from "react";

import "./EditCenterbox.css";


export default function EditCenterboxComponent(props)
{
	return (
		<div className="Contentbox Centerbox RowLinedFlex">
			<props.component
        data={props.data}
      />
		</div>
	)
}