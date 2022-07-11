import { useEffect, useReducer } from "react";
import { useSelector } from "react-redux";
import { useLocation } from "react-router-dom";

import apiMap from "../../data/ApiMap";
import appService from "../../service/AppService";
import User from "../../model/User";
import ArticleFormView from "./ArticleFormView";
import Article from "../../model/Article";


export default function ArticleForm() {
  const [state, dispatch] = useReducer(reducer, createEmptyState());
  const user = useSelector<any, User>((state: any) => state.currentUser.value);
  const urlParams = appService.getCurrentURLParameters();
  const location = useLocation();
  useEffect(() => {prepareForm()}, [location]);


  const prepareForm = () => {
    const newFormData = formDataMap.get(urlParams[0]) ?? null;
    updateState("formData", newFormData);

    if(newFormData?.editing) {
      const apiData = apiMap.get("article-find-partial-by-id");
      const requestData = {apiData, params: [urlParams[1]]};
      appService.exchange<Article>(requestData).then((response) => {
        if(appService.isValidId(response.data?.id)) {
          const {title, content} = response.data;
          const newState = {formData: newFormData, message: "", title, content};
          updateState("state", newState);
        } else {
          const valid = appService.isNumber(response.data?.id);
          const m = valid ? "The system is unavailable!" : "Article data not found!";
          throw new Error(m);
        }
      }).catch((error) => {
        const status = error.response?.status;
        const gm = "An error happened processing the request!";
        const re = appService.getRequestErrorMessage(error);
        const cm = status === 500 ? gm : (error.message ?? gm);
        const m = status === 404 ? re : cm;
        console.log(m);
        console.log(error);
        updateState("message", m);
      });
    } else {
      updateState("state", {formData: newFormData, message: ""});
    }
  }

  const onSaveClick = () => {
    const validation = validateFields(state);

    if(validation.valid) {
      const userId = !state.formData.editing ? user.id : undefined;
      const apiData = apiMap.get(state.formData.apiKey);
      const body = new Article(undefined, userId, state.title, state.content);
      const cred = {username: user.username, password: user.token};
      const params = state.formData.editing ? [urlParams[1]] : [];
      const requestData = {apiData, body, credentials: cred, params};
      appService.exchange<Article>(requestData).then((response) => {
        if(!state.formData.editing) {
          const newState = {formData: state.formData};
          updateState("state", newState);
        }
        
        const text = state.formData.editing ? "edited" : "added";
        alert(`Article ${text} with success!`);
      }).catch((error) => {
        const status = error.response?.status;
        const vre = status === 400 || status === 401 || status === 409;
        const re = appService.getRequestErrorMessage(error);
        const m = vre ? re : "An error happened sending the data to the system!";
        console.log(m);
        console.log(error);
        alert(m);
      });
    } else {
      console.log(validation.error);
      alert(validation.error);
    }
  }

  const updateState = (field: string, value: any) => {
    dispatch({type: field, value});
  }


  return (
    <ArticleFormView
      state={state}
      updateState={updateState}
      onSaveClick={onSaveClick}
    />
  );
}


const validateFields = (state: IState) => {
  if(state.formData.editing)
    return {valid: true, error: ""};

  if(!state.title)
    return {valid: false, error: "You need to inform a title!"};

  if(!state.content)
    return {valid: false, error: "You need to inform a content!"};

  return {valid: true, error: ""};
}

const reducer = (state: IState, action: any): IState => {
  if(action.type === "state")
    return action.value;
  else
    return {...state, [action.type]: action.value};
};

const createEmptyState = () => {
  const fd = {title: "Loading Data", apiKey: "", editing: false};
  const m = "Please wait, the data is being requested!";
  return {formData: fd, message: m};
}

const formDataMap = new Map<string, IFormData>([
  ["my-article", {title: "Edit Article", apiKey: "article-edit-my-article", editing: true}],
  ["add-article", {title: "Add Article", apiKey: "article-add", editing: false}],
  ["edit-article", {title: "Edit Article", apiKey: "article-edit", editing: true}]
]);

interface IState {
  formData: IFormData,
  message: string,

  title?: string,
  content?: string
}

interface IFormData {
  title: string;
  apiKey: string;
  editing: boolean;
}


export type { IState, IFormData };