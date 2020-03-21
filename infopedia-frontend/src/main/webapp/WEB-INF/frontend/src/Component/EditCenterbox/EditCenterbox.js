import React, {Component} from "react";
import EditCenterboxComponent from "./EditCenterboxComponent";
import infoPediaService from "../../Service/InfoPediaService";


export default class EditCenterbox extends Component
{
	render()
	{
    const sc = infoPediaService.getActualScreenComponents();

		return (
			<EditCenterboxComponent
        component={sc.centerboxChild}
        data={this.props.data}
      />
		);
	}
}

