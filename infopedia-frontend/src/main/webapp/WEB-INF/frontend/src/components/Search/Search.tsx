import { useState } from "react";
import { useNavigate } from "react-router-dom";

import SearchView from "./SearchView";


export default function Search() {
  const [search, setSearch] = useState("");
  const [searchBy, setSearchBy] = useState("");
  const navigate = useNavigate();
  const searchByMap = new Map<string, string>([
    ["title", "Title"], ["content", "Content"], ["sub-content", "Sub Content"], 
  ]);


  const onSearchClick = () => {
    const param = search ? `/${search}` : "";
    const searchPath = search ? `/${searchBy ? searchBy : "title"}` : "";
    navigate(`/articles${searchPath}${param}`);
  }


  return (
    <SearchView
      search={search}
      searchBy={searchBy}
      searchByMap={searchByMap}
      setSearch={setSearch}
      setSearchBy={setSearchBy}
      onSearchClick={onSearchClick}
    />
  );
}