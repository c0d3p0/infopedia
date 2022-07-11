import { Link } from "react-router-dom";

import User from "../../model/User";

import "./UserSideMenu.css";


export default function UserSideMenuView(props: IProps) {
  if(props.users && !props.message) {
    return (
      <div className="user-side-menu">
        <h3>See Articles By</h3>
        {createUserLinks(props)}
      </div>
    );
  } else {
    const m = props.message ? props.message : "Getting the data!";

    return (
      <div className="user-side-menu">
        <h3>See Articles By</h3>
        <span>{m}</span>
      </div>
    );
  }
}


const createUserLinks = (props: IProps) => {
  return props.users?.map((u, index) =>
    <Link key={index} className="link" to={`/articles/user-id/${u.id}`}>
      {u.username}
    </Link>
  );
}

interface IProps {
  users: User[] | null;
  message: string;
}