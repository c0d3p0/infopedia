import validateMap from "../Data/ValidateMap";


class ValidateService
{
  constructor()
  {
    this.getValidation = (valid, message, data) =>
    {
      return {valid: valid, message: message, data: data};
    }
  }

  testFields = (fields, form) =>
  {
    let validation;
    let field;
    let data = {};

    for(let i = 0; i < fields.length; i++)
    {
      field = fields[i];
      validation = this.testField(field, form);

      if(validation.valid === true)
        data[field] = validation.data;
      else
        return validation;
    }

    return this.getValidation(true, "", data);
  }

  testField = (field, form) =>
  {
    let validate = validateMap[field];
    let value = form[field].value;

    if(validate)
    {
      if(!new RegExp(validate.pattern).test(value))
        return this.getValidation(false, validate.message);
      
      let ef = validate.equalField;

      if(ef && value !== form[ef].value)
      {
        let msg = validateMap[ef].message;
        return this.getValidation(false, msg);
      } 
    }

    return this.getValidation(true, "", value);
  }
}


const validateService = new ValidateService();
export default validateService;