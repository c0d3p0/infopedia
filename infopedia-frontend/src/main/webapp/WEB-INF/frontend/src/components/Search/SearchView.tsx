import "./Search.css";


export default function SearchView(props: IProps) {
  return (
    <form
      className="search"
      onSubmit={(e) => {e.preventDefault(); props.onSearchClick()}}
    >
      <input
        type="text"
        value={props.search ?? ""}
        placeholder="Search"
        onChange={(e) => {props.setSearch(e.target.value)}}
      />
      <select
        value={props.searchBy ?? ""}
        onChange={(e) => {props.setSearchBy(e.target.value)}}
      >
        {createSearchByOptions(props)}
      </select>
      <button className="bi bi-search"/>
    </form>
  );
}


const createSearchByOptions = (props: IProps) => {
  const elements: JSX.Element[] = [];
  props.searchByMap.forEach((v, k) => {
    elements.push(
      <option
        key={k}
        value={k}
        onChange={(e) => props.setSearchBy(k)}
      >
        {v}
      </option>
    );
  });

  return elements;
}


interface IProps {
  search: string,
  searchBy: string,
  searchByMap: Map<string, string>;
  setSearch(search: string): void;
  setSearchBy(searchBy: string): void;
  onSearchClick(): void;
}