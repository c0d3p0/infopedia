class JsonApiService
{
  constructor()
  {
    this.createRequestObject = (method, headers, body) =>
    {
      const ro = {};
      ro.method = method ? method : "GET";
      ro.headers = headers ? headers : this.createDefaultHeaders();
      ro.body = body ? JSON.stringify(body) : null;
      return ro;
    }

    this.getFinalUrl = (url, appendData) =>
    {
      let u = url;
  
      if(appendData)
      {
        if(typeof appendData === 'object')
        {
          const fields = Object.keys(appendData);
          fields.forEach((item) => u += "/" + appendData[item]);
        }
        else if(Array.isArray(appendData))
          appendData.forEach((item) => u += "/" + item);
        else
          u += "/" + appendData;
      }
      
      return u;
    }

    this.executeFetch = (url, requestObject, responseCallback,
        errorCallback, extras) =>
    {
      const finalExtras = extras ? extras : {};
      fetch(url, requestObject).then((response) =>
      {
        finalExtras.apiResponse = response;
        return response.json();
      })
      .then((data) =>
      {
        finalExtras.apiResponseData = data;

        if(!finalExtras.apiResponse.ok)
          throw Error(finalExtras.apiResponse.statusText);

        if(responseCallback)
          responseCallback(finalExtras);
      })
      .catch((error) =>
      {
        console.log("Problems to execute fetch");
        console.log(error);
        
        if(errorCallback)
        {
          finalExtras.apiResponseError = error;
          errorCallback(finalExtras);
        }
      });
    }
  }
  
  executeRequest = (url, method, appendData, headers, requestBody,
      responseCallback, errorCallback, extras) =>
  {
    const finalUrl = this.getFinalUrl(url, appendData);
    const ro = this.createRequestObject(method, headers, requestBody);
    return this.executeFetch(finalUrl, ro, responseCallback, errorCallback, extras);
  }

  createDefaultHeaders = () =>
  {
    return {"Content-Type": "application/json"};
  }
}


const jsonApiService = new JsonApiService();
export default jsonApiService;