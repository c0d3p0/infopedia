import { useEffect } from 'react';
import { useCookies } from 'react-cookie';
import { useDispatch, useSelector } from 'react-redux';
import { BrowserRouter as Router, NavigateFunction} from 'react-router-dom';

import { setCurrentUser } from '../../features/CurrentUser';
import appService from '../../service/AppService';
import User from '../../model/User';
import AppView from './AppView';

import './App.css';


export default function App() {
  const [cookies, setCookies, removeCookies] = useCookies(["user"]);
  const userCookie = appService.getFromCookie<User>("user", cookies);
  const user = useSelector<any, User>((state) => state.currentUser.value);
  const dispatch = useDispatch();
  useEffect(() => readUserFromCookies(), [cookies]);


  const readUserFromCookies = () => {
    if(appService.isValidId(userCookie.id) && userCookie.id !== user.id)
      dispatch(setCurrentUser({...userCookie}));
  }

  const redirectIfNoPermission = (navigate: NavigateFunction) => {
    let path = getAccessForbiddenPath();
    
    if(path)
      navigate(path);
  }

  const getAccessForbiddenPath = () => {
    const urlParams = appService.getCurrentURLParameters();
    const invalidUser = !appService.isValidId(userCookie.id);

    if(invalidUser && userOnlyMap.has(urlParams[0]))
      return "/access-forbidden";

    if((invalidUser || !userCookie.systemAdmin) && adminOnlyMap.has(urlParams[0]))
      return "/access-forbidden/admin-only";

    return null;
  }


  return (
    <Router>
      <AppView redirectIfNoPermission={redirectIfNoPermission}/>
    </Router>
  );
}


const userOnlyMap = new Set<string>(["my-profile",
    "my-articles", "my-article", "my-article-sub-content", 
    "add-article", "remove-my-account"]);

const adminOnlyMap = new Set<string>(["users",
    "add-user", "edit-user", "edit-article",
    "edit-article", "edit-article-sub-content"]);
