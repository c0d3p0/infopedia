import ElementData from "../../model/ElementData";

import "./UserMenu.css";


export default function UserMenuView(props: IProps) {
  return (
    <div className="user-menu">
      <i className="bi bi-person-lines-fill"/>
      {createMenu(props)}
    </div>
  )
}

const createMenu = (props: IProps) => {
  const {fullAccess, username, menuItemMap} = props;
  const elements: JSX.Element[] = [];
  menuItemMap.forEach((v, k) => {
    if(fullAccess || v.forEveryone) {
      elements.push(
        <button
          key={k}
          className="menu-item"
          onClick={(e) => v.action()}
        >
          {v.label}
        </button>
      );
    }
  });

  return (
    <div className="user-menu-dropdown box-dark">
      <span>Welcome User</span>
      <b>{username}</b>
      <hr />
      {elements}
    </div>
  );
}


interface IProps {
  fullAccess: boolean;
  username: string;
  menuItemMap: Map<string, ElementData>
}