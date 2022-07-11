import icon from "../../images/infopedia_icon.png";

import "./About.css";


export default function About() {
  return (
    <div className="about">
      <img src={icon} title="Infopedia Logo"/>
      <h1>Infopedia, a Free Library</h1>
      <p>
        Infopedia is a free library with the objective of 
        offerring as much knowledge as possible, without 
        any cost, available for everyone around the world. 
      </p>
      <p>
        We believe everyone should have access to knowledge, 
        and if you think alike, then consider creating an 
        account and writing articles about what you know. 
        All our content is the result of the contribution 
        of users from everywhere in the world, that put 
        their valuable effort and time to make sure people 
        like you can have access to what they know and 
        hopefully learn new things.
      </p>
    </div>
  );
}