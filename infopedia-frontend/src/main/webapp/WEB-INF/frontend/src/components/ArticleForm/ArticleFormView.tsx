import { IState } from "./ArticleForm";

import "./ArticleForm.css";


export default function ArticleFormView(props: IProps) {
  if(!props.state.message) {
    return (
      <div className="article-form">
        <h1>{props.state.formData?.title}</h1>
        <hr />
        <form onSubmit={(e) => {e.preventDefault(); props.onSaveClick()}}>
          <label>Title</label>
          <input
            className="textfield-light"
            type="text"
            value={props.state.title ?? ""}
            onChange={(e) => {props.updateState("title", e.target.value)}}
            placeholder="Title"
          />
          <label>Content</label>
          <textarea
            className="textarea-light"
            value={props.state.content ?? ""}
            onChange={(e) => {props.updateState("content", e.target.value)}}
          />
          <p>
            TEXT HINT: Use 1 line break for a paragraph and 3 
            line breaks for subtitles.
          </p>
          <p>
            LINK HINT: Use line breaks to add multiple links.
          </p>
          <button className="button-light">Save</button>
        </form>
      </div>
    );
  } else {
    return (
      <div className="article-form">
        <h1>{props.state.formData?.title}</h1>
        <hr />
        <p>{props.state.message}</p>
      </div>
    );
  }
}


interface IProps {
  state: IState;
  updateState(field: string, value: any): void;
  onSaveClick(): void;
}