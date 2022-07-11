import { useNavigate } from "react-router-dom";

import "./LoginMenu.css";


export default function LoginMenu() {
  const navigate = useNavigate();


  return (
    <div className="login-menu">
      <button
        className="button-light"
        onClick={(e) => navigate("/login-form")}
      >
        Login
      </button>
      <button
        className="button-light"
        onClick={(e) => navigate("/sign-up-form")}
      >
        Sign Up
      </button>
    </div>
  );
}