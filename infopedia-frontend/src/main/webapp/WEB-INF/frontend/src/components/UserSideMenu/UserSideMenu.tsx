import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

import apiMap from "../../data/ApiMap";
import User from "../../model/User";
import appService from "../../service/AppService";
import UserSideMenuView from "./UserSideMenuView";


export default function UserSideMenu() {
  const [users, setUsers] = useState<User[] | null>(null);
  const [message, setMessage] = useState("");
  const location = useLocation();
  useEffect(() => fetchUsers(), [location]);


  const fetchUsers = () => {
    const apiData = apiMap.get("user-find-partial-random");
    const params = ["5"];
    appService.exchange({apiData, params}).then((response) => {
      const data = appService.convertResponseToArray<User>(response.data);

      if(data && data.length > 0) {
        setUsers(data);
        setMessage("");
      } else {
        throw new Error("No users!");
      }
    }).catch((error) => {
      const vre = error.request?.status;
      const cm = error.message ?? error;
      const m = vre ? "An error happened processing the request!" : cm;
      console.log(m);
      console.log(error);
      setMessage(m);
    });
  }


  return (
    <UserSideMenuView
      users={users}
      message={message}
    />
  );
}