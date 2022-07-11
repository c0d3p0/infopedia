import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate } from 'react-router-dom';

import apiMap from "../../data/ApiMap";
import appService from "../../service/AppService";
import User from "../../model/User";
import Article from "../../model/Article";
import UserArticlesView from "./UserArticlesView";


export default function UserArticles() {
  const [articles, setArticles] = useState<Article[] | null>(null);
  const [refreshTime, setRefreshTime] = useState(Date.now());
  const [message, setMessage] = useState("");
  const user = useSelector<any, User>((state) => state.currentUser.value);
  const navigate = useNavigate();
  useEffect(() => fetchArticles(), [user, refreshTime]);


  const fetchArticles = () => {
    if(appService.isValidId(user.id)) {
      const apiData = apiMap.get("article-find-by-user-id");
      const params = [user.id?.toString() ?? "-1"];
      appService.exchange({apiData, params}).then((response) => {
        const data = appService.convertResponseToArray<Article>(response.data);
  
        if(data && data.length > 0) {
          setArticles(data);
          setMessage("");
        }
        else
          throw new Error("No article found!");
      }).catch((error) => {
        const vre = error.request?.status;
        const cm = error.message ?? error;
        const m = vre ? "An error happened processing the request!" : cm;
        console.log(m);
        console.log(error);
        setMessage(m);
      });
    }
  }

  const onShowArticleClick = (id: string) => {
    if(id)
      navigate(`/article-page/${id}`);
  }

  const onEditArticleClick = (id: string) => {
    if(id)
      navigate(`/my-article/${id}`);
  }

  const onEditSubContentClick = (id: string) => {
    if(id)
      navigate(`/my-article-sub-content/${id}`);
  }

  const onRemoveArticleClick = (article: Article) => {
    if(confirm(`Are you sure you want to remove the article ${article.title}?`)) {
      const apiData = apiMap.get("article-remove-my-article");
      const cred = {username: user.username, password: user.token};
      const params = [article.id?.toString() ?? "-1"];
      const requestData = {apiData, credentials: cred, params};      
      appService.exchange<Article>(requestData).then((response) => {
        setRefreshTime(Date.now());
        navigate(`/my-articles/${user.id}`);
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
    <UserArticlesView
      articles={articles}
      message={message}
      onShowArticleClick={onShowArticleClick}
      onEditArticleClick={onEditArticleClick}
      onEditSubContentClick={onEditSubContentClick}
      onRemoveArticleClick={onRemoveArticleClick}
    />
  );
}