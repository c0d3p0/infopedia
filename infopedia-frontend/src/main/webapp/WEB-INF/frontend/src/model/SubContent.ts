import SubContentType from "./SubContentType";


class SubContent {
	constructor (
    public readonly id?: number,
    public readonly userId?: number,
    public readonly articleId?: number,
    public readonly position?: number,
    public readonly type?: SubContentType,
    public readonly title?: string,
    public readonly content?: string
  ) {}
}


export default SubContent;