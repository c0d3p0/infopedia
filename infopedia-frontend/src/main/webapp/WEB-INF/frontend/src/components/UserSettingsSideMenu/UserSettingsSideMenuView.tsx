import ElementData from "../../model/ElementData";

import "./UserSettingsSideMenu.css";


export default function UserSettingsSideMenuView(props: IProps) {
  return (
    <div className="user-settings-side-menu">
      {createMenuItems(props)}
    </div>
  );
}


const createMenuItems = (props: IProps) => {
  const {fullAccess, menuItemMap} = props;
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
  return elements;
}

interface IProps {
  fullAccess: boolean;
  menuItemMap: Map<string, ElementData>;
}