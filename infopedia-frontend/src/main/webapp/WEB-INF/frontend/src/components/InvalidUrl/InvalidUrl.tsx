import icon from "../../images/infopedia_icon.png";

import "./InvalidUrl.css";


export default function InvalidUrl() {
  return (
    <div className="invalid-url">
      <img src={icon} title="Infopedia Logo"/>
      <h1>Page Not Found (404)</h1>
      <p>
        It looks like our library is lacking knowledge 
        about this subject, maybe you should consider 
        doing a research on the subject and then writing 
        an article on our system about it to ensure that 
        the same thing doesn't happen to another person 
        in the future.
      </p>
      <p>
        If you feel that you don't have the energy for 
        such task, then we suggest you to look for 
        something else in our system, maybe this time 
        you're lucky.
      </p>
      <i className="bi bi-emoji-laughing-fill" />
    </div>
  );
}