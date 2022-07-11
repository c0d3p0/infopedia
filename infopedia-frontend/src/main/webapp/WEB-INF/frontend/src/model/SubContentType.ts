export enum SubContentType {
  text = "text",
  imageLink = "image-link",
  link = "link"
}

class SubContentTypeUtil {
  public equals = (sct1: SubContentType, sct2?: SubContentType) => {
    return sct1 === sct2;
  }

  public createFrom(text?: string) {
    const t = text?.toLowerCase();
    return text === "image-link" ? SubContentType.imageLink :
        text === "link" ? SubContentType.link : SubContentType.text;
  }

  public isValid(text?: string) {
    const t = text?.toLowerCase();
    return t === "text" || t === "image-link" || t === "link";
  }

  public getValues() {
    return [SubContentType.text, SubContentType.imageLink, SubContentType.link];
  }
}


export const subContentTypeUtil = new SubContentTypeUtil();
export default SubContentType;