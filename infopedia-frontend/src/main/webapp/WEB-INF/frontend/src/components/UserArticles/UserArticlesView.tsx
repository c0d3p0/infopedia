import Article from "../../model/Article";

import "./UserArticles.css";


export default function UserArticlesView(props: IProps) {
  if(props.articles && !props.message) {
    return (
      <div className="user-articles">
        <h1>My Articles</h1>
        {createArticleElements(props)}
      </div>
    );
  } else {
    const m = props.message ? props.message :
        "Please wait, the data is being requested!";
    
    return (
      <div className="user-articles">
        <h1>My Articles</h1>
        <div className="list-item">
          <hr />
          <span>{m}</span>
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
        <div>
          <h2>{a.title}</h2>
          <button 
            className="button-light bi bi-eye-fill"
            title="Show Article"
            onClick={(e) => props.onShowArticleClick(a.id?.toString() ?? "-1")}
          />
          <button
            className="button-light bi bi-journal-text"
            title="Edit Article"
            onClick={(e) => props.onEditArticleClick(a.id?.toString() ?? "-1")}
          />
          <button
            className="button-light bi bi-journals"
            title="Edit Article Sub Content"
            onClick={(e) => props.onEditSubContentClick(a.id?.toString() ?? "-1")}
          />
          <button
            className="button-light bi bi-journal-x"
            title="Remove Article"
            onClick={(e) => props.onRemoveArticleClick(a)}
          />
        </div>
        <p>{c}</p>
      </div>
    );
  }) ?? null;
}

interface IProps {
  articles: Article[] | null;
  message: string;
  onShowArticleClick(id: string): void;
  onEditArticleClick(id: string): void;
  onEditSubContentClick(id: string): void;
  onRemoveArticleClick(article: Article): void;
}