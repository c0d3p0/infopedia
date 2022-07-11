export enum Gender {
  male = "male",
  female = "female"
}

class GenderUtil {
  public createFrom(text?: string) {
    const g = text?.toLowerCase();
    return g === "female" ? Gender.female : Gender.male;
  }

  public isValid(text?: string) {
    const t = text?.toLowerCase();
    return t === "male" || t === "female";
  }

  public getValues() {
    return [Gender.male, Gender.female];
  }
}



export default Gender;
export const genderUtil = new GenderUtil();