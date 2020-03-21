import React, {Component} from "react";
import EditAreaComponent from "./EditAreaComponent";
import infoPediaService from "../../Service/InfoPediaService";


export default class EditArea extends Component
{
	constructor(props)
	{
		super(props);
		this.state = {}
	}

  handleReceiveData = (extras) =>
  {
    let newState = {rightSideData: this.getDataArray(extras)};
    this.setState(newState);
  }

  handleReceiveDataError = (extras) =>
  {
    const logMessage = "Problems trying to get the data!";
		infoPediaService.handleReceiveDataError(this, extras, logMessage);
  }

  getDataArray = (extras) =>
  {
    let a = extras.apiResponseData ? extras.apiResponseData : [];

    if(!Array.isArray(a))
      a = Array.isArray(a.list) ? a.list : [a];
      
    return a;
  }

  componentDidMount()
  {
    infoPediaService.requestData("getRandomArticles", 6, null,
        null, this.handleReceiveData, this.handleReceiveDataError);
	}

	render()
	{
    const sc = infoPediaService.getActualScreenComponents();

		return (
			<EditAreaComponent
				centerComponent={sc.centerbox}
				data={this.props.data}
				rightSideData={this.state.rightSideData}
			/>
		);
	}
}