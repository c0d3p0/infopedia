import SubContentType, { subContentTypeUtil } from "../../model/SubContentType";
import Article from "../../model/Article";
import SubContent from "../../model/SubContent";

import "./ArticlePage.css";


export default function ArticlePageView(props: IProps) {
  if(props.article && !props.message) {
    return (
      <div className="article-page">
        <h1 id="article-content-item-0">{props.article.title}</h1>
        <hr />
        {createTextContentElement(props, props.article.content)}
        {createSubContentElements(props)}
      </div>
    );
  } else {
    const t = props.article?.id ? "No Title!" : "Requesting Article!";
    const m = props.article?.id ? props.message :
        "Please wait, the data is being requested!";

    return (
      <div className="article-page">
        <h1 id="article-content-item-0">{t}</h1>
        <hr />
        <p>{m}</p>
      </div>
    );
  }
}


const createSubContentElements = (props: IProps) => {
  let scl = props.article?.subContentList?.slice();
  scl = scl?.sort((s1, s2) => (s1.position ?? 0) - (s2.position ?? 0));

  return scl?.map((subContent, index) => {
    const id = `article-content-item-${index + 1}`;
    return (
      <div key={index}>
        <h2 id={id}>{subContent.title}</h2>
        <hr />
        {createSubContentElement(props, subContent)}
      </div>
    );
  });
}

const createSubContentElement = (props: IProps, subContent: SubContent) => {
  const sct = subContentTypeUtil.createFrom(subContent.type?.toString());

  if(subContentTypeUtil.equals(SubContentType.imageLink, sct))
    return createImageContentElement(props, subContent.content);

  if(subContentTypeUtil.equals(SubContentType.link, sct))
    return createLinkContentElement(props, subContent.content);

  return createTextContentElement(props, subContent.content);
}

const createTextContentElement = (props: IProps, content?: string) => {
  const texts = props.getContents(content);
  const elements = texts.map((text, index) => {
    return text.startsWith("\h3") ?
       (<h3 key={index}>{text.substring(2, text.length)}</h3>) :
       (<p key={index}>{text}</p>);
  });
  return (<div className="default-content">{elements}</div>);
}

const createImageContentElement = (props: IProps, content?: string) => {
  const links = props.getLinks(content);
  const elements = links.map((link, index) => <img key={index} src={link} />);
  return (<div className="image-content">{elements}</div>);
}

const createLinkContentElement = (props: IProps, content?: string) => {
  const links = props.getLinks(content);
  const elements = links.map((link, index) =>
    <a key={index} className="link" href={link} target="_blank">
      {link}
    </a>
  );
  return (<div className="link-content">{elements}</div>);
}

interface IProps {
  article: Article | null;
  message: string;
  getLinks(link?: string): string[];
  getContents(content?: string): string[];
}