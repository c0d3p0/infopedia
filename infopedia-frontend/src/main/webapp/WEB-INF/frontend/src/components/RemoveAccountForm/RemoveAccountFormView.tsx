import "./RemoveAccountForm.css";


export default function RemoveAccountFormView(props: IProps) {
  return (
    <div className="remove-account-form">
      <h1>Remove My Account</h1>
      <hr />
      <form onSubmit={(e) => {e.preventDefault(); props.onRemoveAccountClick()}}>
        <label>Password</label>
        <input
          className="textfield-light"
          type="password"
          value={props.password ?? ""}
          onChange={(e) => {props.setPassword(e.target.value)}}
          placeholder="Password"
        />
        <button className="button-light">Remove Account</button>
      </form>
    </div>
  );
}


interface IProps {
  password: string;
  setPassword(password: string): void;
  onRemoveAccountClick(): void;
}