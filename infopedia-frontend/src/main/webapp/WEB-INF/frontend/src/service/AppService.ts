import axios, { AxiosRequestHeaders, AxiosRequestConfig } from "axios";

import { IAPIData } from "../data/ApiMap";
import User from "../model/User";


class AppService {
  exchange = <T>(requestData: RequestData) => {
    if(requestData.apiData) {
      const {apiData, params, credentials, body} = requestData;
      const method = apiData.method.toLowerCase();
      const config = this.createConfig(credentials ?? {}, method);
      const url = this.addParametersToURL(apiData.url, params);

      if(method === "get")
        return axios.get<T>(url, config);

      if(method === "post")
        return axios.post<T>(url, body, config);

      if(method === "put")
        return axios.put<T>(url, body, config);

      if(method === "patch")
        return axios.patch<T>(url, body, config);

      if(method === "delete")
        return axios.delete<T>(url, config);
    }

    throw Error("Invalid request");
  }

  convertResponseToArray = <T>(data: any | Catalogue<any> | any[]) => {
    if(Array.isArray(data))
      return data as T[];
    
    if(data) {
      const c = data as Catalogue<T>;

      if(c?.list)
        return c.list;

      if(Object.keys(data).length > 1)
        return [data] as T[];
    }

    return [] as T[];
  }

  addParametersToURL = (url: string, params?: string[]) => {
    return params && params.length > 0 ? `${url}/${params.join("/")}` : url; 
  }

  createConfig = (credentials: Credentials, method: string) => {
    const notGetOrDelete = !(method === "get" || method === "delete");
    const {username, password, ipuUsername, ipuPassword} = credentials;
    const headers: AxiosRequestHeaders = {};
    let auth;

    if(notGetOrDelete) {
      headers.Accept = "application/json";
      headers["Content-Type"] = "application/json";
    }

    if(username && password) 
      auth = {username, password};

    if(typeof ipuUsername === "string" && typeof ipuPassword === "string")
      headers["IPU-Credentials"] = `Basic ${btoa(`${ipuUsername}:${ipuPassword}`)}`;
    
    const config: AxiosRequestConfig = {headers: headers, auth: auth};
    return config;
  }

  getFromCookie = <T>(key: string, cookies: any) => {
    try {
      return JSON.parse(atob(cookies[key])) as T;
    } catch(error) {
      return new User();
    }
  }

  convertToCookie = (data: any) => {
    return btoa(JSON.stringify(data));
  }

  getCurrentURLParameters = () => {
    const url = window.location.href;
    const params = url.substring(7, url.length).split("/");
    return params.slice(1, params.length);
  }

  isNumber = (data?: any) => {
    let n = data ? parseInt(data.toString()) : NaN;
    return !isNaN(n); 
  }

  isValidId = (id?: any) => {
    let nId = id ? parseInt(id.toString()) : NaN;
    return !isNaN(nId) && nId > -1;
  }

  getRequestErrorMessage = (error: any) => {
    const aux = error.response?.data?.message;

    if(aux) {
      const m = aux ? aux as string : "";
      let s = m.indexOf('"');
      let e = m.lastIndexOf('"');
      s = s > -1 ? (s + 1) : 0;
      e = e > -1 ? e : m.length;
      return m.substring(s, e);
    }

    return null;
  }
}


interface Catalogue<T> {
  list: T[];
}

interface RequestData {
  apiData?: IAPIData,
  params?: string[],
  credentials?: Credentials,
  body?: any
}

interface Credentials {
  username?: string;
  password?: string;
  ipuUsername?: string;
  ipuPassword?: string;
}

const appService = new AppService();
export default appService;