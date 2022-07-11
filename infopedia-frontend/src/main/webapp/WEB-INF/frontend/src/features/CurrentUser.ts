import { createSlice, PayloadAction } from "@reduxjs/toolkit";

import User from "../model/User";


const currentUserSlice = createSlice({
  name: "currentUser",
  initialState: { value: {}},
  reducers:
  {
    setCurrentUser(state, action: PayloadAction<User>) {
      state.value = {...action.payload};
    }
  }
});


export const { setCurrentUser } = currentUserSlice.actions;
export default currentUserSlice.reducer;