import Article from "./Article";
import Gender from "./Gender";


class User {
  public constructor(
    public readonly id?: number,
    public readonly fullName?: string,
    public readonly age?: number,
    public readonly gender?: Gender,
    public readonly country?: string,
    public readonly email?: string,
    public readonly systemAdmin?: boolean,
    public readonly username?: string,
    public readonly password?: string,
    public readonly token?: string,
    public readonly tokenActiveTime?: number,
    public readonly articleList?: Article[],
  ) {}
}


export default User;