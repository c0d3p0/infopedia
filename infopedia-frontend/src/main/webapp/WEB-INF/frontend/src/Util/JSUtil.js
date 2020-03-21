class JSUtil
{
  isValidObject = (object) =>
  {
    if(object === null || object === undefined)
      return false;

    return true;
  }

  getField = (object, key, defaultValue) =>
  {
    let data = object[key];
    return data ? data : defaultValue;
  }

  isStringEmpty = (string) =>
  {
    return string.toString().trim().length < 1;
  }

  isEmptyObject = (object) =>
  {
    return object && Object.keys(object).length === 0 &&
        object.constructor === Object;
  }
}

const jsUtil = new JSUtil();
export default jsUtil;
