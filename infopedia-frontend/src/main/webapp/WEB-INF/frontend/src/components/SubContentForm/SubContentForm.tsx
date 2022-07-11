import { useEffect, useReducer } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";

import { subContentTypeUtil } from "../../model/SubContentType";
import appService from "../../service/AppService";
import apiMap from "../../data/ApiMap";
import Article from "../../model/Article";
import SubContent from "../../model/SubContent";
import User from "../../model/User";
import SubContentFormView from "./SubContentFormView";


export default function SubContentForm() {
  const [state, dispatch] = useReducer(reducer, createEmptyState());
  const user = useSelector<any, User>((state: any) => state.currentUser.value);
  const navigate = useNavigate();
  const urlParams = appService.getCurrentURLParameters();
  const subContentTypes = subContentTypeUtil.getValues();
  const location = useLocation();
  useEffect(() => {fetchArticle()}, [state.refreshTime, location]);
  useEffect(() => {prepareForm()}, [state.subContentId, location]);


  const fetchArticle = () => {
    const apiData = apiMap.get("article-find-by-id");
    const requestData = {apiData, params: [urlParams[1]]};
    appService.exchange<Article>(requestData).then((response) => {
      if(appService.isValidId(response.data?.id)) {
        const article = response.data;
        const uId = parseInt(user.id?.toString() ?? "-2");
        const aId = parseInt(article.userId?.toString() ?? "-1");
        const editing = uId !== aId;
        const newState = {...state, article, editing, message: ""};
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
  }

  const prepareForm = () => {
    if(state.subContentId && state.subContentId !== "new") {
      const apiData = apiMap.get("sub-content-find-by-id");
      const requestData = {apiData, params: [state.subContentId]};
      appService.exchange<SubContent>(requestData).then((response) => {
        if(appService.isValidId(response.data?.id)) {
          const {id, position, type, title, content} = response.data;
          updateState("state", {...state, id, position, type, title, content});
        } else {
          const valid = appService.isNumber(response.data?.id);
          const m = valid ? "The system is unavailable!" : "Sub content data not found!";
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
        alert(m);
        updateState("subContentId", "");
      });
    } else if(state.subContentId === "new" ) {
      const {message, subContentId, article} = state;
      updateState("state", {message, subContentId, article});
    }
  }

  const onSaveClick = () => {
    const validation = validateFields(state);

    if(validation.valid) {
      const apiKey = getSaveApiKey(state, urlParams);
      const apiData = apiMap.get(apiKey);
      const editing = isEditing(state);
      const params = editing ? [state.id?.toString() ?? "-1"] : [];
      const body = createSubContentFromState(state, user, editing);
      const credentials = {username: user.username, password: user.token};
      const requestData = {apiData, params: params, body, credentials};
      appService.exchange<SubContent>(requestData).then((response) => {
        const {message, article} = state;
        const refreshTime = Date.now();
        const subContentId = "";
        updateState("state", {message, article, refreshTime, subContentId});
        const text = editing ? "edited" : "added";
        alert(`Sub content ${text} with success!`);
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

  const onRemoveClick = () => {
    if("Do you really want to remove the selected sub content?") {
      const adminRemove = urlParams[0] === "edit-article-sub-content";
      const apiKey = `sub-content-remove${adminRemove ? "" : "-my-sub-content"}`;
      const apiData = apiMap.get(apiKey);
      const params = [state.id?.toString() ?? "-1"];
      const credentials = {username: user.username, password: user.token};
      const requestData = {apiData, params: params, credentials};
      appService.exchange<SubContent>(requestData).then((response) => {
        alert("Sub content removed with success!");
        const v = appService.isValidId(state.article?.id);
        const id = v ? state.article?.id : (urlParams[1] ? urlParams[1] : "-1");
        updateState("state", createEmptyState());
        navigate(`/my-article-sub-content/${id}`);
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

  const updateState = (field: string, value: any) => {
    dispatch({type: field, value});
  }

  return (
    <SubContentFormView
      state={state}
      subContentTypes={subContentTypes}
      updateState={updateState}
      onSaveClick={onSaveClick}
      onRemoveClick={onRemoveClick}
    />
  );
}


const validateFields = (state: IState) => {
  if(state.subContentId === "new") {
    if(!state.position || isNaN(parseInt(state.position)))
      return {valid: false, error: "You need to inform a position!"};

    if(!subContentTypeUtil.isValid(state.type))
      return {valid: false, error: "You need to select a sub content type!"};

    if(!state.title)
      return {valid: false, error: "You need to inform a title!"};

    if(!state.content)
      return {valid: false, error: "You need to inform a content!"};
  }
  
  return {valid: true, error: ""};
}

const createSubContentFromState = (state: IState, user: User, editing: boolean) => {
  const {id, position, type, title, content} = state;
  const p = parseInt(position ?? "-1");
  const t = subContentTypeUtil.createFrom(type);
  const uId = editing ? undefined : user.id;
  const aId = editing ? undefined : state.article?.id;
  return new SubContent(id, uId, aId, p, t, title, content);
}

const getSaveApiKey = (state: IState, urlParams: string[]) => {
  if(isEditing(state)) {
    const admin = urlParams[0] === "edit-article-sub-content";
    return admin ? "sub-content-edit" : "sub-content-edit-my-sub-content";
  }

  return "sub-content-add";
}

const isEditing = (state: IState) => {
  return state.subContentId && state.subContentId !== "new" ? true : false;
}

const createEmptyState = () => {
  const m = "Please wait, the data is being requested!";
  return {subContentId: "", message: m, refreshTime: Date.now()};
}

const reducer = (state: IState, action: any): IState => {
  if(action.type === "state")
    return action.value;
  else
    return {...state, [action.type]: action.value};
};

interface IState {
  editing?: boolean;
  refreshTime?: number;
  message: string,
  subContentId: string;
  article?: Article;

  id?: number,
  position?: string,
  type?: string,
  title?: string,
  content?: string
}


export type { IState };