import React from "react";
import SubContentForm from "../SubContentForm/SubContentForm";
import pageContentMap from "../../Data/PageContentMap"

import "./ArticleForm.css";


export default function ArticleFormComponent(props)
{
  const {pageContent, isEditing, isDeleting, article, message} = props;
  const {validateMap, editSubContentMap, toggleEditSubContent} = props;
  const {handleChange, handleChangeEditSubContent} = props;
  const {handleSubmit, handleRestore} = props;
  let contentElement, restoreElement, errorElement, successElement;

  if(pageContent.content)
    contentElement = (<p>{pageContent.content}</p>);
  
  let scl = article.subContentList;
  scl = scl ? scl : [];
  const editSubContentElements = scl.map((subContent, index) =>
  {
    return (
      <option key={index} value={subContent.id}>
        {subContent.title}
      </option>
    );
  });

  let esck = Object.keys(editSubContentMap);
  const subContentFormElements = esck.map((field, index) =>
  {
    const isEditing = field === "editSubContent";
    const pc = isEditing ? pageContentMap.editSubContent :
        pageContentMap.newSubContent;

    return (
      <SubContentForm
        pageContent={pc}
        isEditing={isEditing}
        data={editSubContentMap[field]}
        toggleEditSubContent={toggleEditSubContent}
      />
    );
  });

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
  }

  if(message.success)
    successElement = (<div className="SuccessMessage">{message.success}</div>);

  if(message.error)
    errorElement = (<div className="ErrorMessage">{message.error}</div>);

  return (
    <div className="ArticleForm RowLinedFlex">
      <h2>{pageContent.title}</h2>
      {contentElement}
      <form className="RowLinedFlex" onSubmit={handleSubmit}>
        <div className="ColLinedFlex">
          <label>Title:</label>
          <input
            type="text"
            name="title"
            value={article.title ? article.title : ""}
            placeholder="Title"
            required="required"
						pattern={validateMap.title.pattern}
						title={validateMap.title.message}
            disabled={isDeleting}
            onChange={handleChange}
            className="TextField"
          />
        </div>
        <div className="ColLinedFlex">
          <label>Content:</label>
          <textarea
            name="content"
            value={article.content ? article.content : ""}
            placeholder="Content"
            required="required"
						pattern={validateMap.content.pattern}
						title={validateMap.content.message}
            disabled={isDeleting}
            onChange={handleChange}
            className="TextArea"
          />
        </div>
        <div className={"ColLinedFlex" + (!isEditing ? " Hidden" : "")}>
          <label>Edit Sub Content:</label>
          <select
            name="type"
            disabled={!isEditing}
            onChange={handleChangeEditSubContent}
            className="Dropdownlist"
          >
            <option value="">None</option>
            <option value="newSubContent">New Sub Content</option>
            {editSubContentElements}
          </select>
        </div>
        {errorElement}
        {successElement}
        <div className="ArticleFormButtonArea ColLinedFlex">
          <button className="ButtonBlack">Confirm</button>
          {restoreElement}
        </div>
      </form>
      {subContentFormElements}
    </div>
  );
}
