import jsonApiService from "./JsonApiService";
import apiInfoMap from "../Data/ApiInfoMap";
import pageActionMap from "../Data/PageActionMap";
import screenComponentsMap from "../Data/ScreenComponentsMap";
import pageContentMap from "../Data/PageContentMap";
import userPageActionMap from "../Data/UserPageActionMap";
import jsUtil from "../Util/JSUtil";


class InfoPediaService
{
  constructor()
  {
    this.executeRequest = (apiInfo, appendData, headers,
        requestBody, responseCallback, errorCallback, extras) =>
    {
      try
      {
        const {url, method, shouldAppendData} = apiInfo;
        const newAppendData = shouldAppendData ? appendData : null;
        jsonApiService.executeRequest(url, method, newAppendData,
            headers, requestBody, responseCallback, errorCallback, extras);
      }
      catch(error)
      {
        console.log("Problems to execute request");
        console.log(error);
        const extras = {apiRequestError: error};
        errorCallback(extras);
      }
    }

    this.executePageActionInComponent = (field, pageActionKey,
        params, headers, requestBody, extras) =>
    {
      const c = this[field];

      if(c)
      {
        const pa = this.getPageAction(pageActionKey, {});
        const ai = apiInfoMap[pa.onLoadApiInfo];

        if(ai || field !== "mainArea")
        {
          const ne = extras ? extras : {};
          ne.pageAction = pa;
          this.executeRequest(ai, params, headers, requestBody,
            c.handleReceiveData, c.handleReceiveDataError, ne);
        }
        else
          c.setActualPageAction(pa);
      }
    }

    this.mainArea = null;
    this.header = null;
  }

  executePageActionInMainArea = (pageActionKey, params, headers, requestBody,
      extras) =>
  {
    this.executePageActionInComponent("mainArea", pageActionKey,
        params, headers, requestBody, extras);
  }

  executePageActionInHeader = (pageActionKey, params, headers, requestBody,
      extras) =>
  {
    this.executePageActionInComponent("header", pageActionKey,
        params, headers, requestBody, extras);
  }

  requestData = (apiInfoKey, params, headers, requestBody, responseCB,
      errorCB, extras) =>
  {
    const aim = apiInfoMap[apiInfoKey];
    this.executeRequest(aim, params, headers, requestBody, responseCB,
        errorCB, extras);
  }

  getPageAction = (key, defaultValue) =>
  {
    return jsUtil.getField(pageActionMap, key, defaultValue);
  }

  getScreenComponents = (pageAction, defaultValue) =>
  {
    return jsUtil.getField(screenComponentsMap, pageAction.components,
        defaultValue);
  }

  getPageContent = (pageAction, defaultValue) =>
  {
    return jsUtil.getField(pageContentMap, pageAction.pageContent,
        defaultValue);
  }
  
  getActualPageAction = () =>
  {
    return this.mainArea.getActualPageAction();
  }

  getActualScreenComponents = (defaultValue) =>
  {
    const pa = this.getActualPageAction();
    return this.getScreenComponents(pa, defaultValue);
  }

  getActualPageContent = (defaultValue) =>
  {
    const pc = this.getActualPageAction({});
    return this.getPageContent(pc, defaultValue);
  }

  setActualPageAction = (pageActionKey) =>
  {
    if(this.mainArea)
      this.mainArea.setActualPageAction(pageActionKey);
  }

  isActualPageActionUserSpecific = () =>
  {
    const pak = this.mainArea.getActualPageAction().key;
    const isUserSpecific = userPageActionMap[pak];
    return isUserSpecific === true ? true : false;
  }

  handleReceiveDataError = (component, extras, logMessage, newState) =>
  {
    const data = extras.apiResponseData ? extras.apiResponseData : {};
    const error = extras.apiResponseError ? extras.apiResponseError : "";
    const finalState = newState ? newState : {};
    finalState.message = this.getMessageObject(null, data.message);
    console.log(logMessage);
    console.log(finalState.message);
    console.log(error);
    console.log(extras);
    component.setState(finalState);
  }

  handleValidationError = (component, validation) =>
  {
    const message = this.getMessageObject(null, validation.message);
    console.log("Data validation error!");
    console.log(message);
    component.setState({message: message});
  }

  removeMessage = (component, time) =>
  {
    if(component.state.message)
    {
      setTimeout(() =>
      {
        component.setState({message: null});
      }, time);
    }
  }

  getUser = () =>
  {
    if(this.header)
      return this.header.getUser();
    
    return null;
  }

  setUser = (user) =>
  {
    this.header.setUser(user);
  }

  setHeader = (component) =>
  {
    this.header = component;
  }

  setMainArea = (component) =>
  {
    this.mainArea = component;
  }

  createTokenHeaders = () =>
  {
    let user = this.getUser();
    user = user ? user : {};
    return this.createDefaultHeaders(user.username, user.token);
  }

  createDefaultHeaders = (basicAuthUsername, basicAuthPassword,
      ipuCredentialsUsername, ipuCredentialsPassword) =>
  {
    let headers = {"Content-Type": "application/json"};

    if(basicAuthUsername)
    {
      headers["Authorization"] = 'Basic ' +
          btoa(basicAuthUsername + ":" + basicAuthPassword);
    }
    
    if(ipuCredentialsUsername)
    {
      headers["IPU-Credentials"] = 'Basic ' +
          btoa(ipuCredentialsUsername + ":" +
          ipuCredentialsPassword);
    }

    return headers;
  }

  createMessageObject = (success, error) =>
  {
    return {error: error, success: success};
  }

  getMessageObject = (success, error) =>
  {
    return {success: success, error: error};
  }

  getArrayFromData = (data, arrayField) =>
  {
    if(!Array.isArray(data))
    {
      const array = data[arrayField];
      
      if(Array.isArray(array))
        return array;
    }
    else
      return data;
    
    return [data];
  }
}


const infoPediaService = new InfoPediaService();
export default infoPediaService;