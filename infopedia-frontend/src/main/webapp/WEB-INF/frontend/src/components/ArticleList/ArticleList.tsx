import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from 'react-router-dom';

import apiMap from "../../data/ApiMap";
import articleActionMap from "../../data/ArticleActionMap";
import Article from "../../model/Article";
import User from "../../model/User";
import appService from "../../service/AppService";
import ArticleListView from "./ArticleListView";


export default function ArticleList() {
  const navigate = useNavigate();
  const user = useSelector<any, User>((state: any) => state.currentUser.value);
  const [articles, setArticles] = useState<Article[] | null>(null);
  const [refreshTime, setRefreshTime] = useState(Date.now());
  const [message, setMessage] = useState("");
  const location = useLocation();
  useEffect(() => fetchArticles(), [location, refreshTime]);


  const fetchArticles = () => {
    const urlParams = appService.getCurrentURLParameters();
    const apiKey = articleActionMap.get(urlParams[1] ?? "all") ??
        articleActionMap.entries().next().value as string;
    const apiData = apiMap.get(apiKey);
    const params = urlParams[2] ? [urlParams[2]] : [];
    appService.exchange({apiData, params}).then((response) => {
      const data = appService.convertResponseToArray<Article>(response.data);

      if(data && data.length > 0) {
        setArticles(data);
        setMessage("");
      }
      else
        throw new Error("No article found!");
    }).catch((error) => {
      const status = error.response?.status;
      const gm = "An error happened processing the request!";
      const re = appService.getRequestErrorMessage(error);
      const cm = status === 500 ? gm : (error.message ?? gm);
      const m = status === 404 ? re : cm;
      console.log(m);
      console.log(error);
      setMessage(m);
    });
  }

  const onArticleClick = (id: string) => {
    if(id)
      navigate(`/article-page/${id}`);
  }

  const onEditArticleClick = (id: string) => {
    if(id)
      navigate(`/edit-article/${id}`);
  }

  const onEditSubContentClick = (id: string) => {
    if(id)
      navigate(`/edit-article-sub-content/${id}`);
  }

  const onRemoveArticleClick = (article: Article) => {
    if(confirm(`Are you sure you want to remove the article ${article.title}?`)) {
      const apiData = apiMap.get("article-remove");
      const cred = {username: user.username, password: user.token};
      const params = [article.id?.toString() ?? "-1"];
      const requestData = {apiData, credentials: cred, params};      
      appService.exchange<Article>(requestData).then((response) => {
        setRefreshTime(Date.now());
        navigate("/articles");
      }).catch((error) => {
        const vre = error.response?.status === 401;
        const m = vre ? "Login expired, you have to login again!" :
            "An error happened sending the data to the system!";
        console.log(m);
        console.log(error);
        alert(m);
      });
    }
  }


  return (
    <ArticleListView
      user={user}
      articles={articles}
      message={message}
      onArticleClick={onArticleClick}
      onEditArticleClick={onEditArticleClick}
      onEditSubContentClick={onEditSubContentClick}
      onRemoveArticleClick={onRemoveArticleClick}
    />
  );
}