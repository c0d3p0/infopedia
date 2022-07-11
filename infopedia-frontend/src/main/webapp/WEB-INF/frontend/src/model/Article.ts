import User from "./User";
import SubContent from "./SubContent";


class Article {
  constructor(
	  public readonly id?: number,
    public readonly userId?: number,
    public readonly title?: string,
    public readonly content?: string,
    public readonly user?: User,
    public readonly subContentList?: SubContent[]
  ) {}
}


export default Article;