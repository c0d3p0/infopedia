import { IState } from "./SubContentForm";
import appService from "../../service/AppService";
import SubContentType from "../../model/SubContentType";

import "./SubContentForm.css";


export default function SubContentFormView(props: IProps) {
  if(props.state.article && !props.state.message) {
    return (
      <div className="sub-content-form">
        <h1>Article Sub Content</h1>
        <hr />
        <div>
          <h2>{props.state.article.title}</h2>
          <label>Sub Content</label>
          <select
            className="combobox-light"
            value={props.state.subContentId}
            onChange={(e) => props.updateState("subContentId", e.target.value)}
          >
            <option value="">Select a sub content</option>
            {createSubContentOptions(props)}
            {createNewSubContentOption(props)}
          </select>
          {createRemoveButton(props)}
        </div>
        {createSubContentForm(props)}
      </div>
    );
  } else {
    return (
      <div className="sub-content-form">
        <h1>Article Sub Content</h1>
        <hr />
        <p>{props.state.message}</p>
      </div>
    );
  }
}


const createSubContentForm = (props: IProps) => {
  const scId = props.state.subContentId;
  const validSC = appService.isValidId(props.state.id);
  let v = scId === "new" || (scId && scId !== "new" && validSC) ? true : false;
  return v ? (
    <div>
      <h1>Sub Content</h1>
      <hr />
      <form onSubmit={(e) => {e.preventDefault(); props.onSaveClick()}}>
        <label>Position</label>
        <input
          className="textfield-light"
          type="text"
          value={props.state.position ?? ""}
          onChange={(e) => {props.updateState("position", e.target.value.replace(/\D/g, ""))}}
          placeholder="Position"
        />
        <label>Sub Content Type</label>
        <select
          className="combobox-light"
          value={props.state.type ?? ""}
          onChange={(e) => {props.updateState("type", e.target.value)}}
        >
          <option value="">Select a sub content type</option>
          {createSubContentTypeOptions(props.subContentTypes)}
        </select>
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
  ) : null;
}

const createSubContentOptions = (props: IProps) => {
  return props.state.article?.subContentList?.map((sc, index) =>
    <option key={index} value={sc.id}>{sc.title}</option>
  );
}

const createRemoveButton = (props: IProps) => {
  if(props.state.subContentId && props.state.subContentId !== "new") {
    return (
      <button
        className="button-light sub-content-remove"
        onClick={(e) => props.onRemoveClick()}
      >
        Remove Sub Content
      </button>
    );
  }

  return null;
}

const createSubContentTypeOptions = (subContentTypes: SubContentType[]) => {
  const o: JSX.Element[] = [];
  subContentTypes.forEach((sct, index) => o.push(
    <option key={index} value={sct}>{sct.toUpperCase()}</option>)
  );
  return o;
}

const createNewSubContentOption = (props: IProps) => {
  return  !props.state.editing ? (
    <option value="new">Create new sub content</option>
  ) : null;
}

interface IProps {
  state: IState;
  subContentTypes: SubContentType[];
  updateState(field: string, value: any): void;
  onSaveClick(): void;
  onRemoveClick(): void;
}