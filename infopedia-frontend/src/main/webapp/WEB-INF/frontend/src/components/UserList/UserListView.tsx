import User from "../../model/User";

import "./UserList.css";


export default function UserListView(props: IProps) {
  if(props.users && !props.message) {
    return (
      <div className="user-list">
        <div>
          <h1>Users</h1>
          <form onSubmit={(e) => {e.preventDefault(); props.onSearchClick()}}>
            <input
              type="text"
              value={props.search ?? ""}
              onChange={(e) => props.setSearch(e.target.value)}
              placeholder="Search by username or email"
            />
            <button className="bi bi-search"/>
          </form>
        </div>
        {createUserElements(props)}
      </div>
    );
  } else {
    const m = props.message ? props.message :
        "Please wait, the data is being requested!";
    
    return (
      <div className="user-list">
        <h1>Users</h1>
        <div className="list-item">
          <hr />
          <p>{m}</p>
        </div>
      </div>
    );
  }
}


const createUserElements = (props: IProps) => {
  return props.users?.map((u, index) => {
    return (
      <div  key={index} className="list-item">
        <hr />
        <div>
          <h2>{u.username}</h2>
          <button
            className="button-light bi bi-person-lines-fill"
            title="Edit User"
            onClick={(e) => props.onEditUserClick(u.id?.toString() ?? "-1")}
          />
          <button
            className="button-light bi bi-person-x-fill"
            title="Remove User"
            onClick={(e) => props.onRemoveUser(u)}
          />
        </div>
        <div>
          <label>Username: </label>
          <span>{u.username}</span>
        </div>
        <div>
          <label>Gender: </label>
          <span>{u.gender}</span>
        </div>
        <div>
          <label>Country: </label>
          <span>{u.country}</span>
        </div>
      </div>
    );
  }) ?? null;
}

interface IProps {
  users: User[] | null;
  search: string;
  message: string;
  setSearch(search: string): void;
  onEditUserClick(id: string): void;
  onRemoveUser(user: User): void;
  onSearchClick(): void;
}