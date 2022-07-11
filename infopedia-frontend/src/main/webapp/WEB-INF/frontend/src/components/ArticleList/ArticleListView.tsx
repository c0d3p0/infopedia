import Article from "../../model/Article";
import User from "../../model/User";
import appService from "../../service/AppService";

import "./ArticleList.css";


export default function ArticleListView(props: IProps) {
  if(props.articles && !props.message) {
    return (
      <div className="article-list">
        <h1>Article Search Result</h1>
        {createArticleElements(props)}
      </div>
    );
  } else {
    const m = props.message ? props.message :
        "Please wait, the data is being requested!";
    
    return (
      <div className="article-list">
        <h1>Article Search Result</h1>
        <div className="list-item">
          <hr />
          <p>{m}</p>
        </div>
      </div>
    );
  }
}


const createArticleElements = (props: IProps) => {
  return props.articles?.map((a, index) => {
    const overflow = a.content?.length && a.content?.length > 800;
    const c = overflow ? a.content?.substring(0, 800) + "..." : a.content;

    return (
      <div key={index} className="list-item">
        <hr />
        <button onClick={(e) => props.onArticleClick(a.id?.toString() ?? "")}>
          <h2>{a.title}</h2>
          <p>{c}</p>
        </button>
        {createArticleActionElements(props, a)}
      </div>
    );
  }) ?? null;
}

const createArticleActionElements = (props: IProps, article: Article) => {
  const aId = article.id?.toString() ?? "-1";
  const uId = appService.isValidId(props.user.id);

  return uId && (props.user.systemAdmin ? true : false) ? (
    <div>
      <button
        className="button-light bi bi-journal-text"
        title="Edit Article"
        onClick={(e) => props.onEditArticleClick(aId)}
      />
      <button
        className="button-light bi bi-journals"
        title="Edit Article Sub Content"
        onClick={(e) => props.onEditSubContentClick(aId)}
      />
      <button
        className="button-light bi bi-journal-x"
        title="Remove Article"
        onClick={(e) => props.onRemoveArticleClick(article)}
      />
    </div>
  ) : null;
}

interface IProps {
  user: User;
  articles: Article[] | null;
  message: string;
  onArticleClick(id: string): void;
  onEditArticleClick(id: string): void;
  onEditSubContentClick(id: string): void;
  onRemoveArticleClick(article: Article): void;
}