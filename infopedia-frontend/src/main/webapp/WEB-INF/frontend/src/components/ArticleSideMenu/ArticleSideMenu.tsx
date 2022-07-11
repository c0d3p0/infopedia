import { useSelector } from "react-redux";
import { Link } from "react-router-dom";

import Article from "../../model/Article";

import "./ArticleSideMenu.css";


export default function ArticleSideMenu() {
  const article = useSelector<any, Article>((state: any) => state.currentArticle.value);

  return (
    <div className="article-side-menu">
      <div>
        <h3>Contents</h3>
        {createArticleTitleLink(article)}
        {createSubContentTitleLinks(article)}
      </div>
      {createAuthorSectionElement(article)}
    </div>
  );
}


const createArticleTitleLink = (article?: Article) => {
  if(article?.id && article.id > -1) {
    const href = "#article-content-item-0";
    return (<a className="link" href={href}>{article.title}</a>);
  }

  return <span>No content available!</span>;
}

const createSubContentTitleLinks = (article?: Article) => {
  let scl = article?.subContentList?.slice();
  scl = scl?.sort((s1, s2) => (s1.position ?? 0) - (s2.position ?? 0));
  return scl?.map((s, index) => {
    const href = `#article-content-item-${index + 1}`;
    return (<a key={index} className="link" href={href}>{s.title}</a>);
  });
}

const createAuthorSectionElement = (article?: Article) => {
  const username = article?.user?.username;
  const id = article?.user?.id;

  if(id && username) {
    return (
      <div>
        <h3>Author</h3>
        <Link className="link" to={`/articles/user-id/${id}`}>
          {username}
        </Link>
      </div>
    );
  }

  return null;
}