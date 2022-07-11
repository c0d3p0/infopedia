import ElementData from "../../model/ElementData";

import "./NavLink.css"


export default function NavLinkView(props: IProps) {
  return (
    <nav className="nav-link">
      {createMenuItems(props)}
    </nav>
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