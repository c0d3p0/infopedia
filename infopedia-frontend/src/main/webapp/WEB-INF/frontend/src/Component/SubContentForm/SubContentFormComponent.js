import React from "react";

import "./SubContentForm.css";


export default function SubContentFormComponent(props)
{
  const {pageContent, isEditing, subContent, validateMap} = props;
  const {message, handleChange, handleSubmit, handleRemove} = props;
  const {handleHide, handleRestore} = props;
  let removeElement, restoreElement, errorElement, successElement;
  const contentElement = pageContent.content ?
      (<p>{pageContent.content}</p>) : "";

  if(isEditing)
  {
    restoreElement = (
      <button
        type="button"
        onClick={handleRestore}
        className="ButtonBlack"
      >
        Restore
      </button>
    );
    removeElement = (
      <button
        type="button"
        onClick={handleRemove}
        className="ButtonBlack"
      >
        Remove
      </button>
    );
  }

  if(message.success)
    successElement = (<div className="SuccessMessage">{message.success}</div>);

  if(message.error)
    errorElement = (<div className="ErrorMessage">{message.error}</div>);

  return (
    <div className="SubContentForm RowLinedFlex">
      <h2>{pageContent.title}</h2>
      {contentElement}
      <form className="RowLinedFlex" onSubmit={handleSubmit}>
        <div className="ColLinedFlex">
          <label>Position:</label>
          <input
            type="text"
            name="position"
            value={subContent.position ? subContent.position : ""}
            placeholder="Position"
            required="required"
            pattern={validateMap.position.pattern}
            title={validateMap.position.message}
            onChange={handleChange}
            className="TextField"
          />
        </div>
        <div className="ColLinedFlex">
          <label>Type:</label>
          <select
            name="type"
            value={subContent.type ? subContent.type : "text"}
            onChange={handleChange}
            className="Dropdownlist"
          >
            <option value="text">text</option>
            <option value="image-link">image-link</option>
            <option value="link">link</option>
          </select>
        </div>
        <div className="ColLinedFlex">
          <label>Title:</label>
          <input
            type="text"
            name="title"
            value={subContent.title ? subContent.title : ""}
            placeholder="Title"
            required="required"
            pattern={validateMap.title.pattern}
            title={validateMap.title.message}
            onChange={handleChange}
            className="TextField"
          />
        </div>
        <div className="ColLinedFlex">
          <label>Content:</label>
          <textarea
            name="content"
            value={subContent.content ? subContent.content : ""}
            placeholder="Content"
            required="required"
            pattern={validateMap.content.pattern}
            title={validateMap.content.message}
            onChange={handleChange}
            className="TextArea"
          />
        </div>
        {errorElement}
        {successElement}
        <div className="SubContentFormButtonArea ColLinedFlex">
          {removeElement}
          <button className="ButtonBlack ConfirmButton">Confirm</button>
          {restoreElement}
          <button
            type="button"
            onClick={handleHide}
            className="ButtonBlack"
          >
            Hide
          </button>
        </div>
      </form>
    </div>
  );
}
