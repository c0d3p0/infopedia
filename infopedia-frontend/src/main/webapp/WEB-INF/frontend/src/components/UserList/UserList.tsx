import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from 'react-router-dom';

import apiMap from "../../data/ApiMap";
import appService from "../../service/AppService";
import User from "../../model/User";
import UserListView from "./UserListView";


export default function UserList() {
  const [users, setUsers] = useState<User[] | null>(null);
  const [search, setSearch] = useState("");
  const [refreshTime, setRefreshTime] = useState(Date.now());
  const [message, setMessage] = useState("");
  const adminUser = useSelector<any, User>((state) => state.currentUser.value);
  const navigate = useNavigate();
  const urlParams = appService.getCurrentURLParameters();
  useEffect(() => fetchUsers(), [adminUser, refreshTime]);


  const fetchUsers = () => {
    if(appService.isValidId(adminUser.id) && adminUser.systemAdmin) {
      const apiKey = `user-find${urlParams[1] ? "-by-username-or-email" : ""}`;
      const apiData = apiMap.get(apiKey);
      const params = urlParams[1] ? [urlParams[1]] : [];
      const cred = {username: adminUser.username, password: adminUser.token};
      const requestData = {apiData, params, credentials: cred};
      appService.exchange(requestData).then((response) => {
        const data = appService.convertResponseToArray<User>(response.data);

        if(data && data.length > 0) {
          setUsers(data);
          setMessage("");
        }
        else
          throw new Error("No user found!");
      }).catch((error) => {
        const status = error.response?.status;
        const gm = "An error happened processing the request!";
        const re = appService.getRequestErrorMessage(error);
        const cm = status === 500 ? gm : (error.message ?? gm);
        const m = status === 401 || status === 404 ? re : cm;
        console.log(m);
        console.log(error);
        setMessage(m);
      });
    }
  }

  const onSearchClick = () => {
    const param = search ? `/${search}` : "";
    const path = `/users${param}`;
    setRefreshTime(refreshTime + 1);
    navigate(path);
  }

  const onEditUserClick = (id: string) => {
    if(id)
      navigate(`/edit-user/${id}`);
  }

  const onRemoveUser = (user: User) => {
    if(confirm(`Are you sure you want to remove the user ${user.username}?`)) {
      const apiData = apiMap.get("user-remove");
      const cred = {username: adminUser.username, password: adminUser.token};
      const params = [user.id?.toString() ?? "-1"];
      const requestData = {apiData, credentials: cred, params};      
      appService.exchange<User>(requestData).then((response) => {
        setRefreshTime(Date.now());
        navigate("/users");
      }).catch((error) => {
        const m = error.response?.status === 401 ?
            "Login expired, you have to login again!" :
            "An error happened sending the data to the system!";
        console.log(m);
        console.log(error);
        alert(m);
      });
    }
  }


  return (
    <UserListView
      users={users}
      search={search}
      message={message}
      setSearch={setSearch}
      onSearchClick={onSearchClick}
      onEditUserClick={onEditUserClick}
      onRemoveUser={onRemoveUser}
    />
  );
}