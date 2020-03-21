import React from "react";

import "./Searchbar.css";


export default function SearchbarComponent(props)
{
  const {searchImage, searchData, handleChange, handleSubmit} = props;

  return (
    <div className="Searchbar ColLinedFlex">
      <form className="ColLinedFlex" onSubmit={handleSubmit}>
        <input
          type="text"
          name="searchData"
          value={searchData ? searchData : ""}
          required="required"
          placeholder="Search articles by"
          onChange={handleChange}
        />
        <select
          name="searchTypeKey"
          value={props.searchTypeKey}
          onChange={props.handleChange}
        >
          <option value="title">Title</option>
          <option value="username">Username</option>
        </select>
        <button className="RowLinedFlex">
          <img src={searchImage} alt="" />
        </button>
      </form>
    </div>
  );
}
