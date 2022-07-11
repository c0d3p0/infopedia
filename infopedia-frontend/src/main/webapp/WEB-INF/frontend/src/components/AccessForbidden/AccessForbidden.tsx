import appService from "../../service/AppService";

import icon from "../../images/infopedia_icon.png";

import "./AccessForbidden.css";


export default function AccessForbidden() {
  return (
    <div className="access-forbidden">
      <img src={icon} title="Infopedia Logo"/>
      <h1>Access Forbidden (403)</h1>
      {createMessageElements()}
    </div>
  );
}

const createMessageElements = () => {
  const urlParams = appService.getCurrentURLParameters();

  if(urlParams[1] === "admin-only") {
    return (
      <div>
        <p>
          It looks like you're trying to access a feature available 
          only for administrator users. Unfortunately, you will be 
          able to use this feature only if someone gives you 
          administrator privilegies, or if you hack our system and 
          make yourself an administrator.
        </p>
        <i className="bi bi-emoji-frown-fill" />
      </div>
    );
  } else {
    return (
      <div>
        <p>
          It looks like you're trying to access a feature available 
          only for registered users. Maybe it is a nice idea to create 
          an account so you can access some of these user exclusive 
          features.
        </p>
        <p>
          You think it is too annoying to create an account, well, then 
          you will be limited to non registered user features only.
        </p>
        <i className="bi bi-emoji-frown-fill" />
      </div>
    )
  }
}