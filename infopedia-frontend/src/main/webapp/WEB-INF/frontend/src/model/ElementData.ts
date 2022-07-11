class ElementData {
  public constructor(
    public readonly label: string,
    public readonly forEveryone: boolean,
    public readonly action: () => void
  ) {}
}


export default ElementData;