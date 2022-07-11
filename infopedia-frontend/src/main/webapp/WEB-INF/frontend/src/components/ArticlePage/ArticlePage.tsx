import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";

import { setCurrentArticle } from "../../features/CurrentArticle";
import apiMap from "../../data/ApiMap";
import Article from "../../model/Article";
import appService from "../../service/AppService";
import ArticlePageView from "./ArticlePageView";


export default function ArticlePage() {
  const [article, setArticle] = useState<Article | null>(null);
  const [message, setMessage] = useState("");
  const dispatch = useDispatch();
  useEffect(() => fetchArticle(), []);
  

  const fetchArticle = () => {
    const urlParams = appService.getCurrentURLParameters();
    const apiData = apiMap.get("article-find-by-id");
    const params = urlParams[1] ? [urlParams[1]] : ["-1"];
    appService.exchange<Article>({apiData, params}).then((response) => {
      if(response.data?.id) {
        setArticle(response.data);
        setMessage("");
        dispatch(setCurrentArticle(response.data));
      } else {
        throw new Error("Article not found!");
      }
    }).catch((error) => {
      const a = new Article(-1);
      const status = error.response?.status;
      const gm = "An error happened processing the request!";
      const re = appService.getRequestErrorMessage(error);
      const cm = status === 500 ? gm : (error.message ?? gm);
      const m = status === 404 ? re : cm;
      console.log(m);
      console.log(error);
      dispatch(setCurrentArticle({...a}));
      setArticle(a);
      setMessage(m);
    });
  }

  const getLinks = (link?: string) => {
    if(link) {
      const regex = /[\n\u0085\t\u2029\r]+/g;
      const fl = link ? link.replaceAll(regex, "\n").trim() : "";
      return fl.split("\n").filter((l) => l && l.replaceAll(regex, ""));
    }
    
    return [] as string[];
  }
  
  const getContents = (content?: string) => {
    if(content) {
      let regex = /[\n\u0085][\n\u0085][\n\u0085]/g;
      let fc = content ? content.replaceAll(regex, "\n\h3").trim() : "";
      regex = /[\n\u0085\t\u2029\r]/g;
      fc = fc ? fc.replaceAll(regex, "\n").trim() : "";
      return fc.split("\n").filter((l) => l && l.replaceAll(regex, ""));
    }
  
    return [] as string[];
  }


  return (
    <ArticlePageView
      article={article}
      message={message}
      getLinks={getLinks}
      getContents={getContents}
    />
  );
}